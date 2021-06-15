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

package com.telenav.kivakit.aws.s3;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.filesystem.Folder.Type.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;

/**
 * OutputStream that writes to an existing S3 object
 *
 * @author songg
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class S3OutputStream extends OutputStream
{
    // The temporary cache folder for storing materialized files
    private static final Folder cacheFolder = Folder.temporaryForProcess(CLEAN_UP_ON_EXIT).ensureExists();

    /** The {@link BucketObject} this stream writes to */
    private final BucketObject object;

    // The underline output stream
    private final OutputStream outputStream;

    // The cache file
    private final File cacheFile;

    /**
     * Create an {@link OutputStream} writing to a {@link BucketObject}
     *
     * @param object The {@link BucketObject} to which this stream is writing
     */
    public S3OutputStream(final BucketObject object)
    {
        this.object = object;
        cacheFile = cacheFile(object.path());
        outputStream = cacheFile.openForWriting();
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
        object.copyFrom(cacheFile, OVERWRITE, ProgressReporter.NULL);
    }

    @Override
    public void flush()
    {
        try
        {
            outputStream.flush();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void write(final byte[] bytes, final int offset, final int length) throws IOException
    {
        outputStream.write(bytes, offset, length);
    }

    @Override
    public void write(final int b) throws IOException
    {
        outputStream.write(b);
    }

    private File cacheFile(final ResourcePath filePath)
    {
        // Flatten path being cached into a long filename by turning all file
        // system meta characters into underscores.
        // For example, "a/b/c.txt" becomes "a_b_c.txt"
        return File.parse(cacheFolder + "/" + filePath.toString().replaceAll("[/:]", "_"));
    }
}
