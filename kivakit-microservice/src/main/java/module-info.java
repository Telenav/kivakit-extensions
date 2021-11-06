open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.math;

    requires kivakit.protostuff.merged;
    requires kivakit.grpc.merged;
    requires kivakit.test;

    requires org.junit.platform.commons;
    requires org.junit.platform.engine;
    requires org.junit.jupiter;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    // Java
    requires java.annotation;

    // Utilities
    requires com.google.common;

    // Java
    requires java.sql;

    // JSON
    requires gson;

    // Protocols
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.protobuf;

    // Jetty
    requires org.eclipse.jetty.servlet;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.web;
    exports com.telenav.kivakit.microservice.microservlet;
    exports com.telenav.kivakit.microservice.protocols.grpc;
    exports com.telenav.kivakit.microservice.protocols.rest;
    exports com.telenav.kivakit.microservice.protocols.rest.openapi;
    exports com.telenav.kivakit.microservice.protocols.rest.gson;
}
