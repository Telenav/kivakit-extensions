@SuppressWarnings("JavaRequiresAutoModule")
module kivakit.merged.protostuff
{
     exports com.telenav.kivakit.merged.protostuff;

    requires transitive protostuff.core;
    requires transitive protostuff.runtime;
}
