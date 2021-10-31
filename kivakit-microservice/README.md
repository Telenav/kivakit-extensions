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

[*Dependency Diagram*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-microservice</artifactId>
        <version>1.1.0</version>
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

[*Jetty Microservice Plugin*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-jetty.svg)  
[*Microservices*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservice.svg)  
[*Microservlets*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservlet.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.microservice*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.svg)  
[*com.telenav.kivakit.microservice.rest*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.cycle*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.cycle.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.metrics*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.metrics.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.openapi*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.openapi.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.cycle.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.filter.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader.filters*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.reader.filters.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.openapi.serialization.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.metrics*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.metrics.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.metrics.aggregates.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.console*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.console.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.none*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.none.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.openapi*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.openapi.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.requests*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.requests.svg)  
[*com.telenav.kivakit.microservice.web*](https://www.kivakit.org/1.1.0/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.web.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 63.6%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-60-96.png" srcset="https://www.kivakit.org/images/meter-60-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*AggregateMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/AggregateMetric.html) |  |  
| [*AnnotationReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/AnnotationReader.html) |  |  
| [*ArraySerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/ArraySerializer.html) |  |  
| [*AverageBytesMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/AverageBytesMetric.html) |  |  
| [*AverageCountMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/AverageCountMetric.html) |  |  
| [*AverageMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/AverageMetric.html) |  |  
| [*AverageRateMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/AverageRateMetric.html) |  |  
| [*BaseMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/metrics/BaseMetric.html) |  |  
| [*BaseMicroservletMessage*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/cycle/BaseMicroservletMessage.html) |  |  
| [*BaseMicroservletRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/cycle/BaseMicroservletRequest.html) |  |  
| [*ConsoleMetricReporter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/reporters/console/ConsoleMetricReporter.html) |  |  
| [*JettyMicroserviceResponse*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/cycle/JettyMicroserviceResponse.html) |  |  
| [*JettyMicroservletFilter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/filter/JettyMicroservletFilter.html) |  |  
| [*JettyMicroservletFilter.ResolvedMicroservlet*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/filter/JettyMicroservletFilter.ResolvedMicroservlet.html) |  |  
| [*JettyMicroservletFilterHolder*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/filter/JettyMicroservletFilterHolder.html) |  |  
| [*JettyMicroservletRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/cycle/JettyMicroservletRequest.html) |  |  
| [*JettyMicroservletRequestCycle*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/cycle/JettyMicroservletRequestCycle.html) | Microservlet Binding |  
| | Request and Response |  
| [*JettyOpenApiRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/JettyOpenApiRequest.html) |  |  
| [*JettyOpenApiRequest.JettyOpenApiResponse*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/JettyOpenApiRequest.JettyOpenApiResponse.html) |  |  
| [*ListSerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/ListSerializer.html) |  |  
| [*MapSerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/MapSerializer.html) |  |  
| [*MaximumBytesMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MaximumBytesMetric.html) |  |  
| [*MaximumCountMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MaximumCountMetric.html) |  |  
| [*MaximumMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MaximumMetric.html) |  |  
| [*MaximumRateMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MaximumRateMetric.html) |  |  
| [*Metric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/Metric.html) |  |  
| [*MetricReporter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/MetricReporter.html) |  |  
| [*MetricReportingTrait*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/metrics/MetricReportingTrait.html) |  |  
| [*Microservice*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/Microservice.html) | Creating a Microservice |  
| | Example |  
| | Mount Paths |  
| [*MicroserviceMetadata*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceMetadata.html) |  |  
| [*MicroserviceProject*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceProject.html) |  |  
| [*MicroserviceRestApplication*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceRestApplication.html) | Internal Details - Flow of Control |  
| [*MicroserviceRestApplicationGsonFactory*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceRestApplicationGsonFactory.html) |  |  
| [*MicroserviceSettings*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceSettings.html) |  |  
| [*MicroserviceWebApplication*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/web/MicroserviceWebApplication.html) |  |  
| [*Microservlet*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/Microservlet.html) | IMPORTANT NOTE |  
| [*MicroservletClient*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/MicroservletClient.html) |  |  
| [*MicroservletDeleteRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/requests/MicroservletDeleteRequest.html) |  |  
| [*MicroservletError*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/MicroservletError.html) |  |  
| [*MicroservletErrorResponse*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/MicroservletErrorResponse.html) |  |  
| [*MicroservletGetRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/requests/MicroservletGetRequest.html) |  |  
| [*MicroservletJettyFilterPlugin*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/MicroservletJettyFilterPlugin.html) | NOTE |  
| [*MicroservletPath*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/MicroservletPath.html) |  |  
| [*MicroservletPostRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/requests/MicroservletPostRequest.html) |  |  
| [*MicroservletRequest*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/MicroservletRequest.html) |  |  
| [*MicroservletRequest.HttpMethod*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/MicroservletRequest.HttpMethod.html) |  |  
| [*MicroservletResponse*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/MicroservletResponse.html) |  |  
| [*MinimumBytesMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MinimumBytesMetric.html) |  |  
| [*MinimumCountMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MinimumCountMetric.html) |  |  
| [*MinimumMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MinimumMetric.html) |  |  
| [*MinimumRateMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/MinimumRateMetric.html) |  |  
| [*NullMetricReporter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/reporters/none/NullMetricReporter.html) |  |  
| [*OpenApiAnnotationReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/OpenApiAnnotationReader.html) |  |  
| [*OpenApiExcludeMember*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/openapi/OpenApiExcludeMember.html) |  |  
| [*OpenApiIncludeMember*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/openapi/OpenApiIncludeMember.html) |  |  
| [*OpenApiIncludeMemberFromSuperType*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/openapi/OpenApiIncludeMemberFromSuperType.html) |  |  
| [*OpenApiIncludeMemberFromSuperTypeRepeater*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/openapi/OpenApiIncludeMemberFromSuperTypeRepeater.html) |  |  
| [*OpenApiIncludeType*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/openapi/OpenApiIncludeType.html) |  |  
| [*OpenApiPathReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/OpenApiPathReader.html) |  |  
| [*OpenApiPropertyFilter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/filters/OpenApiPropertyFilter.html) |  |  
| [*OpenApiReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/OpenApiReader.html) | Annotations |  
| [*OpenApiRequestHandler*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/openapi/OpenApiRequestHandler.html) |  |  
| [*OpenApiSchemaReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/OpenApiSchemaReader.html) |  |  
| [*OpenApiSerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/OpenApiSerializer.html) |  |  
| [*OpenApiTypeFilter*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/filters/OpenApiTypeFilter.html) |  |  
| [*PrimitiveReader*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/PrimitiveReader.html) |  |  
| [*ProblemReportingTrait*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/cycle/ProblemReportingTrait.html) |  |  
| [*QuantumMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/QuantumMetric.html) |  |  
| [*ReferenceResolver*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/ReferenceResolver.html) |  |  
| [*ScalarMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/ScalarMetric.html) |  |  
| [*SchemaCopier*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/reader/SchemaCopier.html) |  |  
| [*SetSerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/SetSerializer.html) |  |  
| [*StringSerializer*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/internal/plugins/jetty/openapi/serialization/StringSerializer.html) |  |  
| [*TotalBytesMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/TotalBytesMetric.html) |  |  
| [*TotalCountMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/TotalCountMetric.html) |  |  
| [*TotalMetric*](https://www.kivakit.org/1.1.0/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/metrics/aggregates/TotalMetric.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

