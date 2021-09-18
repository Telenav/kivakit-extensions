package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;

import com.telenav.kivakit.resource.path.FilePath;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;


public class JavaFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(FilePath path) {
        // TODO: accept paths begin with "jfs://" or "zip://"
        return true;
    }

    @Override
    public DiskService diskService(FilePath path) {
        return unsupported();
    }

    @Override
    public FileService fileService(FilePath path) {
        return new JavaFile(path);
    }

    @Override
    public FolderService folderService(FilePath path) {
        return new JavaFolder(path);
    }
}
