open module kivakit.data.formats.xml
{
    // KivaKit
    requires transitive kivakit.resource;
    requires transitive kivakit.component;

    // Java
    requires transitive java.xml;

    // Module exports
    exports com.telenav.kivakit.data.formats.xml.stax;
}
