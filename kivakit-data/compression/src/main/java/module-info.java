open module kivakit.data.compression
{
    // KivaKit
    requires transitive kivakit.resource;
    requires transitive kivakit.primitive.collections;

    requires org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.data.compression.codecs.huffman.character;
    exports com.telenav.kivakit.data.compression.codecs.huffman.list;
    exports com.telenav.kivakit.data.compression.codecs.huffman.string;
    exports com.telenav.kivakit.data.compression.codecs.huffman.tree;
    exports com.telenav.kivakit.data.compression.codecs.huffman;
    exports com.telenav.kivakit.data.compression.codecs;
    exports com.telenav.kivakit.data.compression.project;
    exports com.telenav.kivakit.data.compression;
}
