open module kivakit.web.wicket
{
    // KivaKit
    requires transitive kivakit.web.jetty;

    // Wicket
    requires transitive org.apache.wicket.core;
    requires org.junit.jupiter;
    requires org.junit.platform.commons;

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
