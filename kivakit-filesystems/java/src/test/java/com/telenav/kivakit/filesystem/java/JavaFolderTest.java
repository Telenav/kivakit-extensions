package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

import java.util.List;

public class JavaFolderTest extends UnitTest {

    public class JavaFileMatcher implements Matcher {

        @Override
        public boolean matches(Object o) {
            return true;
        }
    }

    @Test
    public void testJavaFolder() {

        try {
            FolderService folder = new JavaFolder("/Users/yyzhou/jfs_test");
            final boolean w = folder.isWritable();
            System.out.println(w);

//            FolderService folder1 = new JavaFolder("/Users/yyzhou/jfs_test1");
//            folder.copyTo()

            for (FileService f : folder.files()) {
                System.out.println(f.path().toString());
            }

            for (FolderService f : folder.folders()) {
                System.out.println(f.path().toString());
            }

            for (FileService f : (List<FileService>)folder.nestedFiles(new JavaFileMatcher())) {
                System.out.println(f.path().toString());
            }

            for (FolderService f : (List<FolderService>)folder.nestedFolders(new JavaFileMatcher())) {
                System.out.println(f.path().toString());
            }

            FolderService subFolder = folder.folder(FileName.parse("jfs_sub1"));
            for (FileService f : subFolder.files()) {
                System.out.println(f.path().toString());
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
