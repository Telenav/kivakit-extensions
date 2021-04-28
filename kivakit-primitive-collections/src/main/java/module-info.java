open module kivakit.collections.primitive
{
    requires transitive kivakit.core.serialization.kryo;
    requires transitive kivakit.core.test;
    requires transitive kivakit.core.collections;

    exports com.telenav.kivakit.collections.primitive;
    exports com.telenav.kivakit.collections.primitive.array;
    exports com.telenav.kivakit.collections.primitive.array.arrays;
    exports com.telenav.kivakit.collections.primitive.array.bits.io.input;
    exports com.telenav.kivakit.collections.primitive.array.bits.io.output;
    exports com.telenav.kivakit.collections.primitive.array.bits.io;
    exports com.telenav.kivakit.collections.primitive.array.bits;
    exports com.telenav.kivakit.collections.primitive.array.packed;
    exports com.telenav.kivakit.collections.primitive.array.scalars;
    exports com.telenav.kivakit.collections.primitive.array.strings;
    exports com.telenav.kivakit.collections.primitive.iteration;
    exports com.telenav.kivakit.collections.primitive.list;
    exports com.telenav.kivakit.collections.primitive.list.store;
    exports com.telenav.kivakit.collections.primitive.map;
    exports com.telenav.kivakit.collections.primitive.map.multi.dynamic;
    exports com.telenav.kivakit.collections.primitive.map.multi.fixed;
    exports com.telenav.kivakit.collections.primitive.map.objects;
    exports com.telenav.kivakit.collections.primitive.map.scalars;
    exports com.telenav.kivakit.collections.primitive.map.split;
    exports com.telenav.kivakit.collections.primitive.set;
    exports com.telenav.kivakit.collections.project;
    exports com.telenav.kivakit.collections.primitive.map.multi;
}
