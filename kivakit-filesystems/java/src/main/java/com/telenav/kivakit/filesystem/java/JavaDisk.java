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
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.values.count.Bytes;

import java.nio.file.Files;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link DiskService} used to provide {@link JavaFileSystemService}.
 *
 * @author yinyinz
 */
public class JavaDisk implements DiskService, ComponentMixin
{
    private final JavaFolder root;

    public JavaDisk(final JavaFolder folder)
    {
        this.root = (JavaFolder) folder.root();
    }

    @Override
    public Bytes free()
    {
        return tryCatch(() ->
        {
            var store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getUnallocatedSpace());
        }, "Unable to get disk free space: $", root.path());
    }

    @Override
    public FolderService root()
    {
        return this.root;
    }

    @Override
    public Bytes size()
    {
        return tryCatch(() ->
        {
            var store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getTotalSpace());
        }, "Unable to get disk size: $", root.path());
    }

    @Override
    public Bytes usable()
    {
        return tryCatch(() ->
        {
            var store = Files.getFileStore(this.root.toJavaPath());
            return Bytes.bytes(store.getUsableSpace());
        }, "Unable to get disk usable size: $", root.path());
    }
}
