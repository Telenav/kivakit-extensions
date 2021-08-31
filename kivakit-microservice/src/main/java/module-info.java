open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.serialization.json;
    requires transitive kivakit.serialization.jersey.json;

    // Java
    requires transitive java.ws.rs;
    requires kivakit.web.swagger;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.rest;
    exports com.telenav.kivakit.microservice.rest.serialization;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.methods;
    exports com.telenav.kivakit.microservice.rest.resources.openapi;
}
