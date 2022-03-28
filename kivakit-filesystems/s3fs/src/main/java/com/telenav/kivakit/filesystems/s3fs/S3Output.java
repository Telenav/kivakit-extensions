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
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Folder.Type;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;

/**
 * OutputStream that appends to an existing S3 object
 *
 * @author songg
 */
@LexakaiJavadoc(complete = true)
public class S3Output extends OutputStream
{
    // The temporary cache folder for storing materialized files
    private static final Folder cacheFolder = Folder.temporaryForProcess(Type.CLEAN_UP_ON_EXIT).ensureExists();

    /** The {@link S3FileSystemObject} this stream writes to */
    private final S3FileSystemObject object;

    // The underline output stream
    private final OutputStream outputStream;

    // The cache file
    private final File cacheFile;

    /**
     * Create an {@link OutputStream} writing to an {@link S3FileSystemObject}
     *
     * @param object The {@link S3FileSystemObject} to which this stream is writing
     */
    protected S3Output(S3FileSystemObject object)
    {
        this.object = object;
        cacheFile = cacheFile(object.path());
        outputStream = cacheFile.onOpenForWriting();
    }

    /** Close this stream and release the lease */
    @Override
    public void close() throws IOException
    {
        outputStream.close();
        if (object.exists())
        {
            object.delete();
        }
        object.copyFrom(cacheFile, OVERWRITE, ProgressReporter.none());
    }

    @Override
    public void flush()
    {
        try
        {
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void write(byte @NotNull [] bytes, int offset, int length) throws IOException
    {
        outputStream.write(bytes, offset, length);
    }

    @Override
    public void write(int b) throws IOException
    {
        outputStream.write(b);
    }

    private File cacheFile(FilePath filePath)
    {
        // Flatten path being cached into a long filename by turning all file
        // system meta characters into underscores.
        // For example, "a/b/c.txt" becomes "a_b_c.txt"
        return File.parseFile(Listener.console(), cacheFolder + "/" + filePath.toString().replaceAll("[/:]", "_"));
    }
}
