package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

import java.nio.file.FileStore;
import java.nio.file.Files;

public class JavaDiskTest extends UnitTest {

    @Test
    public void testJavaFile()
    {
        JavaFileSystemService jfsService = new JavaFileSystemService();
        String testFolderPath = "/Users/yyzhou/jfs_test";
        DiskService disk = jfsService.diskService(FilePath.parseFilePath(testFolderPath));

        FolderService root = disk.root();
        System.out.println(root.path().toString());

        try {
            FileStore store = Files.getFileStore(root.path().asJavaPath());
            System.out.println("available=" + store.getUsableSpace()
                    + ", total=" + store.getTotalSpace());
        } catch (Exception e) {
            System.out.println("error querying space: " + e.toString());
        }
    }
}
