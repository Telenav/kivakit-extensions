open module kivakit.ui.desktop
{
    // KivaKit
    requires transitive kivakit.resource;

    requires transitive java.desktop;

    // Darcula scheme
    requires transitive com.formdev.flatlaf;

    requires org.jetbrains.annotations;

    // Module exports
    exports com.telenav.kivakit.ui.desktop.component.dialog.message;
    exports com.telenav.kivakit.ui.desktop.component.health;
    exports com.telenav.kivakit.ui.desktop.component.icon.logo.kivakit;
    exports com.telenav.kivakit.ui.desktop.component.icon.search;
    exports com.telenav.kivakit.ui.desktop.component.layout.separator;
    exports com.telenav.kivakit.ui.desktop.component.panel.output;
    exports com.telenav.kivakit.ui.desktop.component.panel.properties;
    exports com.telenav.kivakit.ui.desktop.component.panel.section;
    exports com.telenav.kivakit.ui.desktop.component.panel.stack;
    exports com.telenav.kivakit.ui.desktop.component.panel.titled;
    exports com.telenav.kivakit.ui.desktop.component.progress;
    exports com.telenav.kivakit.ui.desktop.component.searchlist;
    exports com.telenav.kivakit.ui.desktop.component.status;
    exports com.telenav.kivakit.ui.desktop.component.version;
    exports com.telenav.kivakit.ui.desktop.component;
    exports com.telenav.kivakit.ui.desktop.event;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.geometry;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.style;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d;
    exports com.telenav.kivakit.ui.desktop.graphics.drawing;
    exports com.telenav.kivakit.ui.desktop.graphics.image;
    exports com.telenav.kivakit.ui.desktop.layout;
    exports com.telenav.kivakit.ui.desktop.model;
    exports com.telenav.kivakit.ui.desktop.theme.darcula;
    exports com.telenav.kivakit.ui.desktop.theme.vanhelsing;
    exports com.telenav.kivakit.ui.desktop.theme;
    exports com.telenav.kivakit.ui.desktop;
}
