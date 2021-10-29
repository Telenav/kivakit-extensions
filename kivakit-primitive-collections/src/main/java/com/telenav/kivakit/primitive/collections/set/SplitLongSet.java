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

package com.telenav.kivakit.primitive.collections.set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.iteration.LongIterable;
import com.telenav.kivakit.primitive.collections.iteration.LongIterator;
import com.telenav.kivakit.primitive.collections.map.PrimitiveMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveSet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A set of primitive long values. Supports typical set functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #add(long)} </li>
 *     <li>{@link #contains(long)}</li>
 *     <li>{@link #remove(long)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Values</b>
 * <ul>
 *     <li>{@link #values()}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveSet
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveSet.class)
public final class SplitLongSet extends PrimitiveSet implements LongIterable
{
    /** The child sets */
    private LongSet[] children;

    public SplitLongSet(String objectName)
    {
        super(objectName);
    }

    private SplitLongSet()
    {
    }

    /**
     * Stores the given value under the given value
     */
    @Override
    public boolean add(long value)
    {
        if (set(value).add(value))
        {
            incrementSize();
            return true;
        }
        return false;
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

    @Override
    public void clear()
    {
        super.clear();
        initialize();
    }

    /**
     * @return True if this set contains the value
     */
    @Override
    public boolean contains(long value)
    {
        if (isEmpty())
        {
            return false;
        }
        else
        {
            return set(value).contains(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof SplitLongSet)
        {
            var that = (SplitLongSet) object;
            if (size() != that.size())
            {
                return false;
            }
            var values = values();
            while (values.hasNext())
            {
                if (!that.contains(values.next()))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return values().hash();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LongIterator iterator()
    {
        return values();
    }

    @Override
    public Method onCompress(Method method)
    {
        for (var child : children)
        {
            if (child != null)
            {
                child.compress(method);
            }
        }
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize()
    {
        super.onInitialize();
        children = new LongSet[initialChildCountAsInt()];
    }

    /**
     * @see KryoSerializable
     */
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);
        children = kryo.readObject(input, LongSet[].class);
    }

    /**
     * Removes the given value from this set
     *
     * @return True if the value was removed and false if it could not be found
     */
    public boolean remove(long value)
    {
        if (set(value).remove(value))
        {
            decreaseSize(1);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(values(), ", ", 10, "\n", Long::toString) + "]";
    }

    /**
     * @return The values in this map in an undefined order
     */
    public LongIterator values()
    {
        var outer = this;
        return new LongIterator()
        {
            private int childIndex;

            private LongIterator values;

            @Override
            public boolean hasNext()
            {
                if (values != null && values.hasNext())
                {
                    return true;
                }
                values = null;
                while (values == null && childIndex < outer.children.length)
                {
                    var next = outer.children[childIndex++];
                    if (next != null && !next.isEmpty())
                    {
                        values = next.values();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public long next()
            {
                return values.next();
            }
        };
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

    @Override
    protected void copyEntries(PrimitiveMap that, ProgressReporter reporter)
    {
        unsupported();
    }

    @Override
    protected PrimitiveMap newMap()
    {
        return unsupported();
    }

    /**
     * @return The submap for the given value
     */
    private LongSet set(long value)
    {
        // Get the child index
        var childIndex = hash(value) % children.length;

        // and if there's no child
        var child = children[childIndex];
        if (child == null)
        {
            // create one
            child = new LongSet(objectName() + ".child[" + childIndex + "]");
            child.initialSize(initialChildSizeAsInt());
            child.maximumSize(Integer.MAX_VALUE);
            child.initialize();

            // and install it in the children array.
            children[childIndex] = child;
        }
        return child;
    }
}
