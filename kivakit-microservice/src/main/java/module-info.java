open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.application;
    requires transitive kivakit.settings.stores.zookeeper;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.web.wicket;
    requires transitive kivakit.math;

    requires transitive kivakit.merged.protostuff;
    requires transitive kivakit.merged.grpc;

    requires org.junit.platform.commons;
    requires org.junit.platform.engine;
    requires org.junit.jupiter;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;

    // Java
    requires java.annotation;
    requires javax.servlet.api;

    // Utilities
    requires com.google.common;

    // Java
    requires java.sql;

    // JSON
    requires gson;

    // Protocols and Platforms
    requires aws.lambda.java.core;
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
    exports com.telenav.kivakit.microservice.protocols.lambda;
    exports com.telenav.kivakit.microservice.protocols.rest;
    exports com.telenav.kivakit.microservice.protocols.rest.openapi;
    exports com.telenav.kivakit.microservice.protocols.rest.gson;
    exports com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;
}
