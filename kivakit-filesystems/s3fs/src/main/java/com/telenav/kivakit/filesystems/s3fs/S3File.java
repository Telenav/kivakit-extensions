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

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystems.s3fs.lexakai.DiagramS3;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link S3FileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramS3.class)
@LexakaiJavadoc(complete = true)
public class S3File extends S3FileSystemObject implements FileService
{
    public S3File(FilePath path)
    {
        super(path, false);
    }

    public S3File(String path)
    {
        super(FilePath.parseFilePath(Listener.console(), path), false);
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
        return true;
    }

    @Override
    public InputStream onOpenForReading()
    {
        var request = GetObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        return client().getObject(request);
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return new S3Output(this);
    }

    public boolean renameTo(S3File that)
    {
        if (canRenameTo(that))
        {
            copyTo(that);
            delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean renameTo(FileService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((S3File) that.resolveService());
        }
        return fatal("Cannot rename $ to $ across filesystems", this, that);
    }

    @Override
    public Bytes sizeInBytes()
    {
        return length();
    }

    public void write(String line)
    {
        if (exists())
        {
            delete();
        }
        var printWriter = printWriter();
        printWriter.println(line);
        printWriter.close();
    }
}
