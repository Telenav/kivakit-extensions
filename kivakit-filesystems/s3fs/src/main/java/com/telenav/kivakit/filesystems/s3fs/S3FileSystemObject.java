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

import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.filesystems.s3fs.internal.lexakai.DiagramS3;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.Patterns.patternMatches;

/**
 * Base functionality common to both {@link S3File} and {@link S3Folder}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramS3.class)
public abstract class S3FileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    /** s3://${region}/${bucket}/${key} */
    private static final Pattern PATTERN = Pattern.compile("(?<scheme>s3)://(?<region>[A-Za-z0-9-]+)/(?<bucket>[^/]+)/(?<key>.*)");

    /** S3 client */
    protected static final Map<String, S3Client> clientForRegion = new ConcurrentHashMap<>();

    public static boolean accepts(String path)
    {
        return patternMatches(PATTERN, path);
    }

    protected static S3Client clientFor(Region region)
    {
        return clientForRegion.computeIfAbsent(region.id(), key -> buildClient(region));
    }

    protected static ListObjectsRequest listRequest(String bucketName, String keyName)
    {
        return ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(keyName + "/")
                .delimiter("/")
                .build();
    }

    protected static FilePath path(Listener listener, String scheme, Region region, String bucketName, String keyName)
    {
        return FilePath.parseFilePath(listener, scheme + "://" + (region != null ? region.id() : "default-region") + "/" + bucketName + "/" + keyName);
    }

    // The scheme of path, such as "s3"
    private final String scheme;

    // Name of s3 bucket
    private final String bucket;

    // Name of s3 object key
    private final String key;

    // Metadata attached to the object
    private Map<String, String> metadata;

    // True if it's a folder
    private final boolean isFolder;

    // The AWS region for this object
    private Region region;

    S3FileSystemObject(FilePath path, boolean isFolder)
    {
        super(normalize(path));

        this.isFolder = isFolder;

        var matcher = PATTERN.matcher(path().toString());
        if (matcher.matches())
        {
            var regionName = matcher.group("region");
            for (var at : Region.regions())
            {
                if (at.id().equals(regionName))
                {
                    this.region = at;
                }
            }
            if ("default-region".equals(regionName))
            {
                this.region = null;
            }
            else
            {
                ensureNotNull(this.region, "Region '$' is not recognized", regionName);
            }
            scheme = matcher.group("scheme");
            bucket = matcher.group("bucket");
            key = isFolder ? matcher.group("key") + "/" : matcher.group("key");
        }
        else
        {
            throw new IllegalArgumentException("Bad S3 path: " + path);
        }
    }

    @Override
    public void copyFrom(Resource resource, @NotNull CopyMode mode, @NotNull ProgressReporter reporter)
    {
        var in = resource.openForReading();

        var request = PutObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        client().putObject(request, RequestBody.fromInputStream(in, resource.sizeInBytes().asBytes()));
    }

    @Override
    public boolean delete()
    {
        var request = DeleteObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        client().deleteObject(request);
        return !exists();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof S3FileSystemObject that)
        {
            return inSameBucket(that) && withIdenticalKey(that);
        }
        return false;
    }

    @Override
    public boolean exists()
    {
        return metadata() != null;
    }

    @Override
    public boolean isFolder()
    {
        return isFolder;
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public abstract Boolean isWritable();

    public String name()
    {
        return path().fileName().name();
    }

    @Override
    public InputStream onOpenForReading()
    {
        return unsupported();
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return unsupported();
    }

    @Override
    public S3Folder parentService()
    {
        var parent = path().parent();
        if (parent != null)
        {
            return new S3Folder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return FilePath.parseFilePath(this, super.path().toString());
    }

    @Override
    public FolderService root()
    {
        return new S3Folder(path(this, scheme(), region(), bucket(), ""));
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    private static S3Client buildClient(Region region)
    {
        var builder = S3Client.builder().region(region);

        var endpoint = endpoint();
        if (endpoint != null)
        {
            builder.endpointOverride(endpoint);
        }
        return builder.build();
    }

    private static URI endpoint()
    {
        URI endpointURI = null;
        var endpoint = System.getProperty("aws-endpoint");
        if (endpoint != null)
        {
            try
            {
                endpointURI = URI.create(endpoint);
            }
            catch (Exception ex)
            {
                Ensure.illegalState(ex, "failed to create aws endpoint URI: $", endpoint);
            }
        }
        return endpointURI;
    }

    private static FilePath normalize(FilePath path)
    {
        return path;
    }

    final String bucket()
    {
        return bucket;
    }

    boolean canRenameTo(S3FileSystemObject to)
    {
        var canRename = true;
        if (to != null && to.exists())
        {
            warning("Can't rename " + this + " to " + to);
            canRename = false;
        }
        else if (equals(to))
        {
            warning("Can't rename to the same Aws S3 object as " + to);
            canRename = false;
        }
        else if (to != null && !inSameBucket(to))
        {
            warning("Can't move S3 object from ${debug} to another bucket ${debug} ", bucket(), to.bucket());
            canRename = false;
        }
        return canRename;
    }

    S3Client client()
    {
        return clientFor(this.region);
    }

    boolean copyTo(S3FileSystemObject that)
    {
        var request = CopyObjectRequest.builder()
                .sourceBucket(bucket())
                .sourceKey(key())

                .destinationBucket(that.bucket())
                .destinationKey(key())
                .build();

        try (var client = client())
        {
            client .copyObject(request);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    boolean inSameBucket(S3FileSystemObject that)
    {
        return bucket().equals(that.bucket());
    }

    final String key()
    {
        return key;
    }

    Bytes length()
    {
        var object = object();
        return Bytes.bytes(object != null ? object.contentLength() : 0);
    }

    Map<String, String> metadata()
    {
        if (metadata == null)
        {
            var object = object();
            if (object != null)
            {
                metadata = object.metadata();
            }
        }
        return metadata;
    }

    GetObjectResponse object()
    {
        var request = GetObjectRequest.builder().bucket(bucket()).key(key()).build();

        GetObjectResponse response = null;
        try (var inputStream = client().getObject(request))
        {
            response = inputStream.response();
        }
        catch (Exception ignored)
        {
        }

        return response;
    }

    Region region()
    {
        return region;
    }

    String scheme()
    {
        return scheme;
    }

    boolean withIdenticalKey(S3FileSystemObject that)
    {
        return key().equals(that.key());
    }
}
