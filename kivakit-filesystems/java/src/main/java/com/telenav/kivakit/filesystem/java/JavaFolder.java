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

package com.telenav.kivakit.filesystem.java;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;

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
        final List<FileService> files = new ArrayList<>();
        try
        {
            var root = path().asJavaPath();
            var paths = Files.newDirectoryStream(root);
            for (Path path : paths)
            {
                if (!Files.isDirectory(path))
                {
                    files.add(new JavaFile(path.toString()));
                }
            }
        }
        catch (final Exception e)
        {
            problem(e, "Unable to list files in: $", path());
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
        var folderPath = path().withChild(folder.name());
        return new JavaFolder(folderPath);
    }

    @Override
    public Iterable<FolderService> folders()
    {
        final List<FolderService> folders = new ArrayList<>();
        try
        {
            var paths = Files.newDirectoryStream(toJavaPath());
            for (Path path : paths)
            {
                if (Files.isDirectory(path))
                {
                    folders.add(new JavaFolder(path));
                }
            }
        }
        catch (final Exception e)
        {
            problem(e, "Unable to list folders in: $", path());
        }

        return folders;
    }

    @Override
    public boolean isEmpty()
    {
        return !folders().iterator().hasNext();
    }

    @Override
    public List<FileService> nestedFiles(Matcher<FilePath> matcher)
    {
        final List<FileService> files = new ArrayList<>();
        try
        {
            Files.walk(toJavaPath())
                    .filter(Files::isRegularFile)
                    .forEach(f ->
                    {
                        var filePath = FilePath.filePath(f);
                        if (matcher.matches(filePath))
                        {
                            files.add(new JavaFile(filePath));
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
    public List<FolderService> nestedFolders(Matcher<FilePath> matcher)
    {
        final List<FolderService> folders = new ArrayList<>();
        try
        {
            Files.walk(toJavaPath())
                    .filter(Files::isDirectory)
                    .forEach(f ->
                    {
                        var filePath = FilePath.filePath(f);
                        if (matcher.matches(filePath))
                        {
                            folders.add(new JavaFolder(filePath));
                        }
                    });
        }
        catch (Exception e)
        {
            problem(e, "Unable to list nested folders in: $", path());
        }

        return folders;
    }
}

