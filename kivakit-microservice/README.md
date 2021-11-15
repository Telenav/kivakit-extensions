[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://www.kivakit.org/images/web-32.png" srcset="https://www.kivakit.org/images/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://www.kivakit.org/images/twitter-32.png" srcset="https://www.kivakit.org/images/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://www.kivakit.org/images/zulip-32.png" srcset="https://www.kivakit.org/images/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-microservice &nbsp;&nbsp; <img src="https://www.kivakit.org/images/puzzle-32.png" srcset="https://www.kivakit.org/images/puzzle-32-2x.png 2x"/>

This module provides an abstraction for developing microservices.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Public API**](#public-api)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-microservice</artifactId>
        <version>1.1.3-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module provides an easy way to create a microservice with a REST and web interface.

### Public API <a name = "public-api"></a>

The following sections catalog the public API for *kivakit-microservice*.

#### Microservice Mini-framework

A [*Microservice*](https://martinfowler.com/articles/microservices.html) is a small, independent cloud service providing a limited, focused API. The *kivakit-microservice* mini-framework makes it easy to create a microservice that has:

- JSON Microservlet REST service

+ Swagger OpenAPI specification

- Apache Wicket web interface

The public API classes in this mini-framework are:

| Class | Purpose |
|-------|---------|
| Microservice | Application base class for microservices |
| MicroserviceMetadata | Metadata describing a microservice |
| MicroserviceSettings | Settings for microservices applications |
| MicroserviceWebApplication | Apache Wicket application base class |
| MicroserviceRestApplication | Base class for REST applications |
| MicroserviceGsonFactory | Factory that produces configured Gson objects for JSON serialization |

#### Microservlet Mini-framework

A *Microservlet* is a handler for HTTP REST methods that can be *mounted* on a specific path in a *MicroserviceRestApplication*. Each method (GET, POST, etc.) takes a subclass of *MicroserviceRequest* and returns a *MicroserviceResponse*. In general, it is not necessary to directly implement a *Microservlet*. Instead, a request class can be directly mounted on a rest application path like this:

    mount("api/1.0/create-robot", CreateRobotRequest.class);

KivaKit will create an anonymous *Microservlet* that instantiates and uses a *CreateRobotRequest* object to handle requests.

The public API classes in this mini-framework are:

| Class | Purpose |
|-------|---------|
| Microservlet | Request handler mounted on a path via MicroserviceRestApplication |
| MicroservletGetRequest | Base class for a GET request handler |
| MicroservletPostRequest | Base class for a POST request handler |
| MicroservletResponse | Holds the response to a request |

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Jetty Microservice Plugin*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-jetty.svg)  
[*Microservices*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservice.svg)  
[*Microservlets*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservlet.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.microservice*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.svg)  
[*com.telenav.kivakit.microservice.internal.protocols*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.grpc*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.grpc.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.cycle*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.cycle.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.openapi*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.openapi.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.svg)  
[*com.telenav.kivakit.microservice.microservlet*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.microservlet.svg)  
[*com.telenav.kivakit.microservice.protocols.grpc*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.grpc.svg)  
[*com.telenav.kivakit.microservice.protocols.rest*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.gson*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.gson.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.openapi*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.openapi.svg)  
[*com.telenav.kivakit.microservice.web*](https://www.kivakit.org/1.1.3-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.web.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 61.7%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-60-96.png" srcset="https://www.kivakit.org/images/meter-60-96-2x.png 2x"/>


The following significant classes are undocumented:  

- MicroserviceGrpcClient  
- RuntimeProtoGenerator

| Class | Documentation Sections |
|---|---|
| [*AnnotationReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/AnnotationReader.html) |  |  
| [*ArrayObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/ArrayObject.html) |  |  
| [*ArraySerializer*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/ArraySerializer.html) |  |  
| [*BaseMicroservletRequest*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/BaseMicroservletRequest.html) |  |  
| [*BaseMicroservletResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/BaseMicroservletResponse.html) |  |  
| [*Constants*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/Constants.html) |  |  
| [*DynamicObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/DynamicObject.html) |  |  
| [*EnumObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/EnumObject.html) |  |  
| [*Generators*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/Generators.html) |  |  
| [*JavaObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/JavaObject.html) |  |  
| [*JettyMicroserviceResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/cycle/JettyMicroserviceResponse.html) |  |  
| [*JettyMicroservletFilter*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/filter/JettyMicroservletFilter.html) |  |  
| [*JettyMicroservletFilter.ResolvedMicroservlet*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/filter/JettyMicroservletFilter.ResolvedMicroservlet.html) |  |  
| [*JettyMicroservletFilterHolder*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/filter/JettyMicroservletFilterHolder.html) |  |  
| [*JettyMicroservletRequest*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/cycle/JettyMicroservletRequest.html) |  |  
| [*JettyMicroservletRequestCycle*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/cycle/JettyMicroservletRequestCycle.html) | Microservlet Binding |  
| | Request and Response |  
| [*JettyOpenApiRequest*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/JettyOpenApiRequest.html) |  |  
| [*JettyOpenApiRequest.JettyOpenApiResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/JettyOpenApiRequest.JettyOpenApiResponse.html) |  |  
| [*ListSerializer*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/ListSerializer.html) |  |  
| [*MapEntryObjectObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapEntryObjectObject.html) |  |  
| [*MapEntryStringObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapEntryStringObject.html) |  |  
| [*MapEntryStringString*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapEntryStringString.html) |  |  
| [*MapObjectObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapObjectObject.html) |  |  
| [*MapSerializer*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/MapSerializer.html) |  |  
| [*MapStringObject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapStringObject.html) |  |  
| [*MapStringString*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/MapStringString.html) |  |  
| [*Microservice*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/Microservice.html) | Creating a Microservice |  
| | Example |  
| | Mount Paths |  
| [*MicroserviceGrpcClient*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/grpc/MicroserviceGrpcClient.html) |  |  
| [*MicroserviceGrpcService*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/grpc/MicroserviceGrpcService.html) |  |  
| [*MicroserviceGsonFactory*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/gson/MicroserviceGsonFactory.html) |  |  
| [*MicroserviceGsonFactorySource*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/gson/MicroserviceGsonFactorySource.html) |  |  
| [*MicroserviceGsonObjectSource*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/gson/MicroserviceGsonObjectSource.html) |  |  
| [*MicroserviceMetadata*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceMetadata.html) |  |  
| [*MicroserviceProject*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceProject.html) |  |  
| [*MicroserviceRestClient*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/MicroserviceRestClient.html) |  |  
| [*MicroserviceRestService*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/MicroserviceRestService.html) | Internal Details - Flow of Control |  
| [*MicroserviceRestService.HttpMethod*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/MicroserviceRestService.HttpMethod.html) |  |  
| [*MicroserviceSettings*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceSettings.html) |  |  
| [*MicroserviceWebApplication*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/web/MicroserviceWebApplication.html) |  |  
| [*Microservlet*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/Microservlet.html) | IMPORTANT NOTE |  
| [*MicroservletError*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletError.html) |  |  
| [*MicroservletErrorResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletErrorResponse.html) |  |  
| [*MicroservletFutureResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/grpc/MicroservletFutureResponse.html) |  |  
| [*MicroservletGrpcResponder*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/MicroservletGrpcResponder.html) |  |  
| [*MicroservletGrpcSchemas*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/MicroservletGrpcSchemas.html) |  |  
| [*MicroservletJettyFilterPlugin*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/MicroservletJettyFilterPlugin.html) | NOTE |  
| [*MicroservletMountTarget*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/MicroservletMountTarget.html) |  |  
| [*MicroservletRequest*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletRequest.html) |  |  
| [*MicroservletRequestHandler*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletRequestHandler.html) |  |  
| [*MicroservletRequestStatistics*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletRequestStatistics.html) |  |  
| [*MicroservletRequestStatisticsAggregator*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletRequestStatisticsAggregator.html) |  |  
| [*MicroservletResponse*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/microservlet/MicroservletResponse.html) |  |  
| [*MicroservletRestPath*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/MicroservletRestPath.html) |  |  
| [*OpenApiAnnotationReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/OpenApiAnnotationReader.html) |  |  
| [*OpenApiExcludeMember*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/openapi/OpenApiExcludeMember.html) |  |  
| [*OpenApiGsonFactory*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/OpenApiGsonFactory.html) |  |  
| [*OpenApiIncludeMember*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/openapi/OpenApiIncludeMember.html) |  |  
| [*OpenApiIncludeMemberFromSuperType*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/openapi/OpenApiIncludeMemberFromSuperType.html) |  |  
| [*OpenApiIncludeMemberFromSuperTypeRepeater*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/openapi/OpenApiIncludeMemberFromSuperTypeRepeater.html) |  |  
| [*OpenApiIncludeType*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/openapi/OpenApiIncludeType.html) |  |  
| [*OpenApiPathReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/OpenApiPathReader.html) |  |  
| [*OpenApiPropertyFilter*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/filters/OpenApiPropertyFilter.html) |  |  
| [*OpenApiReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/OpenApiReader.html) | Annotations |  
| [*OpenApiRequestHandler*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/protocols/rest/openapi/OpenApiRequestHandler.html) |  |  
| [*OpenApiSchemaReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/OpenApiSchemaReader.html) |  |  
| [*OpenApiTypeFilter*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/filters/OpenApiTypeFilter.html) |  |  
| [*Pair*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/Pair.html) |  |  
| [*PrimitiveReader*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/PrimitiveReader.html) |  |  
| [*ProblemReportingTrait*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/cycle/ProblemReportingTrait.html) |  |  
| [*ProtoGenerator*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/ProtoGenerator.html) |  |  
| [*ProtostuffThreadLocal*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/ProtostuffThreadLocal.html) |  |  
| [*ProtostuffThreadLocal.Instantiator*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/ProtostuffThreadLocal.Instantiator.html) |  |  
| [*ReferenceResolver*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/ReferenceResolver.html) |  |  
| [*ReflectionUtil*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/ReflectionUtil.html) |  |  
| [*RuntimeFieldType*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeFieldType.html) |  |  
| [*RuntimeProtoGenerator*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeProtoGenerator.html) |  |  
| [*RuntimeProtoGenerator.ClassNameComparator*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeProtoGenerator.ClassNameComparator.html) |  |  
| [*RuntimeProtoGenerator.EnumObj*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeProtoGenerator.EnumObj.html) |  |  
| [*RuntimeProtoGenerator.Message*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeProtoGenerator.Message.html) |  |  
| [*RuntimeSchemaType*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/RuntimeSchemaType.html) |  |  
| [*SchemaCopier*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/reader/SchemaCopier.html) |  |  
| [*SetSerializer*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/SetSerializer.html) |  |  
| [*StringSerializer*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/rest/plugins/jetty/openapi/serialization/StringSerializer.html) |  |  
| [*UUID*](https://www.kivakit.org/1.1.3-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/internal/protocols/grpc/runtimeproto/UUID.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

