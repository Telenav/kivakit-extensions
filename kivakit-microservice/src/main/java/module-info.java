open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.test;
    requires transitive kivakit.protostuff.merged;

    requires transitive java.annotation;

    requires transitive grpc.api;
    requires transitive grpc.stub;
    requires transitive grpc.netty;
    requires transitive grpc.protobuf;

    requires transitive com.google.protobuf;
    requires transitive com.google.common;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.microservlet;
    exports com.telenav.kivakit.microservice.microservlet.grpc;
    exports com.telenav.kivakit.microservice.microservlet.rest;
    exports com.telenav.kivakit.microservice.microservlet.rest.openapi;
    exports com.telenav.kivakit.microservice.microservlet.metrics;
    exports com.telenav.kivakit.microservice.microservlet.metrics.aggregates;
}
