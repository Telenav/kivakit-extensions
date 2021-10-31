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

package com.telenav.kivakit.filesystems.hdfs.proxy.spi;

import com.telenav.kivakit.filesystems.hdfs.proxy.spi.project.lexakai.diagrams.DiagramHdfsSpi;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RMI interface for the HdfsProxy. See *kivakit-filesystems-hdfs-proxy* and *kivakit-filesystems-hdfs* for a detailed
 * discussion.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfsSpi.class)
@LexakaiJavadoc(complete = true)
public interface HdfsProxy extends Remote
{
    /** The name of the proxy client's entry in the RMI registry */
    String RMI_REGISTRY_NAME = "kivakit-hdfs-proxy";

    /** The RMI registry port being used */
    int RMI_REGISTRY_PORT = 1099;

    Logger LOGGER = LoggerFactory.newLogger();

    Version VERSION = Version.parse(LOGGER, "0.9");

    boolean deleteFile(String path) throws RemoteException;

    boolean deleteFolder(String path) throws RemoteException;

    boolean exists(String path) throws RemoteException;

    List<String> files(String path) throws RemoteException;

    List<String> folders(String path) throws RemoteException;

    boolean isFile(String path) throws RemoteException;

    boolean isFolder(String path) throws RemoteException;

    boolean isWritable(String path) throws RemoteException;

    long lastModified(String path) throws RemoteException;

    boolean lastModified(String pathAsString, long time) throws RemoteException;

    long length(String path) throws RemoteException;

    boolean mkdirs(String path) throws RemoteException;

    List<String> nestedFiles(String path) throws RemoteException;

    long openForReading(String path) throws RemoteException;

    long openForWriting(String path) throws RemoteException;

    boolean rename(String from, String to) throws RemoteException;

    String root(String path) throws RemoteException;

    String temporaryFile(String path, String baseName) throws RemoteException;

    String temporaryFolder(String path, String baseName) throws RemoteException;
}
