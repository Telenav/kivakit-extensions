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

package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Base functionality common to both {@link JavaFile} and {@link JavaFolder}.
 *
 * @author yinyinz
 * @author jonathanl (shibo)
 */
public class JavaFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    /** Java NIO filesystem */
    private FileSystem filesystem;

    /** Java NIO path on the given filesystem */
    private Path path;

    public JavaFileSystemObject(FilePath path)
    {
        super(path);

        var pathString = path.toString();
        if (pathString.contains("!/"))
        {
            String head = Paths.head(pathString, "!/");
            String tail = Paths.tail(pathString, "!/");

            var uri = URI.create(head);
            filesystem = Nio.filesystem(Listener.deafListener(), uri);
            if (filesystem != null)
            {
                this.path = filesystem.getPath(tail);
            }
        }
        else
        {
            // Start with the whole path and work backwards, removing the last component of the path each time,
            for (var at = path; !at.isEmpty(); at = at.withoutLast())
            {
                // until we find a filesystem.
                var uri = URI.create(at.toString());
                filesystem = Nio.filesystem(Listener.deafListener(), uri);
                if (filesystem != null)
                {
                    this.path = path().last(path().size() - at.size()).withoutSchemes().asJavaPath();
                    break;
                }
            }
        }

        ensureNotNull(filesystem, "Not a valid Java filesystem");
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
    {
        return FileSystemObjectService.super.chmod(permissions);
    }

    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter)
    {
        try
        {
            Files.copy(javaPath(), destination.path().asJavaPath());
        }
        catch (Exception e)
        {
            problem(e, "Unable to copy $ to $ ($)", path(), destination.path(), mode);
        }
    }

    @Override
    public Time createdAt()
    {
        try
        {
            var creationTime = (FileTime) Files.getAttribute(javaPath(), "creationTime");
            return Time.epochMilliseconds(creationTime.toMillis());
        }
        catch (IOException e)
        {
            problem(e, "Unable to determine file creation time of: $", javaPath());
        }

        return null;
    }

    @Override
    public boolean exists()
    {
        return Files.exists(javaPath());
    }

    @Override
    public boolean isFolder()
    {
        return this instanceof JavaFolder;
    }

    @Override
    public boolean isRemote()
    {
        return FileSystemObjectService.super.isRemote();
    }

    @Override
    public Boolean isWritable()
    {
        return Files.isWritable(javaPath());
    }

    public Path javaPath()
    {
        return filesystem.getPath(path.toString()).toAbsolutePath();
    }

    @Override
    public InputStream onOpenForReading()
    {
        return unsupported();
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return unsupported();
    }

    @Override
    public FolderService parentService()
    {
        return new JavaFolder(path().parent());
    }

    @Override
    public FilePath path()
    {
        return FilePath.parseFilePath(this, super.path().toString());
    }

    @Override
    public FolderService root()
    {
        return new JavaFolder(path().root());
    }
}
