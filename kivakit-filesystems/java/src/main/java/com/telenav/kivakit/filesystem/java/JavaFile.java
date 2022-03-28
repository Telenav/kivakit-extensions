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

import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.FilePath;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link JavaFileSystemService}.
 *
 * @author yinyinz
 */
public class JavaFile extends JavaFileSystemObject implements
        FileService,
        TryTrait
{
    public JavaFile(FilePath path)
    {
        super(path);
    }

    @Override
    public InputStream onOpenForReading()
    {
        return tryCatch(() -> Files.newInputStream(javaPath()), "Could not open for reading: $", path());
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return tryCatch(() -> Files.newOutputStream(javaPath()), "Could not open for writing: $", path());
    }

    @Override
    public boolean renameTo(FileService that)
    {
        return tryCatch(() ->
        {
            Files.move(javaPath(), that.path().asJavaPath());
            return true;
        }, "Could not move from: $, to: $", path(), that.path());
    }

    @Override
    public Bytes sizeInBytes()
    {
        return tryCatchDefault(() -> Bytes.bytes(Files.size(javaPath())), Bytes._0);
    }
}
