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

# kivakit-web &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/world-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/world-32-2x.png 2x"/>

This module contains modules that relate to the world-wide web.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module provides an easy and consistent API for running embedded [Jetty](https://www.eclipse.org/jetty/)
with [Apache Wicket](https://wicket.apache.org), [Jersey](https://eclipse-ee4j.github.io/jersey/) and [Swagger](https://swagger.io) web
applications. For example:

    var application = new MyRestApplication();
    listenTo(new JettyServer())
        .port(8080)
        .add("/*",          new JettyWicket(MyWebApplication.class))
        .add("/open-api/*", new JettySwaggerOpenApi(application))
        .add("/docs/*",     new JettySwaggerIndex(port))
        .add("/webapp/*",   new JettySwaggerStaticResources())
        .add("/webjar/*",   new JettySwaggerWebJar(application))
        .add("/*",          new JettyJersey(application))
        .start();

In this example, [Jetty](https://www.eclipse.org/jetty/)   server is configured to run on port 8080. Six calls to *add()*
then install KivaKit adapters, which simplify configuration of different web resources.
The first *add* call establishes [Apache Wicket](https://wicket.apache.org) as a servlet filter. Wicket will handle
only requests that are relevant to it. Any remaining requests are then handled
by [Swagger](https://swagger.io) and [Jersey](https://eclipse-ee4j.github.io/jersey/) adapters. Swagger documentation resources are
mounted
on */open-api*, */docs*, */webapp* and */webjar*, and any requests that are not handled
by Wicket *or* Swagger are then handled by Jersey. The final call to *start()* causes the
Jetty web server to start running. The *listenTo()* call directs operational messages
to the listener, in this case, probably a *BaseRepeater*. See [core-kernel messaging](../../kivakit/kivakit-core/documentation/messaging.md)
for more details.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (end-user-text)

### Sub-Projects <a name = "projects"></a> &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-32-2x.png 2x"/>

[**kivakit-web-jersey**](jersey/README.md)  
[**kivakit-web-jetty**](jetty/README.md)  
[**kivakit-web-swagger**](swagger/README.md)  
[**kivakit-web-wicket**](wicket/README.md)  

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name = "code-quality"></a> &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

&nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-80-96-2x.png 2x"/> &nbsp; &nbsp; [**kivakit-web-jersey**](jersey/README.md)  
&nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-80-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-80-96-2x.png 2x"/> &nbsp; &nbsp; [**kivakit-web-jetty**](jetty/README.md)  
&nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-70-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-70-96-2x.png 2x"/> &nbsp; &nbsp; [**kivakit-web-swagger**](swagger/README.md)  
&nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-70-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-70-96-2x.png 2x"/> &nbsp; &nbsp; [**kivakit-web-wicket**](wicket/README.md)

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://www.lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
