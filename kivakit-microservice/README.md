[//]: # (start-user-text)

<a href="https://www.kivakit.org">
<img src="https://telenav.github.io/telenav-assets/images/icons/web-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/web-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://twitter.com/openkivakit">
<img src="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/twitter/twitter-32-2x.png 2x"/>
</a>
&nbsp;
<a href="https://kivakit.zulipchat.com">
<img src="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32.png" srcset="https://telenav.github.io/telenav-assets/images/logos/zulip/zulip-32-2x.png 2x"/>
</a>

[//]: # (end-user-text)

# kivakit-microservice &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/puzzle-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/puzzle-32-2x.png 2x"/>

This module provides an abstraction for developing microservices.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Microservice API**](#microservice-api)  
[**A Microservice from Beginning to End**](#a-microservice-from-beginning-to-end)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-microservice</artifactId>
        <version>1.8.2</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

<a name = "summary"></a>
### Summary <a name = "summary"></a>

This module provides an easy way to create a microservice with multiple service interfaces, including Web, Swagger, REST, GRPC, and AWS Lambda. A single object-oriented request handler can be reused unchanged with all supported protocols (REST, GRPC and AWS Lambda).

 <a name = "articles"></a>
 ### Articles

 - [Build a KivaKit Microservice in 5 Minutes](https://towardsdev.com/build-a-kivakit-microservice-in-5-minutes-b5067cd26b60)
 - [The KivaKit Microservice Framework in Depth](https://towardsdev.com/the-kivakit-microservices-framework-3cd1d064107c)
 - [KivaKit Microservice Clustering](https://state-of-the-art.org/2021/12/22/clustering.html)
 - [KivaKit Microservices and AWS Lambda](https://state-of-the-art.org/2021/12/25/aws-lambda.html)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<a name = "api"></a>
### Microservice API <a name = "microservice-api"></a>

The following sections catalog the public API for *kivakit-microservice*.

#### Microservice Mini-framework

A [*Microservice*](https://martinfowler.com/articles/microservices.html) is a small, independent cloud service providing a limited, focused API. The *kivakit-microservice*
mini-framework makes it easy to create a microservice that has these service interfaces:

- Web (Apache Wicket)
- REST (GlassFish Jersey)
- GRPC (Google Remote Procedure Call)
- AWS Lambda (Amazon Web Services)
- Swagger REST Documentation (OpenAPI)

The public API classes in this mini-framework are:

| Class                      | Purpose                                                              |
|----------------------------|----------------------------------------------------------------------|
| Microservice               | Base class for microservices                                         |
| MicroserviceMetadata       | Metadata describing a microservice                                   |
| MicroserviceCluster        | The Microservices in a cluster (tracked by Zookeeper)                |
| MicroserviceClusterMember  | A Microservice that is participating in a cluster                    |
| MicroserviceSettings       | Settings for microservices applications                              |
| MicroserviceWebApplication | Apache Wicket application base class                                 |
| MicroserviceRestService    | Base class for REST service interfaces                               |
| MicroserviceGrpcService    | Base class for GRPC service interfaces                               |
| MicroserviceLambdaService  | Base class for AWS Lambda service interfaces                         |
| MicroserviceGsonFactory    | Factory that produces configured Gson objects for JSON serialization |

<br/>
<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

#### Microservlet Mini-framework

A *Microservlet* is a handler for HTTP REST methods that can be *mounted* on a specific path in the
*onInitialize()* method of a *MicroserviceRestService* subclass. Each path and method (GET, POST, etc.)
takes a subclass of *MicroserviceRequest* and returns a subclass of *MicroserviceResponse*. For example:

    public class CreateRobotRequest extends MicroserviceRequest { ... }
    public class CreateRobotResponse extends MicroserviceResponse { ... }

In general, it is not necessary to directly implement a *Microservlet*. Instead, a request class can be directly mounted on a rest service path like this:

    var v2 = parseVersion("2.0")

    mount("create-robot", POST, CreateRobotRequest.class);
    mount("get-robot", GET, GetRobotRequest.class);
    mount("get-robot", GET, v2, GetRobotRequestV2.class);

KivaKit will create and install an anonymous *Microservlet* that instantiates and uses a *CreateRobotRequest* (or other request object) to handle requests. All you need to do is call *mount()* in *onInitialize()*.

The public API classes in the microservlet mini-framework are:

| Class                | Purpose                                                       |
|----------------------|---------------------------------------------------------------|
| Microservlet         | Request handler mounted on a path via MicroserviceRestService |
| MicroservletRequest  | Base class for a request handling object                      |
| MicroservletResponse | The response object generated by a MicroservletRequest        |

<br/>
<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<a name = "details"></a>
### A Microservice from Beginning to End <a name = "a-microservice-from-beginning-to-end"></a>

#### 1. Creating the Microservice

To create a KivaKit microservice, see [Build a KivaKit Microservice in 5 Minutes](https://towardsdev.com/build-a-kivakit-microservice-in-5-minutes-b5067cd26b60).

#### 2. Microservice Start Up

1. An instance of the user's `Microservice` application subclass is created in `main(String[])` and `run()` is called
2. A `JettyServer` instance is created
3. A `RestService` subclass is created in `Microservice.onNewRestService()` and the 
4. A `MicroservletJettyPlugin` is created for the `RestService`
5. The Jetty plugin creates an instance of `JettyMicroservletFilter`
6. The Jetty plugin is mounted on the path specified `/*`, which installs the filter
7. The `initialize()` method of the rest service is called to initialize the service
8. In `RestService.onInitialize()` method, the user's code calls `mount*()` methods to bind `MicroservletRequest` handlers to mount paths
9. Jetty Server starts up

#### 3. Handling a Request
    
1. An HTTP request is made to the rest server by a client
2. The Servlet API `JettyMicroservletFilter.doFilter(ServletRequest, ServletResponse, FilterChain)` method is called
3. Request parameters are processed
   - If the HTTP request method is `GET`, any path or query parameters are turned into a JSON object, which is processed as if it were posted
   - If the HTTP request method is `POST`, then the posted JSON object is read by `JettyRestRequest.readRequest(Class)`
   - If the HTTP request method is `DELETE`, any path or query parameters are turned into a JSON object, which is processed as if it were posted
4. The request path is examined to see if it is a mount path for a `MountedMicroservlet`. If no mounted microservlet is found, the
   request is passed to the next filter in the filter chain.
5. The `MountedMicroservlet.handleRequest(HttpMethod, RestRequestCycle)` method is called
6. The `Microservlet` is retrieved, and `Microservlet.respond(Request)` is method called
7. If the microservlet was added with `RestService.mount*()` (as in step 8 above), an anonymous subclass of `Microservlet` was used
   to associate the user's `MicroservletRequest` subclass.
   - When the anonymous microservlet is called, it instantiates the user's `MicroservletRequest` subclass
   - The `MicroservletRequest.respond()` method is called

#### 4. Producing a Response
    
1. The user's code in the `Microservlet.onRespond(MicroservletRequest)` method is called
2. The request handler's return value is passed to `JettyRestResponse.writeResponse(MicroservletResponse)`, which: 
   - Validates the response object by calling `Validatable.validator()` and `Validator.validate(Listener)`
   - Converts the object to output (normally JSON) using the `ObjectSerializer` object provided by `RestService.serializer()`
   - Writes the JSON object to the servlet response output stream
3. Jetty returns the response to the client

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<a name = "protocols"></a>
#### GRPC and AWS Lambda Protocols

The unmodified *MicroservletRequest* handler for a REST service can be reused unmodified by the GRPC, and AWS Lambda protocols.

The *MicroserviceGrpcService* class copies its request handler mounts from *MicroserviceRestService*. If you have written a REST service you can turn on GRPC by simply returning an instance of MicroserviceGrpcService from *Microservice.onNewGrpcService()*

*MicroserviceLambdaService* can mount request handlers in its *onInitialize()* method like this:

    public class MyLambdaService extends MicroserviceLambdaService
    {
        public MyLambdaService(Microservice<?> microservice)
        {
            super(microservice);
        }
    
        public void onInitialize()
        {
            mount("get-robot", "1.0", GetRobotRequest.class);
        }
    }

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 38.5%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-40-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-40-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 47.4%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-50-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-50-96-2x.png 2x"/>     |
| Testing       | 3.1%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>       |
| Documentation | 64.9%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-60-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-60-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*Jetty Microservice Plugin*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-jetty.svg)  
[*Microservices*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservice.svg)  
[*Microservlets*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/diagram-microservlet.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.microservice*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.svg)  
[*com.telenav.kivakit.microservice.internal.lexakai*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.lexakai.svg)  
[*com.telenav.kivakit.microservice.internal.protocols*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.grpc*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.grpc.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.cycle.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.filter.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.reader.filters.svg)  
[*com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.openapi.serialization.svg)  
[*com.telenav.kivakit.microservice.microservlet*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.microservlet.svg)  
[*com.telenav.kivakit.microservice.protocols.grpc*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.grpc.svg)  
[*com.telenav.kivakit.microservice.protocols.lambda*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.lambda.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.gson*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.gson.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.health*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.health.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.http*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.http.svg)  
[*com.telenav.kivakit.microservice.protocols.rest.openapi*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.protocols.rest.openapi.svg)  
[*com.telenav.kivakit.microservice.web*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-microservice/documentation/diagrams/com.telenav.kivakit.microservice.web.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*AnnotationReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ArrayObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ArraySerializer*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*BaseMicroservletRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////.html) |  |  
| [*BaseMicroservletResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////.html) |  |  
| [*BaseMounted*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*Constants*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramJetty*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramMicroservice*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////.html) |  |  
| [*DiagramMicroservlet*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////.html) |  |  
| [*DynamicObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*EnumObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*Generators*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*HealthLiveRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////.html) |  |  
| [*HealthLiveRequest.HealthLiveResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*HealthReadyRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////.html) |  |  
| [*HealthReadyRequest.HealthReadyResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*HttpProblem*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////.html) |  |  
| [*JavaObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*JettyMicroservletFilter*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////////.html) | Microservlet Mounts |  
| | JAR Mounts |  
| [*JettyMicroservletFilterHolder*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*JettyRestRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*JettyRestRequestCycle*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////.html) | Microservlet Binding |  
| | Request and Response |  
| [*JettyRestResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*LambdaFunction*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////.html) |  |  
| [*LambdaRequestHandler*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////.html) | Logging |  
| | Configuration |  
| | Security |  
| [*ListSerializer*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapEntryObjectObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapEntryStringObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapEntryStringString*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapObjectObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapSerializer*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapStringObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MapStringString*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*Microservice*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////.html) | Services |  
| | Creating a Microservice |  
| | Command Line Switches |  
| | Example: |  
| | Lifecycle |  
| | Cluster Elections |  
| | Cluster Membership |  
| | Mount Paths |  
| | Paths |  
| | Properties |  
| | Clustering |  
| | Root Path |  
| [*MicroserviceCluster*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////.html) | Starting Up |  
| | As Members Join and Leave |  
| | Membership |  
| | Leader Elections |  
| [*MicroserviceClusterMember*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////.html) | Properties |  
| | Leader Elections |  
| [*MicroserviceGrpcClient*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////.html) | Creation |  
| | Making Requests |  
| | Stopping |  
| [*MicroserviceGrpcService*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////.html) | Creation |  
| | Lifecycle |  
| | Mounting Request Handlers |  
| [*MicroserviceGsonObjectSource*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroserviceLambdaService*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////.html) | AWS Installation |  
| | Mounting Lambda Request Handlers |  
| | Security |  
| [*MicroserviceMetadata*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////.html) | Functional |  
| | Properties |  
| [*MicroserviceProject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////.html) |  |  
| [*MicroserviceSettings*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////.html) | Properties |  
| [*Microservlet*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////.html) | Responding |  
| | Parameters |  
| | IMPORTANT NOTE |  
| | Properties |  
| [*MicroservletError*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////.html) | Sending |  
| | Properties |  
| [*MicroservletErrorResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////.html) | Properties |  
| | Sending Error Messages |  
| [*MicroservletFutureResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletGrpcResponder*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletGrpcSchemas*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletJettyPlugin*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////.html) | NOTE |  
| [*MicroservletMountTarget*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletPerformance*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////.html) | Request Handling |  
| | Performance |  
| [*MicroservletPerformanceStatistics*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletRequestHandler*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////.html) |  |  
| [*MicroservletResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////.html) |  |  
| [*MountedApi*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////////////.html) | WARNING |  
| [*MountedMicroservlet*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiAnnotationReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiExcludeMember*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiIncludeMember*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiIncludeMemberFromSuperType*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiIncludeMemberFromSuperTypeRepeater*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiIncludeType*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiJsonRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiJsonRequest.JettyOpenApiResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiPathReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiPropertyFilter*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////.html) | Annotations |  
| [*OpenApiRequestHandler*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiSchemaReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*OpenApiTypeFilter*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*Pair*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////.html) |  |  
| [*PrimitiveReader*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ProtoGenerator*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ProtostuffThreadLocal*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ProtostuffThreadLocal.Instantiator*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ReferenceResolver*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*ReflectionUtil*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RestClient*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////.html) | Creation |  
| | HTTP Requests |  
| | Properties |  
| [*RestPath*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////.html) | Creation |  
| | Properties |  
| [*RestProblemReportingTrait*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RestRequest*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////.html) |  |  
| [*RestRequestCycle*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////.html) | Properties |  
| | Gson Serialization |  
| [*RestRequestThread*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////.html) |  |  
| [*RestResponse*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////.html) |  |  
| [*RestService*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////////.html) | API Forwarding - Backwards Compatibility |  
| | API Paths and Versions |  
| | Mounting Request Handlers |  
| | OpenAPI |  
| [*Restful*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice/////////////////////////////////////////////////////////////.html) | Serialization |  
| | Ownership |  
| | Request Cycle |  
| [*RuntimeFieldType*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RuntimeProtoGenerator*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RuntimeProtoGenerator.ClassNameComparator*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RuntimeProtoGenerator.EnumObj*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RuntimeProtoGenerator.Message*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*RuntimeSchemaType*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*SchemaCopier*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*SetSerializer*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*StringSerializer*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////////////////////////////////////////////////////////.html) |  |  
| [*UUID*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice///////////////////////////////////////////////////////////////////////////.html) |  |  
| [*WicketWebApplication*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.microservice//////////////////////////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
