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

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link GitHubFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class GitHubFile extends GitHubFileSystemObject implements FileService
{
    public GitHubFile(FilePath path)
    {
        super(path);
    }

    public GitHubFile(String path)
    {
        super(FilePath.parseFilePath(path));
    }

    @Override
    public boolean chmod(PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public Time created()
    {
        return unsupported();
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
    public InputStream onOpenForReading()
    {
        try
        {
            return entry().readAsBlob();
        }
        catch (Exception e)
        {
            problem(e, "Unable to open for reading: $", entry().getPath());
            return null;
        }
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return unsupported();
    }

    public boolean renameTo(GitHubFile that)
    {
        return unsupported();
    }

    @Override
    public boolean renameTo(FileService that)
    {
        return unsupported();
    }

    @Override
    public Bytes sizeInBytes()
    {
        return length();
    }

    @Override
    Bytes length()
    {
        return Bytes.bytes(entry().getSize());
    }
}
