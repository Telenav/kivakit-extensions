package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JavaDiskTest extends UnitTest
{
    private String path = ".";

    @Before
    public void beforeMethod()
    {
        // use kivakit-extension/kivakit-extensions/kivakit-filesystems as default test case
        path = Paths.get("").toAbsolutePath().getParent().toString();
    }

    @Test
    public void testJavaDisk()
    {
        DiskService disk = new JavaDisk(path);

        FolderService root = disk.root();

        var totalSize = disk.size();
        var freeSize = disk.free();
        var usableSize = disk.usable();

        ensure(totalSize.isNonZero());
        ensure(totalSize.isGreaterThan(freeSize));
        ensure(freeSize.isGreaterThanOrEqualTo(usableSize));
    }
}
