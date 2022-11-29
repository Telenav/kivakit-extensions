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

# kivakit-web-swagger &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

This project contains support for using Swagger with Jersey and Jetty.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.8.6/lexakai/kivakit-extensions/kivakit-web/swagger/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-web-swagger</artifactId>
        <version>1.8.6</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

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

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 66.7%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-70-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-70-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 100.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-100-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-100-96-2x.png 2x"/>     |
| Testing       | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>       |
| Documentation | 100.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-100-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-100-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

None

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.web.swagger*](https://www.kivakit.org/1.8.6/lexakai/kivakit-extensions/kivakit-web/swagger/documentation/diagrams/com.telenav.kivakit.web.swagger.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*SwaggerAssetsJettyPlugin*](https://www.kivakit.org/1.8.6/javadoc/kivakit-extensions/kivakit-web-swagger/com/telenav/kivakit/web/swagger/SwaggerAssetsJettyPlugin.html) |  |  
| [*SwaggerIndexJettyPlugin*](https://www.kivakit.org/1.8.6/javadoc/kivakit-extensions/kivakit-web-swagger/com/telenav/kivakit/web/swagger/SwaggerIndexJettyPlugin.html) |  |  
| [*SwaggerIndexJettyPlugin.IndexServlet*](https://www.kivakit.org/1.8.6/javadoc/kivakit-extensions/kivakit-web-swagger/com/telenav/kivakit/web/swagger/SwaggerIndexJettyPlugin.IndexServlet.html) |  |  
| [*SwaggerOpenApiJettyPlugin*](https://www.kivakit.org/1.8.6/javadoc/kivakit-extensions/kivakit-web-swagger/com/telenav/kivakit/web/swagger/SwaggerOpenApiJettyPlugin.html) |  |  
| [*SwaggerWebJarAssetJettyPlugin*](https://www.kivakit.org/1.8.6/javadoc/kivakit-extensions/kivakit-web-swagger/com/telenav/kivakit/web/swagger/SwaggerWebJarAssetJettyPlugin.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
