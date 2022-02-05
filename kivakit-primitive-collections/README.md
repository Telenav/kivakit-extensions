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

# kivakit-primitive-collections &nbsp;&nbsp; <img src="https://www.kivakit.org/images/set-32.png" srcset="https://www.kivakit.org/images/set-32-2x.png 2x"/>

The primitive collections store data in primitive data structures.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/dependencies-32.png" srcset="https://www.kivakit.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-primitive-collections</artifactId>
        <version>1.3.1-SNAPSHOT</version>
    </dependency>

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

The *primitive* package contains an extensive set of classes for storing data in primitive data structures, including:

- Dynamic primitive arrays (1 and 2 dimensional)
- Primitive lists, maps, sets and multi-maps
- Bit arrays and bitwise I/O
- Bit-packed arrays
- *Split* versions of many data structures which virtualize several primitive data structures into a single
  large one. This can be beneficial in keeping object allocation and garbage collection under control.

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.kivakit.org/images/diagram-40.png" srcset="https://www.kivakit.org/images/diagram-40-2x.png 2x"/>

[*Primitive Arrays*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-array.svg)  
[*Primitive Bit I/O*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-array-bit-io.svg)  
[*Primitive Collections*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-collection.svg)  
[*Primitive Lists*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-list.svg)  
[*Primitive Maps*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-map.svg)  
[*Primitive Multi-Maps*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-multi-map.svg)  
[*Primitive Sets*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-set.svg)  
[*Split Primitive Arrays*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-split-array.svg)  
[*Two-Dimensional Arrays*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/diagram-primitive-array-array.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/box-32.png" srcset="https://www.kivakit.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.primitive.collections*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.svg)  
[*com.telenav.kivakit.primitive.collections.array*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.svg)  
[*com.telenav.kivakit.primitive.collections.array.arrays*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.arrays.svg)  
[*com.telenav.kivakit.primitive.collections.array.bits*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.bits.svg)  
[*com.telenav.kivakit.primitive.collections.array.bits.io*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.bits.io.svg)  
[*com.telenav.kivakit.primitive.collections.array.bits.io.input*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.bits.io.input.svg)  
[*com.telenav.kivakit.primitive.collections.array.bits.io.output*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.bits.io.output.svg)  
[*com.telenav.kivakit.primitive.collections.array.packed*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.packed.svg)  
[*com.telenav.kivakit.primitive.collections.array.scalars*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.scalars.svg)  
[*com.telenav.kivakit.primitive.collections.array.strings*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.array.strings.svg)  
[*com.telenav.kivakit.primitive.collections.iteration*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.iteration.svg)  
[*com.telenav.kivakit.primitive.collections.list*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.list.svg)  
[*com.telenav.kivakit.primitive.collections.list.adapters*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.list.adapters.svg)  
[*com.telenav.kivakit.primitive.collections.list.store*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.list.store.svg)  
[*com.telenav.kivakit.primitive.collections.map*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.svg)  
[*com.telenav.kivakit.primitive.collections.map.multi*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.multi.svg)  
[*com.telenav.kivakit.primitive.collections.map.multi.dynamic*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.multi.dynamic.svg)  
[*com.telenav.kivakit.primitive.collections.map.multi.fixed*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.multi.fixed.svg)  
[*com.telenav.kivakit.primitive.collections.map.objects*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.objects.svg)  
[*com.telenav.kivakit.primitive.collections.map.scalars*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.scalars.svg)  
[*com.telenav.kivakit.primitive.collections.map.split*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.map.split.svg)  
[*com.telenav.kivakit.primitive.collections.project*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.project.svg)  
[*com.telenav.kivakit.primitive.collections.set*](https://www.kivakit.org/1.3.1-SNAPSHOT/lexakai/kivakit-extensions/kivakit-primitive-collections/documentation/diagrams/com.telenav.kivakit.primitive.collections.set.svg)

<img src="https://www.kivakit.org/images/horizontal-line-128.png" srcset="https://www.kivakit.org/images/horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.kivakit.org/images/books-32.png" srcset="https://www.kivakit.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 67.5%.  
  
&nbsp; &nbsp; <img src="https://www.kivakit.org/images/meter-70-96.png" srcset="https://www.kivakit.org/images/meter-70-96-2x.png 2x"/>


The following significant classes are undocumented:  

- BaseBitReader  
- BaseBitWriter  
- BitArray  
- LongToIntMultiMap  
- LongToLongMultiMap  
- LongToObjectMap  
- PrimitiveMap

| Class | Documentation Sections |
|---|---|
| [*BaseBitReader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/input/BaseBitReader.html) |  |  
| [*BaseBitWriter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/output/BaseBitWriter.html) |  |  
| [*BigSplitPackedArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/packed/BigSplitPackedArray.html) |  |  
| [*BitArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/BitArray.html) |  |  
| [*BitInput*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/input/BitInput.html) |  |  
| [*BitOutput*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/output/BitOutput.html) |  |  
| [*BitReader*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/BitReader.html) |  |  
| [*BitWriter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/BitWriter.html) |  |  
| [*ByteArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/ByteArray.html) |  |  
| [*ByteArrayArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/arrays/ByteArrayArray.html) |  |  
| [*ByteCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/ByteCollection.html) |  |  
| [*ByteIterable*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/ByteIterable.html) |  |  
| [*ByteIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/ByteIterator.html) |  |  
| [*ByteList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/ByteList.html) |  |  
| [*CharArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/CharArray.html) |  |  
| [*CharArray.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/CharArray.Converter.html) |  |  
| [*CharCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/CharCollection.html) |  |  
| [*CharIterable*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/CharIterable.html) |  |  
| [*CharIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/CharIterator.html) |  |  
| [*CharList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/CharList.html) |  |  
| [*DefaultHashingStrategy*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/DefaultHashingStrategy.html) |  |  
| [*FixedSizeBitArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/FixedSizeBitArray.html) |  |  
| [*HashingStrategy*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/HashingStrategy.html) |  |  
| [*IntArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/IntArray.html) |  |  
| [*IntArray.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/IntArray.Converter.html) |  |  
| [*IntArrayArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/arrays/IntArrayArray.html) |  |  
| [*IntCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/IntCollection.html) |  |  
| [*IntIterable*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/IntIterable.html) |  |  
| [*IntIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/IntIterator.html) |  |  
| [*IntLinkedListStore*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/store/IntLinkedListStore.html) |  |  
| [*IntList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/IntList.html) |  |  
| [*IntListAdapter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/adapters/IntListAdapter.html) |  |  
| [*IntMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/IntMultiMap.html) |  |  
| [*IntToByteFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/IntToByteFixedMultiMap.html) |  |  
| [*IntToByteMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToByteMap.html) |  |  
| [*IntToByteMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToByteMap.EntryVisitor.html) |  |  
| [*IntToIntFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/IntToIntFixedMultiMap.html) |  |  
| [*IntToIntMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToIntMap.html) |  |  
| [*IntToIntMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToIntMap.EntryVisitor.html) |  |  
| [*IntToLongFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/IntToLongFixedMultiMap.html) |  |  
| [*IntToLongMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToLongMap.html) |  |  
| [*IntToLongMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/IntToLongMap.EntryVisitor.html) |  |  
| [*IntToPackedArrayFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/IntToPackedArrayFixedMultiMap.html) |  |  
| [*LongArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/LongArray.html) |  |  
| [*LongArray.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/LongArray.Converter.html) |  |  
| [*LongArrayArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/arrays/LongArrayArray.html) |  |  
| [*LongCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/LongCollection.html) |  |  
| [*LongIterable*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/LongIterable.html) |  |  
| [*LongIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/LongIterator.html) |  |  
| [*LongLinkedListStore*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/store/LongLinkedListStore.html) |  |  
| [*LongList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/LongList.html) |  |  
| [*LongMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/LongMultiMap.html) |  |  
| [*LongSet*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/set/LongSet.html) |  |  
| [*LongToByteFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/LongToByteFixedMultiMap.html) |  |  
| [*LongToByteMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToByteMap.html) |  |  
| [*LongToByteMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToByteMap.EntryVisitor.html) |  |  
| [*LongToIntFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/LongToIntFixedMultiMap.html) |  |  
| [*LongToIntMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToIntMap.html) |  |  
| [*LongToIntMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToIntMap.EntryVisitor.html) |  |  
| [*LongToIntMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/dynamic/LongToIntMultiMap.html) |  |  
| [*LongToLongFixedMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/fixed/LongToLongFixedMultiMap.html) |  |  
| [*LongToLongMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToLongMap.html) |  |  
| [*LongToLongMap.EntryVisitor*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/LongToLongMap.EntryVisitor.html) |  |  
| [*LongToLongMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/dynamic/LongToLongMultiMap.html) |  |  
| [*LongToObjectMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/objects/LongToObjectMap.html) |  |  
| [*PackedArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/packed/PackedArray.html) |  |  
| [*PackedPrimitiveArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/packed/PackedPrimitiveArray.html) |  |  
| [*PackedPrimitiveArray.OverflowHandling*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/packed/PackedPrimitiveArray.OverflowHandling.html) |  |  
| [*PackedStringArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/strings/PackedStringArray.html) |  |  
| [*PackedStringArray.Type*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/strings/PackedStringArray.Type.html) |  |  
| [*PackedStringStore*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/store/PackedStringStore.html) |  |  
| [*PrimitiveArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/PrimitiveArray.html) |  |  
| [*PrimitiveArrayArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/PrimitiveArrayArray.html) |  |  
| [*PrimitiveCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/PrimitiveCollection.html) |  |  
| [*PrimitiveCollection.AllocationStackTrace*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/PrimitiveCollection.AllocationStackTrace.html) |  |  
| [*PrimitiveCollection.CompressionRecord*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/PrimitiveCollection.CompressionRecord.html) |  |  
| [*PrimitiveCollection.IndexedToString*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/PrimitiveCollection.IndexedToString.html) |  |  
| [*PrimitiveCollectionsKryoTypes*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/project/PrimitiveCollectionsKryoTypes.html) |  |  
| [*PrimitiveCollectionsProject*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/PrimitiveCollectionsProject.html) |  |  
| [*PrimitiveCollectionsUnitTest*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/project/PrimitiveCollectionsUnitTest.html) |  |  
| [*PrimitiveIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/PrimitiveIterator.html) |  |  
| [*PrimitiveList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/PrimitiveList.html) |  |  
| [*PrimitiveListStore*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/store/PrimitiveListStore.html) |  |  
| [*PrimitiveMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/PrimitiveMap.html) |  |  
| [*PrimitiveMap.MapToString*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/PrimitiveMap.MapToString.html) |  |  
| [*PrimitiveMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/PrimitiveMultiMap.html) |  |  
| [*PrimitiveMultiMap.MultiMapToString*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/PrimitiveMultiMap.MultiMapToString.html) |  |  
| [*PrimitiveScalarMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/PrimitiveScalarMap.html) |  |  
| [*PrimitiveScalarMultiMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/multi/PrimitiveScalarMultiMap.html) |  |  
| [*PrimitiveSet*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/set/PrimitiveSet.html) |  |  
| [*PrimitiveSet.SetToString*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/set/PrimitiveSet.SetToString.html) |  |  
| [*PrimitiveSplitArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/PrimitiveSplitArray.html) |  |  
| [*ShortArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/ShortArray.html) |  |  
| [*ShortArray.Converter*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/ShortArray.Converter.html) |  |  
| [*ShortCollection*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/ShortCollection.html) |  |  
| [*ShortIterable*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/ShortIterable.html) |  |  
| [*ShortIterator*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/iteration/ShortIterator.html) |  |  
| [*ShortList*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/list/ShortList.html) |  |  
| [*SplitByteArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/SplitByteArray.html) |  |  
| [*SplitCharArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/SplitCharArray.html) |  |  
| [*SplitIntArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/SplitIntArray.html) |  |  
| [*SplitIntToIntMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/split/SplitIntToIntMap.html) |  |  
| [*SplitLongArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/scalars/SplitLongArray.html) |  |  
| [*SplitLongSet*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/set/SplitLongSet.html) |  |  
| [*SplitLongToByteMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/split/SplitLongToByteMap.html) |  |  
| [*SplitLongToIntMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/split/SplitLongToIntMap.html) |  |  
| [*SplitLongToLongMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/split/SplitLongToLongMap.html) |  |  
| [*SplitPackedArray*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/packed/SplitPackedArray.html) |  |  
| [*SplitPrimitiveMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/SplitPrimitiveMap.html) |  |  
| [*StringToIntMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/StringToIntMap.html) |  |  
| [*StringToObjectMap*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/map/scalars/StringToObjectMap.html) |  |  
| [*VariableReadSizeBitInput*](https://www.kivakit.org/1.3.1-SNAPSHOT/javadoc/kivakit-extensions/kivakit.primitive.collections/com/telenav/kivakit/primitive/collections/array/bits/io/input/VariableReadSizeBitInput.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](https://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://lexakai.org). UML diagrams courtesy of [PlantUML](https://plantuml.com).</sub>

