package com.telenav.kivakit.filesystems.java;

import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import com.telenav.kivakit.resource.resources.StringResource;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.filesystem.File.temporaryFile;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;
import static com.telenav.kivakit.resource.Extension.TXT;
import static com.telenav.kivakit.resource.Extension.ZIP;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.WRITE;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

public class JavaFolderTest extends UnitTest
{
    public static class TxtFileMatcher implements Matcher<FilePath>
    {
        @Override
        public boolean matches(FilePath path)
        {
            return path.hasExtension(TXT);
        }
    }

    @Test
    public void test()
    {
        var archive = archive();
        var path = format("jar:file:$/$", archive, "child");
        var folder = new JavaFolder(parseFilePath(this, path));

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
        var folder = listenTo(parseFolder(this, format("java:jar:file:$", archive)));
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
        var folder = listenTo(parseFolder(this, format("java:jar:file:$/child", archive)));
        ensureEqual(folder.files().size(), 2);
        var files = new HashSet<>();
        files.add(folder.files().get(0).fileName().name());
        files.add(folder.files().get(1).fileName().name());
        ensureEqual(files, Set.of("a.txt", "b.txt"));
    }

    private ZipArchive archive()
    {
        var zip = temporaryFile(ZIP);
        var archive = zipArchive(this, zip, WRITE);
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
