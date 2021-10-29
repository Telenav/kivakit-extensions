////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.primitive.collections.array.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.CharCollection;
import com.telenav.kivakit.primitive.collections.array.PrimitiveSplitArray;
import com.telenav.kivakit.primitive.collections.list.CharList;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveSplitArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

/**
 * A split primitive array of char values. A split array has one key (an index) so it is one-dimensional, although it
 * has an array of children each of which is a primitive array. This is a sort of sparse array that tends to perform
 * well when values cluster by index (which they tend to do with map data identifiers). In addition, allocation of child
 * arrays is quick and memory efficient versus trying to manage one very large array. This design also works around the
 * 2GB limitation of Java arrays (which can only be indexed by int values).
 * <p>
 * Supports the operations of {@link CharCollection}, with the exception of {@link #clear()}. Indexing operations in
 * {@link CharList} are supported just as in {@link CharArray}, but the values are distributed across an array of child
 * {@link CharArray} objects.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveSplitArray
 * @see CharList
 * @see CharArray
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSplitArray.class)
public final class SplitCharArray extends PrimitiveSplitArray implements CharList
{
    /** The child arrays */
    private CharArray[] children;

    /** The index at which adding takes place */
    private int cursor;

    private int childSize;

    public SplitCharArray(String objectName)
    {
        super(objectName);
    }

    private SplitCharArray()
    {
    }

    /**
     * Adds a value, advancing the add cursor
     */
    @Override
    public boolean add(char value)
    {
        assert ensureHasRoomFor(1);
        set(cursor++, value);
        return true;
    }

    @Override
    public Count capacity()
    {
        var capacity = 0;
        for (var child : children)
        {
            if (child != null)
            {
                capacity += child.capacity().asInt();
            }
        }
        return Count.count(capacity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof SplitCharArray)
        {
            var that = (SplitCharArray) object;
            return size() == that.size() && iterator().identical(that.iterator());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char get(int index)
    {
        var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            var child = children[childIndex];
            if (child != null)
            {
                return child.get(index % childSize);
            }
        }
        return childArray(childIndex).get(index % childSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return iterator().hashValue();
    }

    @Override
    public CompressibleCollection.Method onCompress(CompressibleCollection.Method method)
    {
        // Go through our children,
        for (var child : children)
        {
            // and if the child is not null (this is a sparse array)
            if (child != null)
            {
                // then compress the child array.
                child.compress(method);
            }
        }

        return CompressibleCollection.Method.RESIZE;
    }

    @Override
    public void onInitialize()
    {
        super.onInitialize();
        childSize = initialChildSizeAsInt();
        children = new CharArray[initialChildCountAsInt()];
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);
        children = kryo.readObject(input, CharArray[].class);
        childSize = initialChildSizeAsInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char safeGet(int index)
    {
        var childIndex = index / childSize;
        if (childIndex < children.length)
        {
            var child = children[childIndex];
            if (child != null)
            {
                return child.safeGet(index % childSize);
            }
        }
        return childArray(childIndex).safeGet(index % childSize);
    }

    @Override
    public long safeGetPrimitive(int index)
    {
        return safeGet(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(int index, char value)
    {
        // Set the value into the array for the index
        int childIndex = index / childSize;
        childArray(childIndex).set(index % childSize, value);

        // then increase the size if we wrote past the end.
        var size = index + 1;
        if (size > size())
        {
            size(size);
        }
    }

    @Override
    public void setPrimitive(int index, long value)
    {
        set(index, (char) value);
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(index -> Long.toString(get(index)));
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);
        kryo.writeObject(output, children);
    }

    /**
     * @return The child array for the given index
     */
    private CharArray childArray(int childIndex)
    {
        // and if it's beyond the length of the children array,
        if (childIndex >= children.length)
        {
            // resize the children array to double the size,
            children = Arrays.copyOf(children, childIndex * 2);
        }

        // then get the child array,
        var array = children[childIndex];

        // and if it's null,
        if (array == null)
        {
            // create a new child
            array = new CharArray(objectName() + ".child[" + childIndex + "]");
            array.copyConfiguration(this);
            array.initialSize(initialChildSizeAsInt());
            array.maximumSize(maximumChildSizeAsInt());
            array.initialize();

            // and add it to the children array.
            children[childIndex] = array;
        }

        return array;
    }
}
