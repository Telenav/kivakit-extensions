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
    exports com.telenav.kivakit.microservice.rest.microservlet;
    exports com.telenav.kivakit.microservice.rest.microservlet.jetty;
    exports com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle;
    exports com.telenav.kivakit.microservice.rest.microservlet.jetty.filter;
    exports com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi;
    exports com.telenav.kivakit.microservice.rest.microservlet.model;
    exports com.telenav.kivakit.microservice.rest.microservlet.model.methods;
}
