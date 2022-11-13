@SuppressWarnings("JavaRequiresAutoModule")
open module kivakit.microservice
{
    // KivaKit
    requires transitive kivakit.interfaces;
    requires transitive kivakit.application;
    requires transitive kivakit.settings.stores.zookeeper;
    requires transitive kivakit.web.swagger;
    requires transitive kivakit.network.http;

    requires transitive telenav.third.party.grpc;
    requires transitive telenav.third.party.protostuff;

    // Java
    requires java.sql;
    requires java.net.http;
    requires javax.annotation.api;

    // Utilities
    requires com.google.common;

    // AWS
    requires aws.lambda.java.core;

    // Protocols
    requires com.google.protobuf;

    // OpenAPI
    requires io.swagger.v3.oas.models;

    // JSON
    requires com.google.gson;

    // Netty
    requires io.netty.common;

    // Jetty
    requires org.eclipse.jetty.servlet;
    requires jetty.servlet.api;

    // Module exports
    exports com.telenav.kivakit.microservice;
    exports com.telenav.kivakit.microservice.microservlet;
    exports com.telenav.kivakit.microservice.protocols.grpc;
    exports com.telenav.kivakit.microservice.protocols.lambda;
    exports com.telenav.kivakit.microservice.protocols.rest.openapi;
    exports com.telenav.kivakit.microservice.protocols.rest.http;
    exports com.telenav.kivakit.microservice.protocols.rest.gson;
    exports com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter;
}
