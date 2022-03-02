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

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of the {@link FileSystemService} SPI to provide GitHub access through the KIVAKIT resource API. This
 * service accepts {@link FilePath}s that begin with "github:" and it provides implementations of {@link FileService}
 * and {@link FolderService} which are used by {@link File} and {@link Folder} to provide transparent access to file
 * system services.
 *
 * @author jonathanl (shibo)
 * @see GitHubFile
 * @see GitHubFolder
 * @see Resource
 * @see File
 * @see Folder
 */
@LexakaiJavadoc(complete = true)
public class GitHubFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(FilePath path)
    {
        return GitHubFileSystemObject.accepts(path.toString());
    }

    @Override
    public DiskService diskService(FilePath path)
    {
        return unsupported();
    }

    @Override
    @UmlRelation(label = "provides")
    public GitHubFile fileService(FilePath path)
    {
        return new GitHubFile(path);
    }

    @Override
    @UmlRelation(label = "provides")
    public GitHubFolder folderService(FilePath path)
    {
        return new GitHubFolder(path);
    }
}
