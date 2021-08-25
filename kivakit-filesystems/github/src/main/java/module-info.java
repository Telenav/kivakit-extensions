import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.github.GitHubFileSystemService;

open module kivakit.filesystems.s3fs
{
    provides FileSystemService with GitHubFileSystemService;

    // KivaKit
    requires transitive kivakit.component;

    // GitHub API
    requires github.api;

    // Module exports
    exports com.telenav.kivakit.filesystems.github;
    exports com.telenav.kivakit.filesystems.github.project;
}
