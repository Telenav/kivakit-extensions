package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class JavaFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{

    // True if it's a folder
    private final boolean isFolder;

    private FileSystem jfs;

    public JavaFileSystemObject(final FilePath path, final boolean isFolder) {

        super(normalize(path));

        this.isFolder = isFolder;
    }

    FileSystem jfs() {
        if (jfs == null) {
            Path filepath = Paths.get(path().toString());
            Map<String,String> env = new HashMap<>();
            try {
                jfs = FileSystems.newFileSystem(filepath, (ClassLoader) null);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            }
        }

        return jfs;
    }

    @Override
    public FilePath path() {
        return FilePath.parseFilePath(super.path().toString());
    }

    @Override
    public InputStream onOpenForReading() {
        return null;
    }

    @Override
    public Boolean isWritable() {
        return null;
    }

    @Override
    public OutputStream onOpenForWriting() {
        return null;
    }

    @Override
    public boolean isFolder() {
        return isFolder;
    }

    @Override
    public FolderService parent() {
        final var parent = path().parent();
        if (parent != null) {
            return new JavaFolder(parent);
        }
        return null;
    }

    @Override
    public FolderService root() {
        return null;
    }

    private static FilePath normalize(final FilePath path)
    {
        return path;
    }

}
