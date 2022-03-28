@SuppressWarnings("JavaRequiresAutoModule")
module kivakit.merged.prometheus
{
    exports com.telenav.kivakit.merged.prometheus;

    requires transitive simpleclient.hotspot;
    requires transitive simpleclient.servlet;
}