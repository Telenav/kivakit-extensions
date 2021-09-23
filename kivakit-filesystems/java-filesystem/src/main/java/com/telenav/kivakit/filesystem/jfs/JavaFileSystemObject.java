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

public class JavaFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    // True if it's a folder
    private final boolean isFolder;

    protected final Path javaPath;

    public JavaFileSystemObject(final FilePath path, final boolean isFolder) {
        super(path);

        this.javaPath = path.asJavaPath();
        this.isFolder = isFolder;
    }

    public JavaFileSystemObject(final Path path) {
        super((normalize(path)));
        this.javaPath = path;
        this.isFolder = Files.isDirectory(this.javaPath);
    }

    @Override
    public FilePath path() {
        return FilePath.parseFilePath(super.path().toString());
    }

    @Override
    public InputStream onOpenForReading() {
        return unsupported();
    }

    @Override
    public Boolean isWritable() {
        return Files.isWritable(this.javaPath);
    }

    @Override
    public OutputStream onOpenForWriting() {
        return unsupported();
    }

    @Override
    public boolean isFolder() {
        return isFolder;
    }

    @Override
    public FolderService parent() {
        return new JavaFolder(this.javaPath.getParent());
    }

    @Override
    public FolderService root() {
        return new JavaFolder(this.javaPath.getRoot());
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions) {
        return FileSystemObjectService.super.chmod(permissions);
    }

    private static FilePath normalize(final Path path) {
        return FilePath.filePath(path);
    }

    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter) {
        try {
            Files.copy(this.javaPath, destination.path().asJavaPath());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
