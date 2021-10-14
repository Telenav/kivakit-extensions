open module kivakit.data.compression
{
    // KivaKit
    requires transitive kivakit.resource;
    requires kivakit.primitive.collections;

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
