open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.jersey;
    requires transitive kivakit.web.swagger;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.rest;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.rest.methods;
    exports com.telenav.kivakit.microservice.rest.servlet;
    exports com.telenav.kivakit.microservice.rest.microservlet;
    exports com.telenav.kivakit.microservice.rest.microservlet.jetty;
    exports com.telenav.kivakit.microservice.rest.microservlet.cycle;
}
