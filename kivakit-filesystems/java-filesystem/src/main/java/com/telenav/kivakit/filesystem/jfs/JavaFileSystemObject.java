package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

// @yinyin a little comment and add your name as @author
public class JavaFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    // True if it's a folder
    private final boolean isFolder;

    protected final Path javaPath;

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
            // @yinyin report errors like this where possible and they will go into the messaging system...
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
}
