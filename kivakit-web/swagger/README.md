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

# kivakit-web-swagger &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

This project contains support for using Swagger with Jersey and Jetty.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.1.2/lexakai/kivakit-extensions/kivakit-web/swagger/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-web-swagger</artifactId>
        <version>1.1.2</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

[Swagger](https://swagger.io) OpenAPI documentation can be set up on embedded [Jetty](https://www.eclipse.org/jetty/) with just a few lines
of code:

    var application = new ServiceRegistryRestApplication();
    listenTo(new JettyServer())
        .port(8080)
        .add("/open-api/*", new JettySwaggerOpenApi(application))
        .add("/docs/*", new JettySwaggerIndex(port))
        .add("/webapp/*", new JettySwaggerStaticResources())
        .add("/webjar/*", new JettySwaggerWebJar(application))
        .start();

Other servlets and filters, for example [Jersey](../jersey/README.md) and [Wicket](../wicket/README.md), can be added in a similar way.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

None

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.web.swagger*](https://www.kivakit.org/1.1.2/lexakai/kivakit-extensions/kivakit-web/swagger/documentation/diagrams/com.telenav.kivakit.web.swagger.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 75.0%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-80-96.png" srcset="https://www.kivakit.org/images/meter-80-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*JettySwaggerProject*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/JettySwaggerProject.html) |  |  
| [*SwaggerAssetsJettyResourcePlugin*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/SwaggerAssetsJettyResourcePlugin.html) |  |  
| [*SwaggerIndexJettyResourcePlugin*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/SwaggerIndexJettyResourcePlugin.html) |  |  
| [*SwaggerIndexJettyResourcePlugin.IndexServlet*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/SwaggerIndexJettyResourcePlugin.IndexServlet.html) |  |  
| [*SwaggerJettyServletPlugin*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/SwaggerJettyServletPlugin.html) |  |  
| [*SwaggerWebJarJettyResourcePlugin*](https://www.kivakit.org/1.1.2/javadoc/kivakit-extensions/kivakit.web.swagger/com/telenav/kivakit/web/swagger/SwaggerWebJarJettyResourcePlugin.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

