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
import com.telenav.kivakit.primitive.collections.LongCollection;
import com.telenav.kivakit.primitive.collections.iteration.LongIterator;
import com.telenav.kivakit.primitive.collections.map.PrimitiveMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveSet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;

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
public final class LongSet extends PrimitiveSet implements LongCollection
{
    /** The values */
    private long[] values;

    public LongSet(String name)
    {
        super(name);
    }

    private LongSet()
    {
    }

    /**
     * Adds the given value to this set
     */
    @Override
    public boolean add(long value)
    {
        assert !isNull(value);
        assert compressionMethod() != Method.FREEZE;

        if (ensureHasRoomFor(1))
        {
            // Get the index of any existing value (or the next null slot if it doesn't exist)
            var index = index(values, value);

            // If we couldn't find it
            if (isEmpty(values[index]))
            {
                // we can store it in the null slot
                values[index] = value;

                // increase the size of the map and possible resize
                increaseSize();
                return true;
            }
        }
        return false;
    }

    @Override
    public Count capacity()
    {
        return Count.count(values.length);
    }

    /**
     * Clears all values from this set
     */
    @Override
    public void clear()
    {
        super.clear();
        clear(values);
    }

    /**
     * @return True if this set contains the given value
     */
    @Override
    public boolean contains(long value)
    {
        return contains(values, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LongSet)
        {
            var that = (LongSet) object;
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
     * @return The values in this set
     */
    @Override
    public LongIterator iterator()
    {
        return nonEmptyValues(values);
    }

    @Override
    public Method onCompress(Method method)
    {
        if (method == Method.RESIZE)
        {
            return super.onCompress(method);
        }
        else
        {
            var frozenValues = newLongArray(this, "froze", size());
            var keys = iterator();
            for (var i = 0; keys.hasNext(); i++)
            {
                frozenValues[i] = keys.next();
            }
            Arrays.sort(frozenValues);
            values = frozenValues;

            return method;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize()
    {
        super.onInitialize();

        values = newLongArray(this, "allocated");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);

        values = kryo.readObject(input, long[].class);
    }

    /**
     * Removes the given value from this set
     *
     * @return True if the value was removed and false if it was not found
     */
    @Override
    public boolean remove(long value)
    {
        assert compressionMethod() != Method.FREEZE;

        // Get index of value
        var index = index(values, value);

        // If the value was found,
        if (!isEmpty(values[index]))
        {
            // remove it
            values[index] = nullLong();
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
                toString(values(), ", ", 10, "\n", Long::toString);
    }

    /**
     * @return The values in this set
     */
    public LongIterator values()
    {
        return iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copy(PrimitiveMap uncast)
    {
        super.copy(uncast);
        var that = (LongSet) uncast;
        values = that.values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyEntries(PrimitiveMap uncast, ProgressReporter reporter)
    {
        var that = (LongSet) uncast;
        var indexes = nonEmptyIndexes(that.values);
        while (indexes.hasNext())
        {
            var index = indexes.next();
            var value = that.values[index];
            if (!isNull(value))
            {
                add(value);
            }
            reporter.next();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PrimitiveMap newMap()
    {
        return new LongSet(objectName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int slots()
    {
        return values.length;
    }
}
