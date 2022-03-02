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

import com.telenav.kivakit.core.language.patterns.group.Group;
import com.telenav.kivakit.core.language.progress.ProgressReporter;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.DiagramS3;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
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

import static com.telenav.kivakit.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.ensure.Ensure.unsupported;

/**
 * Base functionality common to both {@link S3File} and {@link S3Folder}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramS3.class)
@LexakaiJavadoc(complete = true)
public abstract class S3FileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    protected static final Logger LOGGER = LoggerFactory.newLogger();

    // A bucket name in the path
    private static final Pattern BUCKET_NAME = Pattern.expression("[\\w-\\d\\.]").oneOrMore();

    // A region name in the path
    private static final Pattern REGION_NAME = Pattern.expression("[a-z0-9-]").oneOrMore();

    // A key name in the path
    private static final Pattern KEY_NAME = Pattern.ANYTHING;

    private static final Group<String> regionGroup = REGION_NAME.group(Listener.none());

    private static final Group<String> bucketGroup = BUCKET_NAME.group(Listener.none());

    private static final Group<String> keyGroup = KEY_NAME.group(Listener.none());

    private static Pattern schemePattern;

    private static final Group<String> schemeGroup = schemePattern().group(Listener.none());

    // s3://${region}/${bucket}/${key}
    private static final Pattern pattern = schemeGroup
            .then(Pattern.constant("://"))
            .then(regionGroup)
            .then(Pattern.SLASH)
            .then(bucketGroup)
            .then(Pattern.SLASH.optional())
            .then(keyGroup.optional());

    // S3 client
    protected static final Map<String, S3Client> clientForRegion = new ConcurrentHashMap<>();

    public static boolean accepts(String path)
    {
        return schemePattern()
                .then(Pattern.ANYTHING)
                .matches(path);
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

    // The scheme of path, such as "s3://"
    private final String scheme;

    // Name of s3 bucket
    private final String bucket;

    // Name of s3 object key
    private final String key;

    // Meta data attached to the object
    private Map<String, String> metadata;

    // True if it's a folder
    private final boolean isFolder;

    // The AWS region for this object
    private Region region;

    S3FileSystemObject(FilePath path, boolean isFolder)
    {
        super(normalize(path));

        this.isFolder = isFolder;

        var matcher = pattern.matcher(path().toString());
        if (matcher.matches())
        {
            var regionName = regionGroup.get(matcher);
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
            scheme = schemeGroup.get(matcher);
            bucket = bucketGroup.get(matcher);
            key = isFolder ? keyGroup.get(matcher, "") + "/" : keyGroup.get(matcher, "");
        }
        else
        {
            throw new IllegalArgumentException("Bad S3 path: " + path);
        }
    }

    @Override
    public void copyFrom(Resource resource, CopyMode mode, ProgressReporter reporter)
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
        if (o instanceof S3FileSystemObject)
        {
            var that = (S3FileSystemObject) o;
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
    public S3Folder parent()
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
                LOGGER.problem(ex, "failed to create aws endpoint URI: $", endpoint);
            }
        }
        return endpointURI;
    }

    private static FilePath normalize(FilePath path)
    {
        return path;
    }

    // The S3 path schema
    private static Pattern schemePattern()
    {
        if (schemePattern == null)
        {
            schemePattern = Pattern.expression("s3")
                    .then(Pattern.expression("[na]").optional());
        }
        return schemePattern;
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
            LOGGER.warning("Can't rename " + this + " to " + to);
            canRename = false;
        }
        else if (equals(to))
        {
            LOGGER.warning("Can't rename to the same Aws S3 object as " + to);
            canRename = false;
        }
        else if (to != null && !inSameBucket(to))
        {
            LOGGER.warning("Can't move S3 object from ${debug} to another bucket ${debug} ", bucket(), to.bucket());
            canRename = false;
        }
        return canRename;
    }

    S3Client client()
    {
        return clientFor(this.region);
    }

    void copyTo(S3FileSystemObject that)
    {
        var request = CopyObjectRequest.builder()
                .sourceBucket(bucket())
                .sourceKey(key())
                .destinationBucket(that.bucket())
                .destinationKey(key())
                .build();

        client().copyObject(request);
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
        catch (Exception ex)
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
