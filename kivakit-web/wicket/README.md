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

# kivakit-web-wicket &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/images/icons/wicket-32.png" srcset="https://telenav.github.io/telenav-assets/images/images/icons/wicket-32-2x.png 2x"/>

This project contains support for using Apache Wicket on Jetty.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-web-wicket</artifactId>
        <version>1.6.1</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module makes it easy to configure and start a simple [Apache Wicket](https://wicket.apache.org) application:

    listenTo(new JettyServer())
            .port(8080)
            .add("/*", new JettyWicket(MyWebApplication.class))
            .start();

Other servlets and filters, for example [Swagger](../swagger/README.md) and [Jersey](../jersey/README.md), can be added in a similar way.

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

None

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.web.wicket*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.svg)  
[*com.telenav.kivakit.web.wicket.behaviors.status*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.behaviors.status.svg)  
[*com.telenav.kivakit.web.wicket.components.feedback*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.feedback.svg)  
[*com.telenav.kivakit.web.wicket.components.header*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.header.svg)  
[*com.telenav.kivakit.web.wicket.components.refresh*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.components.refresh.svg)  
[*com.telenav.kivakit.web.wicket.library*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.library.svg)  
[*com.telenav.kivakit.web.wicket.theme*](https://www.kivakit.org/1.6.1/lexakai/kivakit-extensions/kivakit-web/wicket/documentation/diagrams/com.telenav.kivakit.web.wicket.theme.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

Javadoc coverage for this project is 85.7%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-90-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-90-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*Components*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket//////////////////////////////////////////////////.html) |  |  
| [*FeedbackPanel*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket/////////////////////////////////////////////////////////////////.html) |  |  
| [*HeaderPanel*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket/////////////////////////////////////////////////////////////.html) |  |  
| [*JettyWicketFilterHolder*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket///////////////////////////////////////////////////////.html) |  |  
| [*KivaKitTheme*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket//////////////////////////////////////////////////.html) |  |  
| [*MessageColor*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket/////////////////////////////////////////////////////////////.html) |  |  
| [*UpdatingContainer*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket////////////////////////////////////////////////////////////////////.html) |  |  
| [*WicketJettyPlugin*](https://www.kivakit.org/1.6.1/javadoc/kivakit-extensions/kivakit.web.wicket/////////////////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
