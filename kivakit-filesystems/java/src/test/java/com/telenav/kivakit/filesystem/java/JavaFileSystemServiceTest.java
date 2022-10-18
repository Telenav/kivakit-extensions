package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

public class JavaFileSystemServiceTest extends UnitTest
{
    @Test
    public void test()
    {
        var filesystem = new JavaFileSystemService();
        ensure(filesystem.accepts(FilePath.parseFilePath(this, "java:jar:file:/whatever/whenever.zip/monkey.txt")));
    }
}
