package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.core.language.progress.ProgressReporter;
import com.telenav.kivakit.language.count.Bytes;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.core.test.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class JavaFileTest extends UnitTest
{
    @Test
    public void test()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = Message.format("jar:file:$/$", archive, "test-integration.txt");
        var file = new JavaFile(FilePath.parseFilePath(this, path));

        ensure(file.exists());
        ensure(file.sizeInBytes().isGreaterThan(Bytes._0));
        ensureEqual(file.reader().asString(), "output");
    }

    @Test
    public void testIntegration()
    {
        var archive = archive(file("test-integration.txt", "output"));
        var path = Message.format("java:jar:file:$/$", archive, file("test-integration.txt", "output").fileName().name());
        ensureEqual(listenTo(File.parse(this, path)).reader().asString(), "output");
    }

    @NotNull
    private ZipArchive archive(File file)
    {
        var zip = File.temporary(Extension.ZIP);
        var archive = ZipArchive.open(this, zip, ProgressReporter.none(), ZipArchive.Mode.WRITE);
        archive.save(file.fileName().name(), file);
        archive.close();
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
