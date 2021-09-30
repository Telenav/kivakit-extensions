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

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.FilePath;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of the {@link FileSystemService} SPI to provide java.nio filesystem access through the KIVAKIT resource API. This
 * service accepts {@link FilePath}s that supported by default local filesystem and it provides implementations of {@link FileService} and
 * {@link FolderService} which are used by {@link File} and {@link Folder} to provide transparent access to file system
 *
 * @author yinyinz
 * @see JavaFile
 * @see JavaFolder
 * @see Resource
 * @see File
 * @see Folder
 */
public class JavaFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(FilePath path)
    {
        try
        {
            Paths.get(path.asString());
        }
        catch (InvalidPathException | NullPointerException ex)
        {
            return false;
        }

        return true;
    }

    @Override
    public DiskService diskService(FilePath path)
    {
        return new JavaDisk(new JavaFolder(path));
    }

    @Override
    public FileService fileService(FilePath path)
    {
        return new JavaFile(path);
    }

    @Override
    public FolderService folderService(FilePath path)
    {
        return new JavaFolder(path);
    }
}
