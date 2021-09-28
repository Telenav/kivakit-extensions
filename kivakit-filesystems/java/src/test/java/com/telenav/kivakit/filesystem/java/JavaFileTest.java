package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.util.HashMap;
import java.util.Map;

public class JavaFileTest extends UnitTest {

    @Test
    public void testJavaFile()
    {
//        final var f = new JFSFile("/Users/yyzhou/text.zip");

        Path zipfile = Paths.get("/Users/yyzhou/text.zip");

        try {
            Map<String,String> env = new HashMap<>();
//            FileSystem fs = FileSystems.newFileSystem(zipfile, env, null);
            FileSystem fs = FileSystems.getDefault();

            for (FileStore store : fs.getFileStores()) {
                String desc = store.toString();
                String type = store.type();
                long totalSpace = store.getTotalSpace();
                long unallocatedSpace = store.getUnallocatedSpace();
                long availableSpace = store.getUsableSpace();
                System.out.println(desc + ", Total: " + totalSpace + ",  Unallocated: "
                        + unallocatedSpace + ",  Available: " + availableSpace);
            }

            for (Path root : fs.getRootDirectories()) {
                System.out.println(root);

                DirectoryStream<Path> dir_stream = Files.newDirectoryStream(root);
                for (Path p : dir_stream) {
                    System.out.println(p);
                }
                dir_stream.close();
            }

        }
        catch (final Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        System.out.println("test jfs file...");
    }
}
