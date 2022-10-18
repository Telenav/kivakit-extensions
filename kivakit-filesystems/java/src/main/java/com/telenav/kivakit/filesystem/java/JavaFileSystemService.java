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

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of the {@link FileSystemService} SPI to provide java.nio filesystem access through the KIVAKIT
 * resource API. This service accepts {@link FilePath}s that supported by default local filesystem and it provides
 * implementations of {@link FileService} and {@link FolderService} which are used by {@link File} and {@link Folder} to
 * provide transparent access to file system
 *
 * @author yinyinz
 * @see JavaFile
 * @see JavaFolder
 * @see Resource
 * @see File
 * @see Folder
 */
public class JavaFileSystemService implements
        FileSystemService,
        ComponentMixin
{
    public static final String SCHEME = "java:";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(FilePath path)
    {
        // If the path starts with "java:" then it's a JavaFileSystem path,
        if (path.startsWith(SCHEME))
        {
            try
            {
                // so strip off the "java:" scheme and see if Java recognizes the remainder of the path.
                Paths.get(path.withoutPrefix(SCHEME).asString());
                return true;
            }
            catch (Exception e)
            {
                // The path is invalid.
                problem(e, "Invalid Java filesystem path: $", path);
            }
        }

        return false;
    }

    @Override
    public @NotNull DiskService diskService(@NotNull FilePath path)
    {
        return unsupported();
    }

    @Override
    public @NotNull FileService fileService(FilePath path)
    {
        return new JavaFile(path.withoutPrefix(SCHEME));
    }

    @Override
    public @NotNull FolderService folderService(FilePath path)
    {
        return new JavaFolder(path.withoutPrefix(SCHEME));
    }
}
