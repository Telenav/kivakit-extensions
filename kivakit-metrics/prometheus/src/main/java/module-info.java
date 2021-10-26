open module kivakit.metrics.prometheus
{
    // KivaKit
    requires transitive kivakit.metrics.core;
    requires kivakit.prometheus.merged;

    exports com.telenav.kivakit.metrics.prometheus;
}
