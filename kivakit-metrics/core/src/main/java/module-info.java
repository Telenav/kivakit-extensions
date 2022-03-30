open module kivakit.metrics.core
{
    // KivaKit
    requires kivakit.interfaces;
    requires kivakit.core;

    // Exports
    exports com.telenav.kivakit.metrics.core;
    exports com.telenav.kivakit.metrics.core.aggregates;
    exports com.telenav.kivakit.metrics.core.reporters;
    exports com.telenav.kivakit.metrics.core.scalar;
    exports com.telenav.kivakit.metrics.core.aggregates.bytes;
    exports com.telenav.kivakit.metrics.core.aggregates.count;
    exports com.telenav.kivakit.metrics.core.aggregates.rate;
    exports com.telenav.kivakit.metrics.core.aggregates.duration;
}
