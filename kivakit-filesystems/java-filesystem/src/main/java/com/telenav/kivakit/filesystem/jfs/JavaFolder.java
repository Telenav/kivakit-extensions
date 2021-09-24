package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// @yinyin a little comment and add your name as @author
public class JavaFolder extends JavaFileSystemObject implements FolderService
{

    public JavaFolder(final FilePath path)
    {
        super(path, true);
    }

    public JavaFolder(final String path)
    {
        super(FilePath.parseFilePath(path), true);
    }

    public JavaFolder(final Path path)
    {
        super(path);
    }

    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter)
    {
        super.copyTo(destination, mode, reporter);
    }

    @Override
    public FileService file(FileName name)
    {
        return new JavaFile(path().withChild(name.name()));
    }

    @Override
    public List<FileService> files()
    {
        // @yinyin use var whenever possible like: var files = new ArrayList<FileService>();
        final List<FileService> files = new ArrayList<>();
        try
        {
            // @yinyin use var whenever possible like: var root = path().asJavaPath();
            Path root = path().asJavaPath();
            DirectoryStream<Path> directory = Files.newDirectoryStream(root);
            for (Path p : directory)
            {
                if (!Files.isDirectory(p))
                {
                    files.add(new JavaFile(p.toString()));
                }
            }
        }
        catch (final Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return files;
    }

    @Override
    public FolderService folder(Folder folder)
    {
        FilePath folderPath = path().withChild(folder.toString());
        return new JavaFolder(folderPath);
    }

    @Override
    public FolderService folder(FileName folder)
    {
        // @yinyin var
        FilePath folderPath = path().withChild(folder.name());
        return new JavaFolder(folderPath);
    }

    @Override
    public Iterable<FolderService> folders()
    {
        final List<FolderService> folders = new ArrayList<>();
        try
        {
            DirectoryStream<Path> directory = Files.newDirectoryStream(javaPath);
            // @yinyin p -> path, directory -> paths
            for (Path p : directory)
            {
                if (Files.isDirectory(p))
                {
                    folders.add(new JavaFolder(p));
                }
            }
        }
        catch (final Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return folders;
    }

    @Override
    public boolean isEmpty()
    {
        // @yinyin return !folders().iterator().hasNext(); and remove the rest of this code
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(javaPath))
        {
            return !directory.iterator().hasNext();
        }
        catch (Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return false;
    }

    @Override
    public List<FileService> nestedFiles(Matcher<FilePath> matcher)
    {
        final List<FileService> files = new ArrayList<>();
        try
        {
            // @yinyin cool
            Files.walk(javaPath)
                    .filter(Files::isRegularFile)
                    .forEach(f ->
                    {
                        FilePath filePath = FilePath.filePath(f);
                        if (matcher.matches(filePath))
                        {
                            files.add(new JavaFile(filePath));
                        }
                    });
        }
        catch (Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return files;
    }

    @Override
    public List<FolderService> nestedFolders(Matcher<FilePath> matcher)
    {
        final List<FolderService> folders = new ArrayList<>();
        try
        {
            Files.walk(javaPath)
                    .filter(Files::isDirectory)
                    .forEach(f ->
                    {
                        FilePath filePath = FilePath.filePath(f);
                        if (matcher.matches(filePath))
                        {
                            folders.add(new JavaFolder(filePath));
                        }
                    });
        }
        catch (Exception ex)
        {
            // @yinyin use problem()
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return folders;
    }
}

