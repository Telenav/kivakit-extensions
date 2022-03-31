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

# kivakit-logs-email &nbsp;&nbsp; <img src="https://www.kivakit.org/images/envelope-32.png" srcset="https://www.kivakit.org/images/envelope-32-2x.png 2x"/>

This module is an email log service provider.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Including the Provider**](#including-the-provider)  
[**Log Configuration Parameters**](#log-configuration-parameters)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.5.0/lexakai/kivakit-extensions/kivakit-logs/email/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-logs-email</artifactId>
        <version>1.5.0</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module is a log service provider which logs messages by sending emails. A typical use for this is to send emails about high severity messages to one or more email addresses.

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Including the Provider <a name = "including-the-provider"></a>

To include the *EmailLog* service provider, all that is required is to:

1. Include the dependency above in *pom.xml*
2. Import *kivakit-logs-email* in *module-info.java*
3. Configure logging from the command line:

       java -DKIVAKIT_LOG="Console,Email level=CriticalAlert subject=Alert \
           from=jonathanl@telenav.com to=jonathanl@telenav.com \
           host=smtp.telenav.com username=jonathanl@telenav.com password=shibo"

More details about logging are available at [*kivakit-core logging*](../../kivakit/kernel/documentation/logging.md).

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Log Configuration Parameters <a name = "log-configuration-parameters"></a>

* *subject* - subject of the email
* *from* - email address
* *to* - comma separated list of email addresses
* *host* - SMTP host
* *username* - username on the host
* *password* - password to send email

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Email Log*](https://www.kivakit.org/1.5.0/lexakai/kivakit-extensions/kivakit-logs/email/documentation/diagrams/diagram-logs-email.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.logs.email*](https://www.kivakit.org/1.5.0/lexakai/kivakit-extensions/kivakit-logs/email/documentation/diagrams/com.telenav.kivakit.logs.email.svg)  
[*com.telenav.kivakit.logs.email.lexakai*](https://www.kivakit.org/1.5.0/lexakai/kivakit-extensions/kivakit-logs/email/documentation/diagrams/com.telenav.kivakit.logs.email.lexakai.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 41.7%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-40-96.png" srcset="https://www.kivakit.org/images/meter-40-96-2x.png 2x"/>




| Class | Documentation Sections |
|---|---|
| [*DiagramLogsEmail*](https://www.kivakit.org/1.5.0/javadoc/kivakit-extensions/kivakit.logs.email/com/telenav/kivakit/logs/email/lexakai/DiagramLogsEmail.html) |  |  
| [*EmailLog*](https://www.kivakit.org/1.5.0/javadoc/kivakit-extensions/kivakit.logs.email/com/telenav/kivakit/logs/email/EmailLog.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

