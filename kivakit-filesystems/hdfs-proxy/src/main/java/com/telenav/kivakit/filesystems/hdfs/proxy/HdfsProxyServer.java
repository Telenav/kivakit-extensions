////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.application.Server;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystems.hdfs.proxy.converters.UserGroupInformationConverter;
import com.telenav.kivakit.filesystems.hdfs.proxy.project.lexakai.diagrams.DiagramHdfsProxy;
import com.telenav.kivakit.kernel.KernelProject;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * This server application is compiled into an executable JAR file that is launched by the *kivakit-filesystems-hdfs*
 * package NOTE: When the HDFS proxy is updated, follow the instructions documented in HdfsProxyClient.
 */
@UmlClassDiagram(diagram = DiagramHdfsProxy.class)
@UmlRelation(label = "configures with", referent = HdfsProxyServerSettings.class)
@UmlRelation(label = "delegates to", referent = HdfsFileSystem.class)
public class HdfsProxyServer extends Server implements com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy
{
    private static final Monitor temporaryFileCreationLock = new Monitor();

    public static void main(String[] arguments)
    {
        new HdfsProxyServer().run(arguments);
    }

    private final SwitchParser<Folder> CONFIGURATION_FOLDER = Folder
            .folderSwitchParser("configuration-folder", "Folder containing HDFS configuration files")
            .required()
            .build();

    private final SwitchParser<UserGroupInformation> USERNAME = SwitchParser
            .builder(UserGroupInformation.class)
            .name("username")
            .description("HDFS remote user name")
            .converter(new UserGroupInformationConverter(this))
            .required()
            .build();

    private final SwitchParser<Integer> DATA_PORT = SwitchParser
            .integerSwitchParser("data-port", "The port to use when responding to data requests")
            .required()
            .build();

    private final SwitchParser<Integer> RMI_OBJECT_PORT = SwitchParser
            .integerSwitchParser("rmi-object-port", "The port to use when registering the remote proxy object")
            .required()
            .build();

    private Time lastRequest = Time.now();

    private final Map<StreamHandle, InputStream> hdfsIns = new HashMap<>();

    private final Map<StreamHandle, OutputStream> hdfsOuts = new HashMap<>();

    public HdfsProxyServer()
    {
        super(KernelProject.get());

        // Shut the proxy server down when it hasn't been used in a while to prevent stuck proxy servers
        Duration.minutes(1).every(timer ->
        {
            if (lastRequest.elapsedSince().isGreaterThan(Duration.minutes(5)))
            {
                System.exit(0);
            }
        });
    }

