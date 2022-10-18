import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.github.GitHubFileSystemService;

open module kivakit.filesystems.github
{
    provides FileSystemService with GitHubFileSystemService;

    // KivaKit
    requires kivakit.component;

    // GitHub API
    requires github.api;

    // Module exports
    exports com.telenav.kivakit.filesystems.github;
}
