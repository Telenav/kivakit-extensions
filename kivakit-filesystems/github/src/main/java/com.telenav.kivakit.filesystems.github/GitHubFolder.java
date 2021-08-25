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

import java.nio.file.attribute.PosixFilePermission;
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
        super(path, true);
    }

    public GitHubFolder(final String path)
    {
        this(FilePath.parseFilePath(path));
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public GitHubFolder clear()
    {
        return unsupported();
    }

    @Override
    public boolean delete()
    {
        return unsupported();
    }

    @Override
    public void ensureExists()
    {
        unsupported();
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
    public List<GitHubFile> files()
    {
        var files = new ArrayList<GitHubFile>();
        for (var entry : tree().entries(path().asUnixString(), GitHubTree.EntryType.FILE, false))
        {
            files.add(new GitHubFile(entry.getPath()));
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
    public List<GitHubFolder> folders()
    {
        var folders = new ArrayList<GitHubFolder>();
        for (var entry : tree().entries(path().asUnixString(), GitHubTree.EntryType.FOLDER, false))
        {
            folders.add(new GitHubFolder(entry.getPath()));
        }
        return folders;
    }

    public boolean hasFiles()
    {
        return exists() && !files().isEmpty();
    }

    public boolean hasSubFolders()
    {
        return exists() && !folders().isEmpty();
    }

    @Override
    public boolean isEmpty()
    {
        return files().isEmpty();
    }

    @Override
    public Boolean isWritable()
    {
        return false;
    }

    @Override
    public GitHubFolder mkdirs()
    {
        return unsupported();
    }

    @Override
    public List<? extends FileService> nestedFiles(final Matcher<FilePath> matcher)
    {
        var files = new ArrayList<GitHubFile>();
        for (var entry : tree().entries(path().asUnixString(), GitHubTree.EntryType.FILE, true))
        {
            files.add(new GitHubFile(entry.getPath()));
        }
        return files;
    }

    @Override
    public List<? extends FolderService> nestedFolders(final Matcher<FilePath> matcher)
    {
        var folders = new ArrayList<GitHubFolder>();
        for (var entry : tree().entries(path().asUnixString(), GitHubTree.EntryType.FOLDER, true))
        {
            folders.add(new GitHubFolder(entry.getPath()));
        }
        return folders;
    }

    @Override
    public boolean renameTo(final FolderService that)
    {
        return unsupported();
    }

    public boolean renameTo(final GitHubFolder that)
    {
        return unsupported();
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

    @Override
    public GitHubFile temporaryFile(final FileName baseName)
    {
        return unsupported();
    }

    @Override
    public GitHubFolder temporaryFolder(final FileName baseName)
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
