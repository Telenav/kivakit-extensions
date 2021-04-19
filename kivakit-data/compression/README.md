# kivakit-data compression &nbsp;&nbsp; <img src="https://www.lexakai.org/images/compress-52.png" srcset="https://www.lexakai.org/images/compress-52-2x.png 2x"/>

This module contains packages for compressing and decompressing data.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Codecs**](#codecs)  
[**Types of Codecs**](#types-of-codecs)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/dependencies-32.png" srcset="https://www.lexakai.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-data-compression</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module contains a definition of a compressor / decompressor, otherwise known as a *Codec*.
The design of this codec mini-framework is centered around direct access to byte buffers (*ByteList*s)
for optimal efficiency. For other purposes, existing stream-oriented compressors already exist,
including the support for ZIP format in the JDK. This module also contains an implementation of
a fast (table-driven) [*Huffman*](https://en.wikipedia.org/wiki/Huffman_coding) codec.

### Codecs <a name = "codecs"></a>

The *Codec* interface is generic to any compression type and any symbol type and looks like this:

    public interface Codec<Symbol>
    {
        boolean canEncode(Symbol symbol)
        ByteList encode(ByteList output, SymbolProducer<Symbol> producer)
        void decode(ByteList input, SymbolConsumer<Symbol> consumer)
    }

The *encode()* method performs huffman coding of the symbols produced by the given *SymbolProducer*,
and writes the compressed representation to the given *ByteList*. The *decode()* method takes a *ByteList*,
and decodes the compressed data, calling a *SymbolConsumer* with each decoded symbol.

### Types of Codecs <a name = "character"></a>

The *CharacterCodec*, *StringCodec* and *StringListCodec* interfaces provide a definition of compression
by character, string and string list, respectively. The same underlying *Huffman* coder is used to implement
all three.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.lexakai.org/images/diagram-32.png" srcset="https://www.lexakai.org/images/diagram-32-2x.png 2x"/>

None

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/box-32.png" srcset="https://www.lexakai.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.data.compression*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.svg)  
[*com.telenav.kivakit.data.compression.codecs*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.character*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.character.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.list*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.list.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.string*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.string.svg)  
[*com.telenav.kivakit.data.compression.codecs.huffman.tree*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.codecs.huffman.tree.svg)  
[*com.telenav.kivakit.data.compression.project*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/compression/documentation/diagrams/com.telenav.kivakit.data.compression.project.svg)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/books-32.png" srcset="https://www.lexakai.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 74.5%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-70-12.png)

The following significant classes are undocumented:  

- FastHuffmanDecoder.Table.Entry

| Class | Documentation Sections |
|---|---|
| [*CharacterCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/CharacterCodec.html) |  |  
| [*CharacterFrequencies*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/CharacterFrequencies.html) |  |  
| [*Code*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Code.html) |  |  
| [*Codec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/Codec.html) |  |  
| [*CodedSymbol*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/CodedSymbol.html) |  |  
| [*DataCompressionKryoTypes*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionKryoTypes.html) |  |  
| [*DataCompressionProject*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionProject.html) |  |  
| [*DataCompressionUnitTest*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/project/DataCompressionUnitTest.html) |  |  
| [*FastHuffmanDecoder*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.html) |  |  
| [*FastHuffmanDecoder.Table*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.Table.html) |  |  
| [*FastHuffmanDecoder.Table.Entry*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/FastHuffmanDecoder.Table.Entry.html) |  |  
| [*HuffmanCharacterCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/HuffmanCharacterCodec.html) |  |  
| [*HuffmanCharacterCodec.Converter*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/character/HuffmanCharacterCodec.Converter.html) |  |  
| [*HuffmanCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/HuffmanCodec.html) |  |  
| [*HuffmanStringCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/HuffmanStringCodec.html) |  |  
| [*HuffmanStringCodec.Converter*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/HuffmanStringCodec.Converter.html) |  |  
| [*HuffmanStringListCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/list/HuffmanStringListCodec.html) |  |  
| [*Leaf*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Leaf.html) |  |  
| [*Node*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Node.html) |  |  
| [*StringCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/StringCodec.html) |  |  
| [*StringFrequencies*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/string/StringFrequencies.html) |  |  
| [*StringListCodec*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/StringListCodec.html) |  |  
| [*SymbolConsumer*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolConsumer.html) |  |  
| [*SymbolConsumer.Directive*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolConsumer.Directive.html) |  |  
| [*SymbolProducer*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/SymbolProducer.html) |  |  
| [*Symbols*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Symbols.html) |  |  
| [*Tree*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.compression/com/telenav/kivakit/data/compression/codecs/huffman/tree/Tree.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

