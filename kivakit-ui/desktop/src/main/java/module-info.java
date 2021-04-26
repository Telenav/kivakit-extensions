open module kivakit.ui.desktop
{
    requires transitive java.desktop;

    requires transitive kivakit.core.resource;

    requires transitive com.formdev.flatlaf;

    exports com.telenav.kivakit.ui.swing.component.dialog.message;
    exports com.telenav.kivakit.ui.swing.component.health;
    exports com.telenav.kivakit.ui.swing.component.icon.logo.kivakit;
    exports com.telenav.kivakit.ui.swing.component.icon.search;
    exports com.telenav.kivakit.ui.swing.component.layout.separator;
    exports com.telenav.kivakit.ui.swing.component.panel.output;
    exports com.telenav.kivakit.ui.swing.component.panel.properties;
    exports com.telenav.kivakit.ui.swing.component.panel.section;
    exports com.telenav.kivakit.ui.swing.component.panel.stack;
    exports com.telenav.kivakit.ui.swing.component.panel.titled;
    exports com.telenav.kivakit.ui.swing.component.progress;
    exports com.telenav.kivakit.ui.swing.component.searchlist;
    exports com.telenav.kivakit.ui.swing.component.status;
    exports com.telenav.kivakit.ui.swing.component.version;
    exports com.telenav.kivakit.ui.swing.component;
    exports com.telenav.kivakit.ui.swing.event;
    exports com.telenav.kivakit.ui.swing.graphics.drawing.awt;
    exports com.telenav.kivakit.ui.swing.graphics.drawing.drawables;
    exports com.telenav.kivakit.ui.swing.graphics.drawing;
    exports com.telenav.kivakit.ui.swing.graphics.font;
    exports com.telenav.kivakit.ui.swing.graphics.geometry;
    exports com.telenav.kivakit.ui.swing.graphics.image;
    exports com.telenav.kivakit.ui.swing.graphics.style;
    exports com.telenav.kivakit.ui.swing.layout;
    exports com.telenav.kivakit.ui.swing.model;
    exports com.telenav.kivakit.ui.swing.project;
    exports com.telenav.kivakit.ui.swing.theme.darcula;
    exports com.telenav.kivakit.ui.swing.theme.vanhelsing;
    exports com.telenav.kivakit.ui.swing.theme;
}
