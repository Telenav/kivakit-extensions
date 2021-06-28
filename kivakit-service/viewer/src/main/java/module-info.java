open module kivakit.service.viewer
{
    requires transitive kivakit.application;
    requires transitive kivakit.network.http;
    requires transitive kivakit.service.registry;
    requires transitive kivakit.service.client;

    exports com.telenav.kivakit.service.viewer;
}
