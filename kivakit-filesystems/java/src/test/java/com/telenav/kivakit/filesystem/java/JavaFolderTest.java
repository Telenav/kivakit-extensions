package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.resources.StringResource;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class JavaFolderTest extends UnitTest
{
    public static class TxtFileMatcher implements Matcher<FilePath>
    {
        @Override
        public boolean matches(FilePath path)
        {
            return path.hasExtension(Extension.TXT);
        }
    }

    @Test
    public void test()
    {
        var archive = archive();
        var path = Strings.format("jar:file:$/$", archive, "child");
        var folder = new JavaFolder(FilePath.parseFilePath(this, path));

        ensure(folder.isFolder());
        ensureFalse(folder.isEmpty());
        ensureFalse(folder.isFile());

        ensure(folder.exists());
        ensure(folder.hasFiles());
        ensureFalse(folder.hasSubFolders());
        ensure(folder.isWritable());

        ensureEqual(folder.nestedFiles(new TxtFileMatcher()).size(), 2);
    }

    @Test
    public void testIntegration()
    {
        var archive = archive();
        var folder = listenTo(Folder.parse(this, Strings.format("java:jar:file:$", archive)));
        ensureEqual(folder.files().size(), 1);
        ensureEqual(folder.folders().size(), 1);
        var files = new HashSet<>();
        files.add(folder.files().get(0).fileName().name());
        ensureEqual(files, Set.of("c.txt"));
    }

    @Test
    public void testIntegrationChild()
    {
        var archive = archive();
        var folder = listenTo(Folder.parse(this, Strings.format("java:jar:file:$/child", archive)));
        ensureEqual(folder.files().size(), 2);
        var files = new HashSet<>();
        files.add(folder.files().get(0).fileName().name());
        files.add(folder.files().get(1).fileName().name());
        ensureEqual(files, Set.of("a.txt", "b.txt"));
    }

    private ZipArchive archive()
    {
        var zip = File.temporary(Extension.ZIP);
        var archive = ZipArchive.open(this, zip, ZipArchive.Mode.WRITE);
        if (archive != null)
        {
            archive.save("/child/a.txt", new StringResource("a"));
            archive.save("/child/b.txt", new StringResource("b"));
            archive.save("/c.txt", new StringResource("c"));
            archive.close();
        }
        return archive;
    }
}
