package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;


public class JavaFileSystemServiceTest extends UnitTest {

    @Test
    public void testJFSService() {
        JavaFileSystemService jfsService = new JavaFileSystemService();

        String testFolderPath = "/Users/yyzhou/jfs_test";
        FolderService testFolder = jfsService.folderService(FilePath.parseFilePath(testFolderPath));
        for (FileService f : testFolder.files()) {
            System.out.println(f.path().asString());
        }
    }
}
