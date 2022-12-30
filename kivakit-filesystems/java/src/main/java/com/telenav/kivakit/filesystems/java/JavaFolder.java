////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.java;

import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FolderService} used to provide {@link JavaFileSystemService}.
 *
 * @author yinyinz
 */
public class JavaFolder extends JavaFileSystemObject implements FolderService
{
    public JavaFolder(FilePath path)
    {
        super(path);
    }

    @Override
    public FileService file(FileName name)
    {
        return new JavaFile(path().withChild(name.name()));
    }

    @Override
    public List<FileService> files()
    {
        var files = new ArrayList<FileService>();
        for (var at : Nio.filesAndFolders(this, javaPath()))
        {
            if (!Files.isDirectory(at))
            {
                files.add(file(at));
            }
        }
        return files;
    }

    @Override
    public FolderService folder(Folder folder)
    {
        var folderPath = path().withChild(folder.toString());
        return new JavaFolder(folderPath);
    }

    @Override
    public FolderService folder(FileName folder)
    {
        var folderPath = path().withChild(folder.name());
        return new JavaFolder(folderPath);
    }

    @Override
    public List<FolderService> folders()
    {
        var folders = new ArrayList<FolderService>();
        for (var at : Nio.filesAndFolders(this, javaPath()))
        {
            if (Files.isDirectory(at) || at.toString().equals("/"))
            {
                folders.add(folder(fileName(at)));
            }
        }
        return folders;
    }

    @Override
    public boolean isEmpty()
    {
        return !folders().isEmpty() && !files().isEmpty();
    }

    @Override
    public List<FileService> nestedFiles(@NotNull Matcher<FilePath> matcher)
    {
        var files = new ArrayList<FileService>();
        try (var stream = Files.walk(javaPath()))
        {
            stream.filter(Files::isRegularFile)
                    .forEach(at ->
                    {
                        var filePath = FilePath.filePath(at);
                        if (matcher.matches(filePath))
                        {
                            files.add(file(at));
                        }
                    });
        }
        catch (Exception e)
        {
            problem(e, "Unable to list nested files in: $", path());
        }

        return files;
    }

    @Override
    public List<FolderService> nestedFolders(@NotNull Matcher<FilePath> matcher)
    {
        var folders = new ArrayList<FolderService>();
        try (var stream = Files.walk(javaPath()))
        {
            stream.filter(Files::isDirectory)
                    .forEach(at ->
                    {
                        var filePath = FilePath.filePath(at);
                        if (matcher.matches(filePath))
                        {
                            folders.add(new JavaFolder(filePath));
                        }
                    });
            {

            }
        }
        catch (Exception e)
        {
            problem(e, "Unable to list nested folders in: $", path());
        }

        return folders;
    }

    private FileService file(Path path)
    {
        return file(fileName(path));
    }

    @NotNull
    private FileName fileName(Path at)
    {
        return FileName.parseFileName(this, at.getFileName().toString());
    }
}
