@SuppressWarnings("JavaRequiresAutoModule")
open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.interfaces;
    requires transitive kivakit.application;
    requires transitive kivakit.settings.stores.zookeeper;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.web.wicket;

    requires transitive kivakit.merged.protostuff;
    requires transitive kivakit.merged.grpc;

    // Java
    requires java.sql;
    requires java.annotation;
    requires javax.servlet.api;

    // Utilities
    requires com.google.common;

    // AWS
    requires aws.lambda.java.core;

    // Protocols
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires com.google.protobuf;
    requires io.netty.common;

    // JSON
    requires gson;

    // Jetty
    requires org.eclipse.jetty.servlet;
    requires io.swagger.v3.oas.models;
    requires kivakit.network.http;

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
