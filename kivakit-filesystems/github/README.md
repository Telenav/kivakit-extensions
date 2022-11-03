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

# kivakit-filesystems-github &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/disks-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/disks-32-2x.png 2x"/>

Read only filesystem service provider for GitHub.

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Example**](#example)  

[**Dependencies**](#dependencies) | [**Code Quality**](#code-quality) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/dependencies-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-filesystems/github/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-filesystems-github</artifactId>
        <version>1.8.2</version>
    </dependency>

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module is a filesystem service provider, giving read-only access to GitHub files and folders under the [*kivakit-resource*](../../kivakit/resource/README.md) mini-framework. This service provider will be discovered using the Java service loader mechanism. All that is required to make use of GitHub paths with *File* and *Folder* objects is to:

1. Include the dependency above in *pom.xml*
2. Import *kivakit-filesystems-github* in *module-info.java*
3. Make use of GitHub paths in files and folders

### Example <a name = "example"></a>

For example, this code:

    var file = File.parse("github://Telenav/kivakit/develop/README.md")

would access the file *README.txt* in the *develop* branch of the repository *kivakit* belonging to the user *Telenav*.

If an access token is required for a private GitHub repository, it can be specified like this:

    var file = File.parse("github://jonathanlocke/access-token/[token]/code/develop/README.md");
    
where [token] is a GitHub access token for the user jonathanlocke.

[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Code Quality <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/ruler-32.png" srcset="https://telenav.github.io/telenav-assets/images/icons/ruler-32-2x.png 2x"/>

Code quality for this project is 5.6%.  
  
&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-10-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-10-96-2x.png 2x"/>

| Measurement   | Value                    |
|---------------|--------------------------|
| Stability     | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>     |
| Testing       | 0.0%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-0-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-0-96-2x.png 2x"/>       |
| Documentation | 16.7%&nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/meters/meter-20-96.png" srcset="https://telenav.github.io/telenav-assets/images/meters/meter-20-96-2x.png 2x"/> |

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/diagram-40.png" srcset="https://telenav.github.io/telenav-assets/images/icons/diagram-40-2x.png 2x"/>

None

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/box-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/box-24-2x.png 2x"/>

[*com.telenav.kivakit.filesystems.github*](https://www.kivakit.org/1.8.2/lexakai/kivakit-extensions/kivakit-filesystems/github/documentation/diagrams/com.telenav.kivakit.filesystems.github.svg)

### Javadoc <a name="code-quality"></a> &nbsp;&nbsp; <img src="https://telenav.github.io/telenav-assets/images/icons/books-24.png" srcset="https://telenav.github.io/telenav-assets/images/icons/books-24-2x.png 2x"/>

| Class | Documentation Sections  |
|-------|-------------------------|
| [*GitHubFile*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFile.html) |  |  
| [*GitHubFileSystemObject*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFileSystemObject.html) |  |  
| [*GitHubFileSystemService*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFileSystemService.html) |  |  
| [*GitHubFolder*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFolder.html) |  |  
| [*GitHubTree*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubTree.html) |  |  
| [*GitHubTree.EntryType*](https://www.kivakit.org/1.8.2/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubTree.EntryType.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512.png" srcset="https://telenav.github.io/telenav-assets/images/separators/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>
