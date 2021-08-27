import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.hdfs.HdfsFileSystemService;

open module kivakit.filesystems.hdfs
{
    provides FileSystemService with HdfsFileSystemService;

    // KivaKit
    requires transitive kivakit.service.client;
    requires transitive kivakit.filesystems.hdfs.proxy.spi;

    // RMI
    requires transitive java.rmi;

    // Module exports
    exports com.telenav.kivakit.filesystems.hdfs;
    exports com.telenav.kivakit.filesystems.hdfs.project;
    exports com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams;
}
