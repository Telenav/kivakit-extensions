open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.protostuff.merged;
    requires transitive kivakit.grpc.merged;
    requires transitive kivakit.math;
    requires kivakit.test;

    // Java
    requires java.annotation;

    // Utilities
    requires com.google.common;

    // Java
    requires java.sql;

    // JSON
    requires transitive gson;

    // Protocols
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.protobuf;

    // Wicket
    requires wicket.core;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.microservlet;
    exports com.telenav.kivakit.microservice.protocols.grpc;
    exports com.telenav.kivakit.microservice.protocols.rest;
    exports com.telenav.kivakit.microservice.protocols.rest.openapi;
    exports com.telenav.kivakit.microservice.metrics;
    exports com.telenav.kivakit.microservice.metrics.aggregates;
    exports com.telenav.kivakit.microservice.protocols.rest.gson;
}
