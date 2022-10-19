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

package com.telenav.kivakit.filesystems.github;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.FileName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FolderService} used to provide {@link GitHubFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
public class GitHubFolder extends GitHubFileSystemObject implements FolderService
{
    public GitHubFolder(FilePath path)
    {
        super(path);
    }

    public GitHubFolder(String path)
    {
        this(FilePath.parseFilePath(Listener.consoleListener(), path));
    }

    @Override
    public Time createdAt()
    {
        return unsupported();
    }

    @Override
    public boolean exists()
    {
        return super.exists() || !isEmpty();
    }

    @Override
    public GitHubFile file(FileName fileName)
    {
        return new GitHubFile(path().withChild(fileName.name()));
    }

    @Override
    public List<FileService> files()
    {
        var files = new ArrayList<FileService>();
        for (var entry : tree().entries(relativePath().asUnixString(), GitHubTree.EntryType.FILE, false))
        {
            files.add(new GitHubFile(root() + "/" + entry.getPath()));
        }
        return files;
    }

    @Override
    public GitHubFolder folder(@NotNull FileName name)
    {
        return new GitHubFolder(FilePath.parseFilePath(this, child(name).toString()));
    }

    @Override
    public GitHubFolder folder(@NotNull Folder folder)
    {
        return new GitHubFolder(FilePath.parseFilePath(this, child(folder).toString()));
    }

    @Override
    public List<FolderService> folders()
    {
        var folders = new ArrayList<FolderService>();
        for (var entry : tree().entries(relativePath().asUnixString(), GitHubTree.EntryType.FOLDER, false))
        {
            folders.add(new GitHubFolder(root() + "/" + entry.getPath()));
        }
        return folders;
    }

    @Override
    public boolean isEmpty()
    {
        return FolderService.super.isEmpty();
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public Boolean isWritable()
    {
        return false;
    }

    @Override
    public List<FileService> nestedFiles(@NotNull Matcher<FilePath> matcher)
    {
        var files = new ArrayList<FileService>();
        for (var entry : tree().entries(relativePath().asUnixString(), GitHubTree.EntryType.FILE, true))
        {
            files.add(new GitHubFile(root() + "/" + entry.getPath()));
        }
        return files;
    }

    @Override
    public List<FolderService> nestedFolders(@NotNull Matcher<FilePath> matcher)
    {
        var folders = new ArrayList<FolderService>();
        for (var entry : tree().entries(relativePath().asUnixString(), GitHubTree.EntryType.FOLDER, true))
        {
            folders.add(new GitHubFolder(root() + "/" + entry.getPath()));
        }
        return folders;
    }

    @Override
    public GitHubFolder root()
    {
        return (GitHubFolder) super.root();
    }

    @Override
    public Bytes sizeInBytes()
    {
        return unsupported();
    }

    private FilePath child(FileName child)
    {
        return path().withChild(child.name());
    }

    private FilePath child(Folder folder)
    {
        return path().withChild(folder.toString());
    }
}
