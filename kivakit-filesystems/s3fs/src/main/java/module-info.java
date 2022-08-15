import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.s3fs.S3FileSystemService;

open module kivakit.filesystems.s3fs
{
    provides FileSystemService with S3FileSystemService;

    // KivaKit
    requires kivakit.resource;

    // S3
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.regions;
    
    // Module exports
    exports com.telenav.kivakit.filesystems.s3fs;
}
