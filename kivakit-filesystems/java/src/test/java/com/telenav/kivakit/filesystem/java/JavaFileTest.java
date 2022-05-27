package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.testing.UnitTest;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.filesystem.FilePath;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class JavaFileTest extends UnitTest
{
    @Test
    public void test()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = Strings.format("jar:file:$/$", archive, "test-integration.txt");
        var file = new JavaFile(FilePath.parseFilePath(this, path));

        ensure(file.exists());
        ensure(file.sizeInBytes().isGreaterThan(Bytes._0));
        ensureEqual(file.reader().asString(), "output");
    }

    @Test
    public void testIntegration()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = Strings.format("java:jar:file:$/$", archive, file("test-integration.txt", "output").fileName().name());
        ensureEqual(listenTo(File.parseFile(this, path)).reader().asString(), "output");
    }

    private ZipArchive archive(File file)
    {
        var zip = File.temporary(Extension.ZIP);
        var archive = ZipArchive.open(this, zip, ZipArchive.Mode.WRITE);
        if (archive != null)
        {
            archive.save(file.fileName().name(), file);
            archive.close();
        }
        return archive;
    }

    @NotNull
    private File file(String name, String output)
    {
        return listenTo(Folder.kivakitTemporary()
                .folder("java-file-test")
                .mkdirs()
                .file(name)
                .print(output));
    }
}
