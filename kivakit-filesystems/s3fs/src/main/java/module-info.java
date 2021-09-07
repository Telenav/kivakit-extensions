import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.s3fs.S3FileSystemService;

open module kivakit.filesystems.s3fs
{
    provides FileSystemService with S3FileSystemService;

    // KivaKit
    requires transitive kivakit.configuration;

    // S3
    requires transitive software.amazon.awssdk.services.s3;
    requires transitive software.amazon.awssdk.core;
    requires transitive software.amazon.awssdk.regions;
    requires transitive software.amazon.awssdk.auth;

    // Module exports
    exports com.telenav.kivakit.filesystems.s3fs;
    exports com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams;
}
