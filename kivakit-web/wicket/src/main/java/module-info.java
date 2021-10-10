open module kivakit.web.wicket
{
    // KivaKit
    requires transitive kivakit.web.jetty;
    requires transitive kivakit.component;

    // Wicket
    requires wicket.core;
    requires wicket.util;
    requires wicket.request;

    // Module exports
    exports com.telenav.kivakit.web.wicket.behaviors.status;
    exports com.telenav.kivakit.web.wicket.components.feedback;
    exports com.telenav.kivakit.web.wicket.components.header;
    exports com.telenav.kivakit.web.wicket.components.refresh;
    exports com.telenav.kivakit.web.wicket.library;
    exports com.telenav.kivakit.web.wicket.theme;
    exports com.telenav.kivakit.web.wicket;
}
