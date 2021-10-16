package com.telenav.kivakit.filesystems.github;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for GitHub filesystem.
 * <p>
 * NOTE: This test should be turned off except when debugging as it hits GitHub during a build of KivaKit, which occurs
 * frequently.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@Ignore
public class GitHubFileSystemTest extends UnitTest
{
    @Test
    public void testFile()
    {
        final var file = listenTo(new GitHubFile("github://Telenav/lexakai/develop/README.md"));
        ensure(file.string().contains("lexakai"));
        ensure(file.sizeInBytes().isGreaterThan(Bytes._128));
    }

    @Test
    public void testFiles()
    {
        final var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        final var files = folder.files();
        ensure(!files.isEmpty());
        ensure(files.contains(folder.file(FileName.parse("README.md"))));
        ensure(files.contains(folder.file(FileName.parse("pom.xml"))));
    }

    @Test
    public void testNestedFiles()
    {
        final var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        final var files = folder.nestedFiles(new All<>());
        ensure(!files.isEmpty());
        ensure(files.contains(folder.file(FileName.parse("README.md"))));
        ensure(files.contains(folder.file(FileName.parse("pom.xml"))));
        ensure(files.contains(folder.folder(FileName.parse("setup")).file(FileName.parse("setup.sh"))));
    }

    @Test
    public void testNestedFolders()
    {
        final var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        final var folders = folder.nestedFolders(new All<>());
        ensure(!folders.isEmpty());
        ensure(folders.contains(folder.folder(FileName.parse("documentation"))));
        ensure(folders.contains(folder.folder(FileName.parse("legal"))));
        ensure(folders.contains(folder.folder(FileName.parse("src")).folder(FileName.parse("main"))));
    }

    @Test
    public void testPrivateFile()
    {
        final var token = "";
        final var file = listenTo(new GitHubFile("github://jonathanlocke/access-token/" + token + "/borrelia-corpus/master/borrelia-pmids.txt"));
        ensure(file.string().contains("30909955"));
        ensure(file.sizeInBytes().isGreaterThan(Bytes._128));
    }
}
