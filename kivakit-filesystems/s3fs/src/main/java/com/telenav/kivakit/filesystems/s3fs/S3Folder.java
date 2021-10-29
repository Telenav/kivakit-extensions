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

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;

import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FolderService} used to provide {@link S3FileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramS3.class)
@LexakaiJavadoc(complete = true)
public class S3Folder extends S3FileSystemObject implements FolderService
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Monitor LOCK = new Monitor();

    // In the S3 world, there no folder physically, so we created a metadata file to
    // represents its existence
    private static final FileName METADATA = FileName.parse(".metadata");

    public S3Folder(FilePath path)
    {
        super(path, true);
    }

    public S3Folder(String path)
    {
        this(FilePath.parseFilePath(path));
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public S3Folder clear()
    {
        for (var folder : folders())
        {
            folder.clear();
            folder.delete();
        }

        for (var file : files())
        {
            if (!isMetadata(file.fileName()))
            {
                file.delete();
            }
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
        if (!isEmpty())
        {
            LOGGER.warning("Can't delete a non-empty folder of " + this);
            return false;
        }

        // delete the meta data file
        var metaFile = metadataFile();
        if (metaFile.exists())
        {
            metaFile.delete();
        }

        // delete this s3 object
        return super.delete();
    }

    @Override
    public void ensureExists()
    {
        if (!exists())
        {
            mkdirs();
        }
    }

    @Override
    public boolean exists()
    {
        return super.exists() || metadataFile().exists() || !isEmpty();
    }

    @Override
    public S3File file(FileName fileName)
    {
        return new S3File(path().withChild(fileName.name()));
    }

    @Override
    public List<FileService> files()
    {
        var request = ListObjectsRequest.builder()
                .bucket(bucket())
                .build();

        var response = client().listObjects(request);

        List<FileService> files = new ArrayList<>();
        for (var object : response.contents())
        {
            var path = S3FileSystemObject.path(scheme(), bucket(), object.key());
            files.add(new S3File(path));
        }
        return files;
    }

    @Override
    public List<FileService> files(Matcher<FilePath> matcher)
    {
        List<FileService> files = new ArrayList<>();
        for (var file : files())
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public S3Folder folder(FileName name)
    {
        return new S3Folder(FilePath.parseFilePath(child(name).toString()));
    }

    @Override
    public S3Folder folder(Folder folder)
    {
        return new S3Folder(FilePath.parseFilePath(child(folder).toString()));
    }

    @Override
    public List<FolderService> folders()
    {
        var request = ListBucketsRequest.builder().build();
        var response = client().listBuckets(request);

        List<FolderService> folders = new ArrayList<>();
        for (var bucket : response.buckets())
        {
            var path = S3FileSystemObject.path(scheme(), bucket.name(), "");
            folders.add(new S3Folder(path));
        }
        return folders;
    }

    public boolean hasFiles()
    {
        return exists() && files().iterator().hasNext();
    }

    public boolean hasSubFolders()
    {
        return exists() && folders().iterator().hasNext();
    }

    @Override
    public boolean isEmpty()
    {
        Iterable<FolderService> folders = folders();
        if (folders != null && folders().iterator().hasNext())
        {
            return false;
        }
        for (var file : files())
        {
            if (!file.fileName().equals(METADATA))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public Boolean isWritable()
    {
        return false;
    }

    @Override
    public S3Folder mkdirs()
    {
        var folder = this;
        while (folder != null && !folder.equals(root()))
        {
            mkdir(folder);
            folder = folder.parent();
        }
        return this;
    }

    @Override
    public List<FileService> nestedFiles(Matcher<FilePath> matcher)
    {
        List<FileService> files = new ArrayList<>();
        for (var file : nestedFiles(this, new ArrayList<>()))
        {
            if (matcher.matches(file.path()))
            {
                files.add(file);
            }
        }
        return files;
    }

    @Override
    public List<FolderService> nestedFolders(Matcher<FilePath> matcher)
    {
        List<FolderService> folders = new ArrayList<>();
        for (var at : nestedFolders(this, new ArrayList<>()))
        {
            if (matcher.matches(at.path()))
            {
                folders.add(at);
            }
        }
        return folders;
    }

    @Override
    public boolean renameTo(FolderService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((S3Folder) that.resolveService());
        }
        fail("Cannot rename $ to $ across filesystems", this, that);
        return false;
    }

    public boolean renameTo(S3Folder that)
    {
        if (exists() && canRenameTo(that))
        {
            for (var file : files())
            {
                file.renameTo(that.file(file.fileName()));
            }
            for (var folder : folders())
            {
                folder.renameTo(that.folder(folder.fileName()));
            }
            metadataFile().renameTo(that.metadataFile());
            return true;
        }
        return false;
    }

    @Override
    public S3Folder root()
    {
        var path = path(scheme(), bucket(), "");
        return new S3Folder(path);
    }

    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }

    @Override
    public S3File temporaryFile(FileName baseName)
    {
        synchronized (LOCK)
        {
            var sequenceNumber = 0;
            S3File file;
            do
            {
                file = file(baseName.withSuffix("-" + sequenceNumber + ".tmp"));
                sequenceNumber++;
            }
            while (file.exists());
            return file;
        }
    }

    @Override
    public S3Folder temporaryFolder(FileName baseName)
    {
        synchronized (LOCK)
        {
            var sequenceNumber = 0;
            S3Folder folder;
            do
            {
                folder = folder(baseName.withSuffix("-" + sequenceNumber + ".tmp"));
                sequenceNumber++;
            }
            while (folder.exists());
            folder.mkdirs();
            return folder;
        }
    }

    private FilePath child(FileName child)
    {
        return path().withChild(child.name());
    }

    private FilePath child(Folder folder)
    {
        return path().withChild(folder.toString());
    }

    private boolean isMetadata(FileName fileName)
    {
        return fileName.equals(METADATA);
    }

    private S3File metadataFile()
    {
        return file(METADATA);
    }

    private void mkdir(S3Folder folder)
    {
        var file = folder.file(METADATA);
        if (!file.exists())
        {
            // Create a place holder for every folder to make sure it's a folder
            // conceptually
            file.write(Time.now().toString());
        }
    }

    private List<FileService> nestedFiles(FolderService folder, List<FileService> files)
    {
        files.addAll(folder.files());
        for (var at : folders())
        {
            nestedFiles(at, files);
        }
        return files;
    }

    private List<FolderService> nestedFolders(FolderService folder, List<FolderService> folders)
    {
        folders.add(this);
        for (var at : folder.folders())
        {
            nestedFolders(at, folders);
        }
        return folders;
    }
}
