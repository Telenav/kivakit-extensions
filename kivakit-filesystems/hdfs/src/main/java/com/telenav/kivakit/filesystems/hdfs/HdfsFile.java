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

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy;
import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.language.threading.Retry;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link HdfsFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfs.class)
@UmlRelation(label = "gets proxy from", referent = HdfsProxyClient.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class HdfsFile extends BaseWritableResource implements FileService
{
    private final FilePath path;

    private Bytes size;

    @UmlAggregation
    private HdfsProxy proxy;

    public HdfsFile(FilePath path)
    {
        this.path = path;
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
    public boolean delete()
    {
        return retry(() -> proxy().deleteFile(pathAsString()))
                .orDefault(this, false, "Unable to delete $", this);
    }

    @Override
    public boolean exists()
    {
        return retry(() -> proxy().exists(pathAsString()))
                .orDefault(this, false, "Unable to determine if $ exists", this);
    }

    @Override
    public boolean isFolder()
    {
        return retry(() -> proxy().isFolder(pathAsString()))
                .orDefault(this, false, "Unable to determine if $ is a folder", this);
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public Boolean isWritable()
    {
        return retry(() -> proxy().isWritable(pathAsString())).orDefault(this, false, "Unable to determine if $ is writable", this);
    }

    @Override
    public Time lastModified()
    {
        return retry(() -> Time.milliseconds(proxy().lastModified(pathAsString())))
                .orDefault(this, null, "Unable to get last modified time of $", this);
    }

    @Override
    public boolean lastModified(Time modified)
    {
        return retry(() -> proxy().lastModified(pathAsString(), modified.asMilliseconds()))
                .orDefault(this, false, "Unable to set last modified time of $ to $", this, modified);
    }

    @Override
    public InputStream onOpenForReading()
    {
        return retry(() -> HdfsProxyIO.in(proxy().openForReading(pathAsString())))
                .orDefault(this, null, "Unable to open $ for reading", this);
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return retry(() -> HdfsProxyIO.out(proxy().openForWriting(pathAsString())))
                .orDefault(this, null, "Unable to open $ for writing", this);
    }

    @Override
    public HdfsFolder parent()
    {
        var parent = path().parent();
        if (parent != null)
        {
            return new HdfsFolder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return path;
    }

    @Override
    public boolean renameTo(FileService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((HdfsFile) that.resolveService());
        }
        return fatal("Cannot rename $ to $ across filesystems", this, that);
    }

    public boolean renameTo(HdfsFile to)
    {
        return retry(() -> proxy().rename(pathAsString(), to.path().toString()))
                .orDefault(this, false, "Unable to rename $ to $", this, to);
    }

    @Override
    public HdfsFolder root()
    {
        return new HdfsFolder(path.root());
    }

    @Override
    public Bytes sizeInBytes()
    {
        if (size == null)
        {
            size = retry(() ->
            {
                var length = proxy().length(pathAsString());
                return length < 0 ? null : Bytes.bytes(length);
            }).orDefault(this, Bytes._0, "Unable to get size of $", this);
        }
        return size;
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    private String pathAsString()
    {
        return path().toString();
    }

    private HdfsProxy proxy()
    {
        if (proxy == null)
        {
            proxy = HdfsProxyClient.get().proxy();
        }
        return proxy;
    }

    private <T> Unchecked<T> retry(Unchecked<T> code)
    {
        return Retry.retry(code, 16, Duration.seconds(15), () -> proxy = null);
    }
}
