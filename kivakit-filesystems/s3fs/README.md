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

# kivakit-filesystems-s3fs &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/disks-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/disks-32-2x.png 2x"/>

Service provider for AWS S3 filesystems.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Example**](#example)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.11.0/lexakai/kivakit-extensions/kivakit-filesystems/s3fs/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-filesystems-s3fs</artifactId>
        <version>1.11.0</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module is a filesystem service provider, giving access to S3 filesystem data under the [*kivakit-resource*](../../kivakit/resource/README.md) mini-framework. This service provider will be discovered using the Java service loader mechanism. All that is required to make use of S3 paths with *File* and *Folder* objects is to:

1. Include the dependency above in *pom.xml*
2. Import *kivakit-filesystems-s3fs* in *module-info.java*
3. Set the AWS environment variables *AWS_ACCESS_KEY_ID* and *AWS_SECRET_ACCESS_KEY* to allow authentication with S3
4. Make use of S3 paths in files and folders, for example:

### Example <a name = "example"></a>

For example, this code:

    var file = File.parse("s3://us-west-1/my-bucket/my-file.txt")

would access the file *my-file.txt* in the bucket *my-bucket* in the *us-west-1* region on S3. To use the default region specified using the environment variable *AWS_DEFAULT_REGION*, the region "default-region" can be specified, as in:

    var file = File.parse("s3://default-region/my-bucket/my-file.txt")

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 0.0%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>     |
| Testing       | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>       |
| Documentation | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

[*S3 Service Provider*](https://www.kivakit.org/1.11.0/lexakai/kivakit-extensions/kivakit-filesystems/s3fs/documentation/diagrams/diagram-s3.svg)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.filesystems.s3fs*](https://www.kivakit.org/1.11.0/lexakai/kivakit-extensions/kivakit-filesystems/s3fs/documentation/diagrams/com.telenav.kivakit.filesystems.s3fs.svg)  
[*com.telenav.kivakit.filesystems.s3fs.internal.lexakai*](https://www.kivakit.org/1.11.0/lexakai/kivakit-extensions/kivakit-filesystems/s3fs/documentation/diagrams/com.telenav.kivakit.filesystems.s3fs.internal.lexakai.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*DiagramS3*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/internal/lexakai/DiagramS3.html) |  |  
| [*S3File*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/S3File.html) |  |  
| [*S3FileSystemObject*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/S3FileSystemObject.html) |  |  
| [*S3FileSystemService*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/S3FileSystemService.html) |  |  
| [*S3Folder*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/S3Folder.html) |  |  
| [*S3Output*](https://www.kivakit.org/1.11.0/javadoc/kivakit-extensions/kivakit-filesystems-s3fs/com/telenav/kivakit/filesystems/s3fs/S3Output.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
