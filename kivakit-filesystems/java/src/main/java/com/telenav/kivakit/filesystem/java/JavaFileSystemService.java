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

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.path.FilePath;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

// @yinyin a little comment and add your name as @author
public class JavaFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(FilePath path)
    {
        // @yinyin this should do
        return path.startsWith("java:");
    }

    @Override
    public DiskService diskService(FilePath path)
    {
        // @yinyin We should be able to implement this (I think)
        return unsupported();
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
