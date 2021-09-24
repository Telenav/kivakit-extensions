package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FilePath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// @yinyin rename "java-filesystem" to "java" so it's like the others (the project is already under "kivakit-filesystems")
// @yinyin add module-info.java under src/main/java
// @yinyin a little comment and add your name as @author
public class JavaFile extends JavaFileSystemObject implements FileService, ComponentMixin
{
    public JavaFile(final FilePath path)
    {
        super(path, false);
    }

    public JavaFile(final String path)
    {
        super(FilePath.parseFilePath(path), false);
    }

    // @yinyin not used
    public JavaFile(final Path path)
    {
        super(path);
    }

    @Override
    public boolean exists()
    {
        return Files.exists(javaPath);
    }

    @Override
    public InputStream onOpenForReading()
    {
        // @yinyin i just added this... this is from ComponentMixin
        return tryCatch(() -> Files.newInputStream(javaPath), "Could not open for reading: $", path());
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        // @yinyin replace this and other try catch blocks with tryCatch or tryCatchThrow methods?
        try
        {
            return Files.newOutputStream(javaPath);
        }
        catch (Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    public boolean renameTo(FileService that)
    {
        try
        {
            Files.move(this.javaPath, that.path().asJavaPath());
            return true;
        }
        catch (IOException e)
        {
            // @yinyin use problem()
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public Bytes sizeInBytes()
    {
        // @yinyin this failure is not recoverable. use tryCatchThrow()
        try
        {
            return Bytes.bytes(Files.size(this.javaPath));
        }
        catch (Exception ex)
        {
            problem("Cannot determine file size: $", javaPath).throwAsIllegalStateException();
            return null;
        }
    }
}
