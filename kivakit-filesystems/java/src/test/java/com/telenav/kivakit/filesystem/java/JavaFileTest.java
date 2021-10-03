package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JavaFileTest extends UnitTest
{
    private String path = ".";

    @Before
    public void beforeMethod()
    {
        // use kivakit-extension/kivakit-extensions/kivakit-filesystems/pom.xml as default test case
        path = Paths.get("").toAbsolutePath().getParent().toString();
        path = Paths.get(path, "pom.xml").toString();
    }

    @Test
    public void testJavaFile()
    {
        var file = new JavaFile(path);

        ensure(file.exists());
        ensure(file.sizeInBytes().isGreaterThan(Bytes._0));
    }
}
