open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.test;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.rest;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.rest.microservlet;
    exports com.telenav.kivakit.microservice.rest.microservlet.metrics;
    exports com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates;
    exports com.telenav.kivakit.microservice.rest.microservlet.openapi;
    exports com.telenav.kivakit.microservice.rest.microservlet.requests;
}
