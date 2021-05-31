open module kivakit.service.viewer
{
    requires transitive kivakit.application;
    requires transitive kivakit.network.http;
    requires kivakit.service.registry;
    requires kivakit.service.client;

    exports com.telenav.kivakit.service.viewer;
}
