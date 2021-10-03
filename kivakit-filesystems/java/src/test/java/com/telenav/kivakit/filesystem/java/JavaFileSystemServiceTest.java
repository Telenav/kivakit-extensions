package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JavaFileSystemServiceTest extends UnitTest
{
    private String path = ".";

    @Before
    public void beforeMethod()
    {
        // use kivakit-extension/kivakit-extensions/kivakit-filesystems as default test case
        path = Paths.get("").toAbsolutePath().getParent().toString();
    }

    @Test
    public void testJFSService()
    {
        JavaFileSystemService jfsService = new JavaFileSystemService();

        var filePath = FilePath.parseFilePath(this.path);

        ensure(!path.isBlank() && !path.isEmpty());
        ensure(jfsService.accepts(filePath));
    }
}
