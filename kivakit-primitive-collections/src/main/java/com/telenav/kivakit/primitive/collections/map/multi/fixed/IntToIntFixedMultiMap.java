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

package com.telenav.kivakit.primitive.collections.map.multi.fixed;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.primitive.collections.array.scalars.IntArray;
import com.telenav.kivakit.primitive.collections.iteration.IntIterator;
import com.telenav.kivakit.primitive.collections.list.PrimitiveList;
import com.telenav.kivakit.primitive.collections.map.multi.IntMultiMap;
import com.telenav.kivakit.primitive.collections.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.primitive.collections.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.primitive.collections.map.scalars.IntToIntMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A compact multi-map which allows one-time put of a fixed list of values. Adding more values is not supported.
 *
 * @author jonathanl (shibo)
 * @see IntArray
 * @see PrimitiveMultiMap
 * @see KryoSerializable
 */
@SuppressWarnings({ "ConstantConditions" })
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public final class IntToIntFixedMultiMap extends PrimitiveMultiMap implements IntMultiMap, PrimitiveScalarMultiMap
{
    private static final int TERMINATOR = Integer.MAX_VALUE - 1;

    /** Null terminated lists of values */
    private IntArray values;

    /** Map from key to values index */
    private IntToIntMap indexes;

    public IntToIntFixedMultiMap(final String objectName)
    {
        super(objectName);
    }

    protected IntToIntFixedMultiMap()
    {
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(final int key)
    {
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return An int array for the given key
     */
    public IntArray get(final int key)
    {
        final var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            final var values = new IntArray("get");
            values.initialSize(initialChildSizeAsInt());
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                final var value = this.values.get(i);
                if (value == TERMINATOR)
                {
                    return values;
                }
                values.add(value);
            }
            return values;
        }
        return null;
    }

    @Override
    public IntArray get(final long key)
    {
        return get((int) key);
    }

    @Override
    public PrimitiveList getPrimitiveList(final long key)
    {
        return get((int) key);
    }

    @Override
    public boolean isScalarKeyNull(final long key)
    {
        return isNull((int) key);
    }

    /**
     * @return An iterator over the keys in this map
     */
    public IntIterator keys()
    {
        return indexes.keys();
    }

    @Override
    public CompressibleCollection.Method onCompress(final CompressibleCollection.Method method)
    {
        if (method == CompressibleCollection.Method.RESIZE)
        {
            return super.onCompress(method);
        }
        else
        {
            indexes.compress(method);
            values.compress(method);

            return CompressibleCollection.Method.MIXED;
        }
    }

    @Override
    public void putAll(final long key, final IntArray values)
    {
        putAll((int) key, values);
    }

    @Override
    public void putAll(final long key, final List<? extends Quantizable> values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get((int) key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put((int) key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(final int key, final int[] values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(final int key, final IntArray values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            final var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(TERMINATOR);
        }
    }

    @Override
    public void putPrimitiveList(final long key, final PrimitiveList values)
    {
        putAll((int) key, (IntArray) values);
    }

    @Override
    public void putPrimitiveList(final long key, final List<? extends Quantizable> values)
    {
        putAll((int) key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        values = kryo.readObject(input, IntArray.class);
        indexes = kryo.readObject(input, IntToIntMap.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return indexes.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(keys(), key -> get(key.intValue()) == null ? null : get(key.intValue()).iterator(),
                        (key, values) -> values == null ? "null" : key + " -> " + values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, values);
        kryo.writeObject(output, indexes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        indexes = new IntToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new IntArray(objectName() + ".values");
        indexes.initialSize(initialSize());
        values.initialize();

        // Add a value in the first index spot because index 0 is invalid
        values.add(nullInt());
    }
}
