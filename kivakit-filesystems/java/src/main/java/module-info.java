import com.telenav.kivakit.filesystem.java.JavaFileSystemService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;

open module kivakit.filesystems.java
{
    provides FileSystemService with JavaFileSystemService;

    // KivaKit
    requires transitive kivakit.component;

    requires org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.filesystem.java;
}