    @Override
    public synchronized boolean deleteFile(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return isFile(pathAsString) && fileSystem(path).delete(hdfsPath(path), false);
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to delete $", path);
        }
        return false;
    }

    @Override
    public synchronized boolean deleteFolder(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return isFolder(pathAsString) && fileSystem(path).delete(hdfsPath(path), false);
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to delete $", path);
        }
        return false;
    }

    @Override
    public synchronized boolean exists(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return fileSystem(path).exists(hdfsPath(path));
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine if $ exists", path);
        }
        return false;
    }

    @Override
    public synchronized List<String> files(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        return files(pathAsString, false);
    }

    @Override
    public synchronized List<String> folders(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            List<String> folders = new ArrayList<>();
            var hdfsPath = hdfsPath(path);
            var fileSystem = fileSystem(path);
            for (FileStatus status : fileSystem.listStatus(hdfsPath))
            {
                if (status.isDirectory())
                {
                    folders.add(status.getPath().toString());
                }
            }
            return folders;
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to list files in $", path);
            return null;
        }
    }

    @Override
    public synchronized boolean isFile(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return fileSystem(path).getFileStatus(hdfsPath(path)).isFile();
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine if $ is a file", path);
        }
        return false;
    }

    @Override
    public synchronized boolean isFolder(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return fileSystem(path).getFileStatus(normalizeFolderPath(hdfsPath(path))).isDirectory();
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine if $ is a folder", path);
        }
        return false;
    }

    @Override
    public synchronized boolean isWritable(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            fileSystem(path).access(hdfsPath(path), FsAction.WRITE);
            return true;
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine if $ is writable", path);
        }
        return false;
    }

    @Override
    public synchronized long lastModified(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return fileSystem(path).getFileStatus(hdfsPath(path)).getModificationTime();
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine last modification time of $", path);
        }
        return -1;
    }

    @Override
    public synchronized boolean lastModified(String pathAsString, long time) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            fileSystem(path).setTimes(hdfsPath(path), time, -1L);
            return true;
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to set last modification time of $", path);
            return false;
        }
    }

    @Override
    public synchronized long length(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            if (isFile(pathAsString))
            {
                FileStatus status = fileSystem(path).getFileStatus(hdfsPath(path));
                return status.getLen();
            }
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to determine length of $", path);
        }
        return -1L;
    }

    @Override
    public synchronized boolean mkdirs(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            return fileSystem(path).mkdirs(hdfsPath(path),
                    new FsPermission(FsAction.ALL, FsAction.ALL, FsAction.ALL, false));
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to create folders in $", path);
        }
        return false;
    }

    @Override
    public synchronized List<String> nestedFiles(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        return files(pathAsString, true);
    }

    @Override
    public synchronized long openForReading(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            InputStream in = fileSystem(path).open(hdfsPath(path));
            StreamHandle handle = StreamHandle.create();
            hdfsIns.put(handle, new BufferedInputStream(in));
            return handle.identifier().asLong();
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to open $ for reading", path);
            return -1;
        }
    }

    @Override
    public synchronized long openForWriting(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            OutputStream out = fileSystem(path).create(hdfsPath(path));
            StreamHandle handle = StreamHandle.create();
            hdfsOuts.put(handle, new BufferedOutputStream(out));
            return handle.identifier().asLong();
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to open $ for writing", path);
            return -1;
        }
    }

    @Override
    public synchronized boolean rename(String fromAsString, String toAsString) throws RemoteException
    {
        lastRequest = Time.now();
        var from = FilePath.parseFilePath(fromAsString);
        var to = FilePath.parseFilePath(toAsString);
        try
        {
            return fileSystem(from).rename(hdfsPath(from), hdfsPath(to));
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to rename $ to $", from, to);
            return false;
        }
    }

    @Override
    public synchronized String root(String pathAsString) throws RemoteException
    {
        lastRequest = Time.now();
        return FilePath.parseFilePath(pathAsString).root().toString();
    }

    @Override
    @SuppressWarnings("EmptyTryBlock")
    public synchronized String temporaryFile(String pathAsString, String baseName) throws RemoteException
    {
        lastRequest = Time.now();
        synchronized (temporaryFileCreationLock)
        {
            var path = FilePath.parseFilePath(pathAsString);
            int sequenceNumber = 0;
            FilePath file;
            do
            {
                file = path.file(FileName.parse(baseName).withSuffix("-" + sequenceNumber + ".tmp"));
                sequenceNumber++;
            }
            while (exists(file.toString()));
            try (OutputStream ignored = fileSystem(path).create(hdfsPath(path)))
            {
                // We don't need to do anything with the file except create it
            }
            catch (Exception e)
            {
                throwRemoteException(e, "Unable to create temporary file in $ with base name $", path, baseName);
                return null;
            }
            return file.asStringPath().toString();
        }
    }

    @Override
    public synchronized String temporaryFolder(String pathAsString, String baseName) throws RemoteException
    {
        lastRequest = Time.now();
        var path = FilePath.parseFilePath(pathAsString);
        synchronized (temporaryFileCreationLock)
        {
            int sequenceNumber = 0;
            FilePath folder;
            do
            {
                folder = path.withChild(FileName.parse(baseName).withSuffix("-" + sequenceNumber + ".tmp").name());
                sequenceNumber++;
            }
            while (exists(pathAsString));
            var temporaryPathAsString = folder.asStringPath().toString();
            mkdirs(temporaryPathAsString);
            return temporaryPathAsString;
        }
    }

    @Override
    protected void onRun()
    {
        try
        {
            // Register proxy server settings
            registerSettings(new HdfsProxyServerSettings()
                    .configurationFolder(get(CONFIGURATION_FOLDER))
                    .user(get(USERNAME)));

            announce("Exporting remote HdfsProxyServer object");
            var rmiObjectPort = get(RMI_OBJECT_PORT);
            com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy proxy = (com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy) UnicastRemoteObject.exportObject(this, rmiObjectPort);
            narrate("Creating RMI registry on port $", com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy.RMI_REGISTRY_PORT);
            Registry registry = LocateRegistry.createRegistry(com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy.RMI_REGISTRY_PORT);
            narrate("Binding remote object to RMI registry on port $", rmiObjectPort);
            registry.bind(com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy.RMI_REGISTRY_NAME + "-" + rmiObjectPort, proxy);
            announce("HDFS proxy server is ready");
            listen();
        }
        catch (Exception e)
        {
            fail(e, "Unable to start proxy server");
        }
    }

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.of(
                DATA_PORT,
                RMI_OBJECT_PORT,
                CONFIGURATION_FOLDER,
                USERNAME);
    }

    private FileSystem fileSystem(FilePath path)
    {
        return HdfsFileSystem.of(path).fileSystem();
    }

    private List<String> files(String pathAsString, boolean recursive) throws RemoteException
    {
        var path = FilePath.parseFilePath(pathAsString);
        try
        {
            List<String> files = new ArrayList<>();
            RemoteIterator<LocatedFileStatus> iterator = fileSystem(path).listFiles(hdfsPath(path), recursive);
            while (iterator.hasNext())
            {
                LocatedFileStatus status = iterator.next();
                if (status.isFile())
                {
                    files.add(status.getPath().toString());
                }
            }
            return files;
        }
        catch (Exception e)
        {
            throwRemoteException(e, "Unable to list files in " + path);
            return null;
        }
    }

    private void handleRequest(Socket socket)
    {
        KivaKitThread.run(this, "DataRequestHandler", () ->
        {
            try
            {
                try (DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                     OutputStream out = new BufferedOutputStream(socket.getOutputStream()))
                {
                    char command = in.readChar();
                    long handle = in.readLong();
                    switch (command)
                    {
                        case 'i':
                            try (InputStream hdfsIn = hdfsIns.get(StreamHandle.of(handle)))
                            {
                                IO.copy(hdfsIn, out);
                            }
                            break;

                        case 'o':
                            try (OutputStream hdfsOut = hdfsOuts.get(StreamHandle.of(handle)))
                            {
                                IO.copy(in, hdfsOut);
                            }
                            break;

                        default:
                            problem("Didn't understand client command: " + command);
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                problem(e, "Failure handling request");
            }
        });
    }

    private Path hdfsPath(FilePath path)
    {
        return HdfsFileSystem.of(path).hdfsPath(path);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void listen()
    {
        try (ServerSocket serverSocket = new ServerSocket(get(DATA_PORT)))
        {
            information("Waiting for requests");
            while (true)
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    if (socket != null)
                    {
                        handleRequest(socket);
                    }
                }
                catch (Exception e)
                {
                    problem(e, "Failure handling request");
                }
            }
        }
        catch (Exception e)
        {
            problem(e, "Failure listening for connections");
        }
    }

    private Path normalizeFolderPath(Path path)
    {
        if (!path.toString().endsWith("/"))
        {
            return new Path(path + "/");
        }
        return path;
    }

    private void throwRemoteException(
            Exception cause, String message, Object... arguments) throws RemoteException
    {
        problem(cause, message, arguments);
        throw new RemoteException(Message.format(message, arguments), cause);
    }
}
