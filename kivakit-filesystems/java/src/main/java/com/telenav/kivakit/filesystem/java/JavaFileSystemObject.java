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

import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * Base functionality common to both {@link JavaFile} and {@link JavaFolder}.
 *
 * @author yinyinz
 */
public class JavaFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    // True if it's a folder
    private final boolean isFolder;

    private final Path javaPath;

    public JavaFileSystemObject(final FilePath path, final boolean isFolder)
    {
        super(path);

        this.javaPath = path.asJavaPath();
        this.isFolder = isFolder;
    }

    public JavaFileSystemObject(final Path path)
    {
        super((normalize(path)));
        this.javaPath = path;
        this.isFolder = Files.isDirectory(this.javaPath);
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
            Files.copy(this.javaPath, destination.path().asJavaPath());
        }
        catch (Exception e)
        {
            problem(e, "Unable to copy $ to $ ($)", path(), destination.path(), mode);
        }
    }

    @Override
    public boolean isFolder()
    {
        return isFolder;
    }

    @Override
    public Boolean isWritable()
    {
        return Files.isWritable(this.javaPath);
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
    public boolean exists()
    {
        return Files.exists(this.javaPath);
    }

    @Override
    public FolderService parent()
    {
        return new JavaFolder(this.javaPath.getParent());
    }

    @Override
    public FilePath path()
    {
        return FilePath.parseFilePath(super.path().toString());
    }

    @Override
    public FolderService root()
    {
        return new JavaFolder(this.javaPath.getRoot());
    }

    private static FilePath normalize(final Path path)
    {
        return FilePath.filePath(path);
    }

    @Override
    public Time created()
    {
        try
        {
            FileTime creationTime = (FileTime) Files.getAttribute(this.javaPath, "creationTime");
            return Time.milliseconds(creationTime.toMillis());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Path toJavaPath()
    {
        return this.javaPath;
    }
}
