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

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy;
import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.threading.Retry;
import com.telenav.kivakit.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FolderService} used to provide {@link HdfsFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfs.class)
@UmlNotPublicApi
@UmlRelation(label = "gets proxy from", referent = HdfsProxyClient.class)
@LexakaiJavadoc(complete = true)
public class HdfsFolder extends BaseComponent implements FolderService
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Monitor temporaryLock = new Monitor();

    private final FilePath path;

    @UmlAggregation
    private HdfsProxy proxy;

    public HdfsFolder(FilePath path)
    {
        this.path = path;
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public FolderService clear()
    {
        for (var folder : folders())
        {
            folder.clear();
        }
        for (var file : files())
        {
            file.delete();
        }
        return this;
    }

    @Override
    public Time created()
    {
        return unsupported();
    }

    @Override
    public boolean delete()
    {
        return retry(() -> proxy().deleteFolder(pathAsString()))
                .orDefault(null, "Unable to delete $", this);
    }

    @Override
    public boolean exists()
    {
        return retry(() -> proxy().exists(pathAsString())).orDefault(false, "Unable to determine if $ exists", this);
    }

    @Override
    public HdfsFile file(FileName name)
    {
        return new HdfsFile(path.file(name));
    }

    @Override
    public List<FileService> files()
    {
        return retry(() -> matching(proxy().files(pathAsString()), new AnythingMatcher<>())).orDefault(new ArrayList<>(), "Unable to locate files in $", this);
    }

    @Override
    public List<FileService> files(Matcher<FilePath> matcher)
    {
        return retry(() -> matching(proxy().files(pathAsString()), matcher)).orDefault(new ArrayList<>(), "Unable to locate files in $", this);
    }

    @Override
    public HdfsFolder folder(FileName name)
    {
        return new HdfsFolder(path().file(name));
    }

    @Override
    public HdfsFolder folder(Folder folder)
    {
        return new HdfsFolder(path().withChild(folder.path()));
    }

    @Override
    public List<FolderService> folders()
    {
        return folders(new AnythingMatcher<>());
    }

    @Override
    public List<FolderService> folders(Matcher<FilePath> matcher)
    {
        return retry(() ->
        {
            var files = new ArrayList<FolderService>();
            for (var pathAsString : proxy().folders(pathAsString()))
            {
                var path = FilePath.parseFilePath(this, pathAsString);
                if (matcher.matches(path))
                {
                    files.add(new HdfsFolder(path));
                }
            }

            return files;
        }).orDefault(new ArrayList<>(), "Unable to locate folders in $", this);
    }

    @Override
    public boolean isEmpty()
    {
        Iterable<FolderService> folders = folders();
        if (folders != null && folders.iterator().hasNext())
        {
            return false;
        }

        Iterable<FileService> files = files();
        return files == null || !files.iterator().hasNext();
    }

    @Override
    public boolean isFolder()
    {
        return retry(() -> proxy().isFolder(pathAsString()))
                .orDefault(false, "Unable to determine if $ is a folder", this);
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public Boolean isWritable()
    {
        return exists() && retry(() -> proxy().isWritable(pathAsString())).orDefault(false, "Unable to determine if $ is writable", this);
    }

    @Override
    public Time lastModified()
    {
        return retry(() -> Time.milliseconds(proxy().lastModified(pathAsString())))
                .orDefault(null, "Unable to determine modification time of $", this);
    }

    @Override
    public HdfsFolder mkdirs()
    {
        return retry(() ->
        {
            if (proxy().mkdirs(pathAsString()))
            {
                return new HdfsFolder(path);
            }
            return null;
        }).orDefault(null, "Unable to create folder path $", this);
    }

    public String name()
    {
        return path().fileName().name();
    }

    public List<FileService> nestedFiles()
    {
        return retry(() -> matching(proxy().nestedFiles(pathAsString()), new AnythingMatcher<>())).orDefault(new ArrayList<>(), "Unable to locate files in $", this);
    }

    @Override
    public List<FileService> nestedFiles(Matcher<FilePath> matcher)
    {
        return retry(() -> matching(proxy().nestedFiles(pathAsString()), matcher)).orDefault(new ArrayList<>(), "Unable to locate files in $", this);
    }

    @Override
    public List<FolderService> nestedFolders(Matcher<FilePath> matcher)
    {
        return unsupported();
    }

    @Override
    public HdfsFolder parent()
    {
        var parent = path().parent();
        if (parent != null)
        {
            return new HdfsFolder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return path;
    }

    @Override
    public boolean renameTo(FolderService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((FolderService) that.resolveService());
        }
        fail("Cannot rename $ to $ across filesystems", this, that);
        return false;
    }

    public boolean renameTo(HdfsFolder to)
    {
        return retry(() -> proxy().rename(pathAsString(), to.path().toString()))
                .orDefault(false, "Unable to rename $ to $", this, to);
    }

    @Override
    public HdfsFolder root()
    {
        return new HdfsFolder(path.root());
    }

    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }

    @SuppressWarnings("EmptyTryBlock")
    @Override
    public FileService temporaryFile(FileName baseName)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;

            FilePath file;
            do
            {
                file = path.withChild(baseName.withSuffix("-" + sequenceNumber + ".tmp").name());
                sequenceNumber++;
            }
            while (new HdfsFile(file).exists());

            var hdfsFile = new HdfsFile(file);
            try (var ignored = hdfsFile.openForWriting())
            {
                // File has been created
            }
            catch (Exception e)
            {
                LOGGER.warning("Unable to create file $", hdfsFile);
                return null;
            }
            return hdfsFile;
        }
    }

    @Override
    public FolderService temporaryFolder(FileName baseName)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;
            FilePath folder;
            do
            {
                folder = path.withChild(baseName.withSuffix("-" + sequenceNumber + ".tmp").name());
                sequenceNumber++;
            }
            while (new HdfsFile(folder).exists());

            var hdfsFolder = new HdfsFolder(folder);
            hdfsFolder.mkdirs();
            return hdfsFolder;
        }
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    private List<FileService> matching(List<String> paths, Matcher<FilePath> matcher)
    {
        var files = new ArrayList<FileService>();
        for (var pathAsString : paths)
        {
            var path = FilePath.parseFilePath(this, pathAsString);
            if (matcher.matches(path))
            {
                files.add(new HdfsFile(path));
            }
        }
        return files;
    }

    private String pathAsString()
    {
        return path().toString();
    }

    private HdfsProxy proxy()
    {
        if (proxy == null)
        {
            proxy = HdfsProxyClient.get().proxy();
        }
        return proxy;
    }

    private <T> Unchecked<T> retry(Unchecked<T> code)
    {
        return Retry.retry(code, 16, Duration.seconds(15), () -> proxy = null);
    }
}
