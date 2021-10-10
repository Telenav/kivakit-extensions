open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.test;

    requires grpc.api;
    requires grpc.stub;
    requires grpc.netty;
    requires grpc.protobuf;

    requires com.google.protobuf;
    requires protostuff.core;
    requires protostuff.runtime;
    requires protostuff.api;
    requires java.annotation;

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
