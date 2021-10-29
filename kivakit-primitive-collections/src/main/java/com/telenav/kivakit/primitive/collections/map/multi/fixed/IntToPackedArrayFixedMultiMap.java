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
import com.telenav.kivakit.kernel.language.values.count.BitCount;
import com.telenav.kivakit.primitive.collections.array.packed.PackedPrimitiveArray;
import com.telenav.kivakit.primitive.collections.array.packed.SplitPackedArray;
import com.telenav.kivakit.primitive.collections.array.scalars.IntArray;
import com.telenav.kivakit.primitive.collections.array.scalars.LongArray;
import com.telenav.kivakit.primitive.collections.iteration.IntIterator;
import com.telenav.kivakit.primitive.collections.list.PrimitiveList;
import com.telenav.kivakit.primitive.collections.map.multi.LongMultiMap;
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
public final class IntToPackedArrayFixedMultiMap extends PrimitiveMultiMap implements LongMultiMap, PrimitiveScalarMultiMap
{
    /** Null terminated lists of values */
    private SplitPackedArray values;

    /** Map from key to values index */
    private IntToIntMap indexes;

    private BitCount bits;

    private PackedPrimitiveArray.OverflowHandling overflow;

    private long listTerminator;

    public IntToPackedArrayFixedMultiMap(String objectName)
    {
        super(objectName);
    }

    private IntToPackedArrayFixedMultiMap()
    {
    }

    public IntToPackedArrayFixedMultiMap bits(BitCount bits, PackedPrimitiveArray.OverflowHandling overflow)
    {
        this.bits = bits;
        this.overflow = overflow;
        return this;
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(int key)
    {
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return A long array for the given key
     */
    public LongArray get(int key)
    {
        var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                var value = this.values.get(i);
                if (value == listTerminator)
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
    public LongArray get(long key)
    {
        return get((int) key);
    }

    @Override
    public PrimitiveList getPrimitiveList(long key)
    {
        return get((int) key);
    }

    /**
     * @return A long array for the given key
     */
    public LongArray getSigned(int key)
    {
        var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                var value = this.values.getSigned(i);
                if (value == listTerminator)
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
    public PrimitiveList getSignedPrimitiveList(long key)
    {
        var index = indexes.get((int) key);
        if (!indexes.isNull(index))
        {
            var values = new LongArray("get");
            values.initialSize(256);
            values.initialize();

            for (var i = index; i < this.values.size(); i++)
            {
                var value = this.values.getSigned(i);
                if (value == listTerminator)
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
    public boolean isScalarKeyNull(long key)
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

    public IntToPackedArrayFixedMultiMap listTerminator(long listTerminator)
    {
        this.listTerminator = listTerminator;
        return this;
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
            indexes.compress(method);
            values.compress(method);

            return Method.MIXED;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitialize()
    {
        super.onInitialize();

        indexes = new IntToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new SplitPackedArray(objectName() + ".values");
        values.bits(bits, overflow);
        values.initialSize(initialSize());
        values.initialize();

        // Add a value in the first index spot because index 0 is invalid
        values.add(nullLong());
    }

    @Override
    public void putAll(long key, LongArray values)
    {
        putAll((int) key, values);
    }

    @Override
    public void putAll(long key, List<? extends Quantizable> values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get((int) key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put((int) key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(listTerminator);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(int key, long[] values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(listTerminator);
        }
    }

    /**
     * Puts the given values under the given key
     */
    public void putAll(int key, LongArray values)
    {
        // If we haven't already put a value for this key
        assert isNull(indexes.get(key));

        // and we have room to put a value
        if (ensureHasRoomFor(1))
        {
            // get the next index in the values array
            var index = this.values.size();

            // add a mapping from the key to the index
            indexes.put(key, index);

            // then add all the values and a terminator
            this.values.addAll(values);
            this.values.add(listTerminator);
        }
    }

    @Override
    public void putPrimitiveList(long key, PrimitiveList values)
    {
        putAll((int) key, (LongArray) values);
    }

    @Override
    public void putPrimitiveList(long key, List<? extends Quantizable> values)
    {
        putAll((int) key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);

        bits = kryo.readObject(input, BitCount.class);
        overflow = kryo.readObject(input, PackedPrimitiveArray.OverflowHandling.class);
        listTerminator = kryo.readObject(input, long.class);
        values = kryo.readObject(input, SplitPackedArray.class);
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
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, bits);
        kryo.writeObject(output, overflow);
        kryo.writeObject(output, listTerminator);
        kryo.writeObject(output, values);
        kryo.writeObject(output, indexes);
    }
}
