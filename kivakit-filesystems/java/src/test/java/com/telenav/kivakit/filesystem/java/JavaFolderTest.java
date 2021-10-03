package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class JavaFolderTest extends UnitTest
{
    private String path = ".";

    @Before
    public void beforeMethod()
    {
        // use kivakit-extension/kivakit-extensions/kivakit-filesystems as default test case
        path = Paths.get("").toAbsolutePath().getParent().toString();
    }

    public class PomFileMatcher implements Matcher<FilePath>
    {
        @Override
        public boolean matches(final FilePath path)
        {
            return path.endsWith("pom.xml");
        }
    }

    public class SrcFolderMatcher implements Matcher<FilePath>
    {
        @Override
        public boolean matches(final FilePath path)
        {
            return path.endsWith("src");
        }
    }

    @Test
    public void testJavaFolder()
    {
        FolderService folder = new JavaFolder(path);

        ensure(folder.isFolder());
        ensureFalse(folder.isEmpty());
        ensureFalse(folder.isFile());

        ensure(folder.exists());
        ensure(folder.hasFiles());
        ensure(folder.hasSubFolders());
        ensure(folder.isWritable());

        // 6 submodule folders should contain src folder
        ensureEqual(folder.nestedFolders(new SrcFolderMatcher()).size(), 6);

        // 6 submodule folders + 1 module folder should contain pom.xml file
        ensureEqual(folder.nestedFiles(new PomFileMatcher()).size(), 7);
    }
}
