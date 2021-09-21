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

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-microservice</artifactId>
        <version>1.1.0-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module provides an easy way to create a microservice with a REST and web interface.

### Public API

The following sections catalog the public API for *kivakit-microservice*.

#### Microservice Mini-framework

A [*Microservice*](https://martinfowler.com/articles/microservices.html) is a small, independent cloud service providing a limited, focused API. The *kivakit-microservice* mini-framework makes it easy to create a microservice that has:

 - Jersey + JSON + Swagger REST interface
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

### REST Parameters and OpenAPI

Path and query parameters are automatically converted into a JSON object by KivaKit. This request:

    GET /api/1.0/create-robot/name/rapunzel
    
is identical to this request:

    GET /api/1.0/create-robot?name=rapunzel
    
is identical to this request:

    POST /api/1.0/create-robot
    
    {
        "name" : "rapunzel"
    }

Because path and query parameters are handled in this way, onGet() and onPost() take no parameters. In addition, in Swagger, the OpenAPI @Operation annotation does not require any @Parameter arguments. All arguments to KivaKit request handlers are defined by @Schema annotations on the relevant classes.

### Request and Response Scoping

It can be very convenient to use nested classes to implement request and response classes. It's also possible in many cases to avoid "POJOs" (Plain Old Java Objects) and their getters and setters.

For example:

    public class MultiplyRequest extends MicroservletPostRequest
    {
        public static class Response extends MicroservletResponse
        {
            private int result;
            
            public int result()
            {
                return result;
            }
        }
        
        private final int a;
        private final int b;
        
        public MultiplyRequest(int a, int b)
        {
            this.a = a;
            this.b = b;
        }
        
        public Response onPost()
        {
            final var response = listenTo(new Response());        
            response.result = a * b;
            return response;
        }
    
        @Override
        public Class<Response> responseType()
        {
            return Response.class;
        }
    }
    
Here, the *MultipleRequest* object directly contains the *a* and *b* multiplicands, which are immutable and initialized by the constructor. The nested *Response* class contains the *result* value. Since the *Response* is nested inside the request, the request has access to its private *result* variable, so there is no need for a *result(int)* setter method. The only accessor is the *result()* method for accessing the result of the operation.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Jetty Microservice Plugin*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-jetty.svg)  
[*Microservices*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservice.svg)  
[*Microservlets*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservlet.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.microservice*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.svg)  
[*com.telenav.kivakit.microservice.rest*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.jetty*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.jetty.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.jetty.cycle.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.jetty.filter*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.jetty.filter.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.model*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.model.svg)  
[*com.telenav.kivakit.microservice.rest.microservlet.model.requests*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.rest.microservlet.model.requests.svg)  
[*com.telenav.kivakit.microservice.web*](https://www.kivakit.org/1.1.0-SNAPSHOT/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.web.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 63.6%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-60-96.png" srcset="https://www.kivakit.org/images/meter-60-96-2x.png 2x"/>


The following significant classes are undocumented:  

- JettyOpenApiRequest  
- Microservlet

| Class | Documentation Sections |
|---|---|
| [*BaseMicroservletMessage*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/BaseMicroservletMessage.html) |  |  
| [*BaseMicroservletRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/BaseMicroservletRequest.html) |  |  
| [*JettyMicroserviceResponse*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/cycle/JettyMicroserviceResponse.html) |  |  
| [*JettyMicroservlet*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/JettyMicroservlet.html) |  |  
| [*JettyMicroservletFilter*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/filter/JettyMicroservletFilter.html) |  |  
| [*JettyMicroservletFilterHolder*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/filter/JettyMicroservletFilterHolder.html) |  |  
| [*JettyMicroservletRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/cycle/JettyMicroservletRequest.html) |  |  
| [*JettyMicroservletRequestCycle*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/cycle/JettyMicroservletRequestCycle.html) | Binding |  
| [*JettyOpenApiRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/openapi/JettyOpenApiRequest.html) |  |  
| [*JettyOpenApiRequest.Response*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/jetty/openapi/JettyOpenApiRequest.Response.html) |  |  
| [*Microservice*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/Microservice.html) | Creating a Microservice |  
| | Example |  
| | Mount Paths |  
| [*MicroserviceGsonFactory*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceGsonFactory.html) |  |  
| [*MicroserviceJerseyGsonSerializer*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceJerseyGsonSerializer.html) |  |  
| [*MicroserviceMetadata*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceMetadata.html) |  |  
| [*MicroserviceProject*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceProject.html) |  |  
| [*MicroserviceRestApplication*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceRestApplication.html) | Flow of Control |  
| [*MicroserviceRestResource*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/MicroserviceRestResource.html) |  |  
| [*MicroserviceSettings*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/MicroserviceSettings.html) |  |  
| [*MicroserviceWebApplication*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/web/MicroserviceWebApplication.html) |  |  
| [*Microservlet*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/Microservlet.html) |  |  
| [*MicroservletErrors*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/MicroservletErrors.html) |  |  
| [*MicroservletGetRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/requests/MicroservletGetRequest.html) |  |  
| [*MicroservletPostRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/requests/MicroservletPostRequest.html) |  |  
| [*MicroservletRequest*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/MicroservletRequest.html) |  |  
| [*MicroservletResponse*](https://www.kivakit.org/1.1.0-SNAPSHOT/javadoc/kivakit-extensions/kivakit.microservice/com/telenav/kivakit/microservice/rest/microservlet/model/MicroservletResponse.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

