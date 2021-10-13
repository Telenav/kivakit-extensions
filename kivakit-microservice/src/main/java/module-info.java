open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.test;
    requires kivakit.protostuff.merged;
    requires kivakit.grpc.merged;

    requires java.annotation;
    requires javax.servlet.api;

    requires org.jetbrains.annotations;

    requires wicket.core;

    requires io.swagger.v3.core;
    requires io.swagger.v3.oas.models;

    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    requires org.eclipse.jetty.servlet;
    requires gson;

    requires com.google.protobuf;
    requires com.google.common;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.microservlet;
    exports com.telenav.kivakit.microservice.microservlet.grpc;
    exports com.telenav.kivakit.microservice.microservlet.rest;
    exports com.telenav.kivakit.microservice.microservlet.rest.openapi;
    exports com.telenav.kivakit.microservice.metrics;
    exports com.telenav.kivakit.microservice.metrics.aggregates;
    exports com.telenav.kivakit.microservice.microservlet.rest.gson;
}
