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
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.values.count.Bytes;

import java.nio.file.FileStore;
import java.nio.file.Files;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link DiskService} used to provide {@link JavaFileSystemService}.
 *
 * @author yinyinz
 */
public class JavaDisk implements DiskService
{
    private final JavaFolder root;

    public JavaDisk(final JavaFolder folder)
    {
        this.root = (JavaFolder) folder.root();
    }

    @Override
    public Bytes free()
    {
        try {
            FileStore store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getUnallocatedSpace());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public FolderService root()
    {
        return this.root;
    }

    @Override
    public Bytes size()
    {
        try {
            FileStore store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getTotalSpace());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public Bytes usable()
    {
        try {
            FileStore store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getUsableSpace());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }
}
