package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.filesystem.File.temporaryFile;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.filesystem.Folders.kivakitTemporaryFolder;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.WRITE;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

public class JavaFileTest extends UnitTest
{
    @Test
    public void test()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = format("jar:file:$/$", archive, "test-integration.txt");
        var file = new JavaFile(parseFilePath(this, path));

        ensure(file.exists());
        ensure(file.sizeInBytes().isGreaterThan(Bytes._0));
        ensureEqual(file.reader().asString(), "output");
    }

    @Test
    public void testIntegration()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = format("java:jar:file:$/$", archive, file("test-integration.txt", "output").fileName().name());
        ensureEqual(listenTo(File.parseFile(this, path)).reader().asString(), "output");
    }

    private ZipArchive archive(File file)
    {
        var zip = temporaryFile(Extension.ZIP);
        var archive = zipArchive(this, zip, WRITE);
        if (archive != null)
        {
            archive.save(file.fileName().name(), file);
            archive.close();
        }
        return archive;
    }

    @SuppressWarnings("SameParameterValue")
    @NotNull
    private File file(String name, String output)
    {
        var file = listenTo(kivakitTemporaryFolder()
                .folder("java-file-test")
                .mkdirs()
                .file(name));
        file.writer().saveText(output);
        return file;
    }
}
