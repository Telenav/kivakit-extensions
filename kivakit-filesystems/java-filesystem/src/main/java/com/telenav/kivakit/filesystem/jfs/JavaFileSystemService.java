package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.path.FilePath;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

// @yinyin a little comment and add your name as @author
public class JavaFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(FilePath path)
    {
        // @yinyin this should do
        return path.startsWith("java:");
    }

    @Override
    public DiskService diskService(FilePath path)
    {
        // @yinyin We should be able to implement this (I think)
        return unsupported();
    }

    @Override
    public FileService fileService(FilePath path)
    {
        return new JavaFile(path);
    }

    @Override
    public FolderService folderService(FilePath path)
    {
        return new JavaFolder(path);
    }
}
