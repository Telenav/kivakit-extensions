package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FilePath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaFile extends JavaFileSystemObject implements FileService{

    public JavaFile(final FilePath path) {
        super(path, false);
    }

    public JavaFile(final String path) {
        super(FilePath.parseFilePath(path), false);
    }

    public JavaFile(final Path path) {
        super(path);
    }

    @Override
    public InputStream onOpenForReading() {
        try {
            return Files.newInputStream(javaPath);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    public OutputStream onOpenForWriting() {
        try {
            return Files.newOutputStream(javaPath);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    public boolean renameTo(FileService that) {

        try {
            Files.move(this.javaPath, that.path().asJavaPath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean exists() {
        return Files.exists(javaPath);
    }

    @Override
    public Bytes sizeInBytes() {
        try {
            return Bytes.bytes(Files.size(this.javaPath));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return Bytes._0;
    }
}
