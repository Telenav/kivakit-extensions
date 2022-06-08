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

# kivakit-logs-file &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons//log-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons//log-32-2x.png 2x"/>

This module is a text file log service provider.

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Including the Provider**](#including-the-provider)  
[**Log Configuration Parameters**](#log-configuration-parameters)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.6.0/lexakai/kivakit-extensions/kivakit-logs/file/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-logs-file</artifactId>
        <version>1.6.0</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module is a log service provider which logs messages to the filesystem. It includes log roll-over, based on time or log size.

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Including the Provider <a name = "including-the-provider"></a>

To include the *EmailLog* service provider, all that is required is to:

1. Include the dependency above in *pom.xml*
2. Import *kivakit-logs-file* in *module-info.java*
3. Configure logging from the command line:

       java -DKIVAKIT_LOG="Console,File level=Warning file=~/log.txt rollover=daily maximum-size=100M

More details about logging are available at [*kivakit-core logging*](../../kivakit/kernel/documentation/logging.md).

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Log Configuration Parameters <a name = "log-configuration-parameters"></a>

* *file* - path to file
* *rollover* - when to rollover to a new file (none, hourly or daily, default is none)
* *maximum-size* - maximum size of log before it rolls over (default is "50M")

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*File Logs*](https://www.kivakit.org/1.6.0/lexakai/kivakit-extensions/kivakit-logs/file/documentation/diagrams/diagram-logs-file.svg)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-32-2x.png 2x"/>

[*com.telenav.kivakit.logs.file*](https://www.kivakit.org/1.6.0/lexakai/kivakit-extensions/kivakit-logs/file/documentation/diagrams/com.telenav.kivakit.logs.file.svg)  
[*com.telenav.kivakit.logs.file.lexakai*](https://www.kivakit.org/1.6.0/lexakai/kivakit-extensions/kivakit-logs/file/documentation/diagrams/com.telenav.kivakit.logs.file.lexakai.svg)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

Javadoc coverage for this project is 69.3%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/meter-70-96.png" srcset="https://telenav.github.io/telenav-assets/meter-70-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*BaseRolloverTextLog*](https://www.kivakit.org/1.6.0/javadoc/kivakit-extensions/kivakit.logs.file//////////////////////////////////////////////////.html) |  |  
| [*BaseRolloverTextLog.Rollover*](https://www.kivakit.org/1.6.0/javadoc/kivakit-extensions/kivakit.logs.file///////////////////////////////////////////////////////////.html) |  |  
| [*DiagramLogsFile*](https://www.kivakit.org/1.6.0/javadoc/kivakit-extensions/kivakit.logs.file//////////////////////////////////////////////////////.html) |  |  
| [*FileLog*](https://www.kivakit.org/1.6.0/javadoc/kivakit-extensions/kivakit.logs.file//////////////////////////////////////.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/icons/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
