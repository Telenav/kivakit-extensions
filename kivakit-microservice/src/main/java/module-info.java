open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.serialization.jersey.json;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.rest;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.rest.methods;
    exports com.telenav.kivakit.microservice.rest.servlet;
}
