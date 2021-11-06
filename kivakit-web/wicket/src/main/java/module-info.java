open module kivakit.web.wicket
{
    // KivaKit
    requires transitive kivakit.web.jetty;

    // Wicket
    requires transitive org.apache.wicket.core;
    requires transitive org.apache.wicket.util;
    requires transitive org.apache.wicket.request;

    requires transitive org.junit.jupiter;
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.platform.commons;
    requires transitive org.apiguardian.api;

    // Jetty
    requires org.eclipse.jetty.servlet;
    requires javax.servlet.api;

    // Module exports
    exports com.telenav.kivakit.web.wicket.behaviors.status;
    exports com.telenav.kivakit.web.wicket.components.feedback;
    exports com.telenav.kivakit.web.wicket.components.header;
    exports com.telenav.kivakit.web.wicket.components.refresh;
    exports com.telenav.kivakit.web.wicket.library;
    exports com.telenav.kivakit.web.wicket.theme;
    exports com.telenav.kivakit.web.wicket;
}
