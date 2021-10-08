package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class JavaFileSystemServiceTest extends UnitTest
{
    @Test
    public void test()
    {
        var filesystem = new JavaFileSystemService();
        ensure(filesystem.accepts(FilePath.parseFilePath("java:jar:file:/whatever/whenever.zip/monkey.txt")));
    }
}
