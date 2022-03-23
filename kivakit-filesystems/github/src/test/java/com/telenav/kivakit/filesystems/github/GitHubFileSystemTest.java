package com.telenav.kivakit.filesystems.github;

import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.resource.path.FileName;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Bytes.bytes;

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
        var file = listenTo(new GitHubFile("github://Telenav/lexakai/develop/README.md"));
        ensure(file.asString().contains("lexakai"));
        ensure(file.sizeInBytes().isGreaterThan(bytes(128)));
    }

    @Test
    public void testFiles()
    {
        var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        var files = folder.files();
        ensure(!files.isEmpty());
        ensure(files.contains(folder.file(FileName.parse(this, "README.md"))));
        ensure(files.contains(folder.file(FileName.parse(this, "pom.xml"))));
    }

    @Test
    public void testNestedFiles()
    {
        var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        var files = folder.nestedFiles(Filter.all());
        ensure(!files.isEmpty());
        ensure(files.contains(folder.file(FileName.parse(this, "README.md"))));
        ensure(files.contains(folder.file(FileName.parse(this, "pom.xml"))));
        ensure(files.contains(folder.folder(FileName.parse(this, "setup")).file(FileName.parse(this, "setup.sh"))));
    }

    @Test
    public void testNestedFolders()
    {
        var folder = listenTo(new GitHubFolder("github://Telenav/lexakai/develop"));
        var folders = folder.nestedFolders(Filter.all());
        ensure(!folders.isEmpty());
        ensure(folders.contains(folder.folder(FileName.parse(this, "documentation"))));
        ensure(folders.contains(folder.folder(FileName.parse(this, "legal"))));
        ensure(folders.contains(folder.folder(FileName.parse(this, "src")).folder(FileName.parse(this, "main"))));
    }

    @Test
    public void testPrivateFile()
    {
        var token = "";
        @SuppressWarnings("SpellCheckingInspection")
        var file = listenTo(new GitHubFile("github://jonathanlocke/access-token/" + token + "/borrelia-corpus/master/borrelia-pmids.txt"));
        ensure(file.asString().contains("30909955"));
        ensure(file.sizeInBytes().isGreaterThan(bytes(128)));
    }
}
