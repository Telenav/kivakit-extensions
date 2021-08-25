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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FolderService} used to provide {@link GitHubFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class GitHubFolder extends GitHubFileSystemObject implements FolderService
{
    public GitHubFolder(final FilePath path)
    {
        super(path);
    }

    public GitHubFolder(final String path)
    {
        this(FilePath.parseFilePath(path));
    }

    @Override
    public boolean exists()
    {
        return super.exists() || !isEmpty();
    }

    @Override
    public GitHubFile file(final FileName fileName)
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
    public GitHubFolder folder(final FileName name)
    {
        return new GitHubFolder(FilePath.parseFilePath(child(name).toString()));
    }

    @Override
    public GitHubFolder folder(final Folder folder)
    {
        return new GitHubFolder(FilePath.parseFilePath(child(folder).toString()));
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
    public Boolean isWritable()
    {
        return false;
    }

    @Override
    public List<FileService> nestedFiles(final Matcher<FilePath> matcher)
    {
        var files = new ArrayList<FileService>();
        for (var entry : tree().entries(relativePath().asUnixString(), GitHubTree.EntryType.FILE, true))
        {
            files.add(new GitHubFile(root() + "/" + entry.getPath()));
        }
        return files;
    }

    @Override
    public List<FolderService> nestedFolders(final Matcher<FilePath> matcher)
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

    private FilePath child(final FileName child)
    {
        return path().withChild(child.name());
    }

    private FilePath child(final Folder folder)
    {
        return path().withChild(folder.toString());
    }
}
