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

# kivakit-filesystems github &nbsp;&nbsp; <img src="https://www.kivakit.org/images/disks-32.png" srcset="https://www.kivakit.org/images/disks-32-2x.png 2x"/>

Read only filesystem service provider for GitHub.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Example**](#example)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.1.2-SNAPSHOT/lexakai/kivakit-extensions/kivakit-filesystems/github/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-filesystems-github</artifactId>
        <version>1.1.2-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

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

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

None

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.filesystems.github*](https://www.kivakit.org/1.1.2-SNAPSHOT/lexakai/kivakit-extensions/kivakit-filesystems/github/documentation/diagrams/com.telenav.kivakit.filesystems.github.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 75.1%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-80-96.png" srcset="https://www.kivakit.org/images/meter-80-96-2x.png 2x"/>


The following significant classes are undocumented:  

- GitHubTree

| Class | Documentation Sections |
|---|---|
| [*GitHubFile*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFile.html) |  |  
| [*GitHubFileSystemObject*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFileSystemObject.html) |  |  
| [*GitHubFileSystemProject*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFileSystemProject.html) |  |  
| [*GitHubFileSystemService*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFileSystemService.html) |  |  
| [*GitHubFolder*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubFolder.html) |  |  
| [*GitHubTree*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubTree.html) |  |  
| [*GitHubTree.EntryType*](https://www.kivakit.org/1.1.2-SNAPSHOT/javadoc/kivakit-extensions/kivakit.filesystems.github/com/telenav/kivakit/filesystems/github/GitHubTree.EntryType.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

