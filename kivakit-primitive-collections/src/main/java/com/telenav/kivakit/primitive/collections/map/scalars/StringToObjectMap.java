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

package com.telenav.kivakit.primitive.collections.map.scalars;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.kernel.language.iteration.BaseIterator;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.iteration.IntIterator;
import com.telenav.kivakit.primitive.collections.map.PrimitiveMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Arrays;
import java.util.Iterator;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * A map from String keys to int values. Supports typical map functions:
 * <p>
 * <b>Access</b>
 * <ul>
 *     <li>{@link #get(String)} </li>
 *     <li>{@link #put(String, Object)}</li>
 *     <li>{@link #remove(String)}</li>
 *     <li>{@link #clear()}</li>
 * </ul>
 * <p>
 * <b>Keys and Values</b>
 * <ul>
 *     <li>{@link #keys()}</li>
 *     <li>{@link #values()}</li>
 * </ul>
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}.
 *
 * @author jonathanl (shibo)
 * @see PrimitiveMap
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public final class StringToObjectMap<T> extends PrimitiveMap
{
    /** The keys */
    private String[] keys;

    /** The values */
    private T[] values;

    public StringToObjectMap(String objectName)
    {
        super(objectName);
    }

    private StringToObjectMap()
    {
    }

    @Override
    public Count capacity()
    {
        return Count.count(keys.length);
    }

    /**
     * Clears all key/value pairs from this map
     */
    @Override
    public void clear()
    {
        super.clear();
        assert compressionMethod() != CompressibleCollection.Method.FREEZE;
        size(0);
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
    }

    /**
     * @return True if this map contains the given key
     */
    public boolean containsKey(String key)
    {
        if (compressionMethod() == CompressibleCollection.Method.FREEZE)
        {
            return Arrays.binarySearch(keys, key) >= 0;
        }
        else
        {
            return get(key) != null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof StringToObjectMap)
        {
            if (object == this)
            {
                return true;
            }
            var that = (StringToObjectMap<T>) object;
            if (size() != that.size())
            {
                return false;
            }
            var keys = keys();
            while (keys.hasNext())
            {
                var key = keys.next();
                var value = get(key);
                if (!Objects.equal(value, that.get(key)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return The value for the given key. The returned value should be checked with {@link #isNull(int)} to determine
     * if it represents a null value.
     */
    public T get(String key)
    {
        if (compressionMethod() == CompressibleCollection.Method.FREEZE)
        {
            var index = Arrays.binarySearch(keys, key);
            return index < 0 ? null : values[index];
        }
        else
        {
            return values[index(keys, key)];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return keys().hashCode() ^ values().hashCode();
    }

    /**
     * @return The keys in this map
     */
    public Iterator<String> keys()
    {
        return new BaseIterator<>()
        {
            private final IntIterator indexes = nonEmptyIndexes();

            @Override
            protected String onNext()
            {
                if (indexes.hasNext())
                {
                    return keys[indexes.next()];
                }
                return null;
            }
        };
    }

    @Override
    public CompressibleCollection.Method onCompress(CompressibleCollection.Method method)
    {
        if (method == CompressibleCollection.Method.RESIZE)
        {
            return super.onCompress(method);
        }
        else
        {
            var frozenKeys = newStringArray(this, "froze", size());
            T[] frozenValues = newObjectArray(this, "froze", size());
            var keys = keys();
            for (var i = 0; keys.hasNext(); i++)
            {
                frozenKeys[i] = keys.next();
            }
            Arrays.sort(frozenKeys);
            for (var i = 0; i < frozenValues.length; i++)
            {
                frozenValues[i] = get(frozenKeys[i]);
            }
            this.keys = frozenKeys;
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

        keys = newStringArray(this, "allocated");
        values = newObjectArray(this, "allocated");
    }

    /**
     * Stores the given value under the given key
     */
    public void put(String key, T value)
    {
        assert compressionMethod() != CompressibleCollection.Method.FREEZE;

        if (value == null)
        {
            fail("Cannot put null values");
        }

        // Get the index to put at
        var index = index(keys, key);

        // If the slot at the given index is empty
        if (isEmpty(keys[index]))
        {
            // then we're adding a new key/value pair
            keys[index] = key;
            values[index] = value;
            increaseSize();
        }
        else
        {
            // otherwise, we're just changing the value
            values[index] = value;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);

        keys = kryo.readObject(input, String[].class);
        values = (T[]) kryo.readClassAndObject(input);
    }

    /**
     * Removes the key / values pair for the given key
     *
     * @return True if the key was remove, false if it could not be found
     */
    public boolean remove(String key)
    {
        assert compressionMethod() != CompressibleCollection.Method.FREEZE;

        // Get index of key
        var index = index(keys, key);

        // If the key was found,
        if (!isEmpty(keys[index]))
        {
            // remove it
            keys[index] = TOMBSTONE_STRING;
            values[index] = null;
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
        return "[StringToObjectMap name = " + objectName() + ", size = " + size() + "]";
    }

    /**
     * @return The values in this map
     */
    public Iterator<T> values()
    {
        return new BaseIterator<>()
        {
            private final IntIterator indexes = nonEmptyIndexes();

            @Override
            protected T onNext()
            {
                if (indexes.hasNext())
                {
                    return values[indexes.next()];
                }
                return null;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, keys);
        kryo.writeClassAndObject(output, values);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void copy(PrimitiveMap uncast)
    {
        super.copy(uncast);
        var that = (StringToObjectMap<T>) uncast;
        keys = that.keys;
        values = that.values;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void copyEntries(PrimitiveMap uncast, ProgressReporter reporter)
    {
        var that = (StringToObjectMap<T>) uncast;
        var indexes = nonEmptyIndexes(that.keys);
        while (indexes.hasNext())
        {
            var index = indexes.next();
            var key = that.keys[index];
            var value = that.values[index];
            if (key != null && value != null)
            {
                put(key, value);
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
        return new StringToObjectMap<T>(objectName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int slots()
    {
        return keys.length;
    }

    /**
     * @return The indexes with key/value pairs
     */
    private IntIterator nonEmptyIndexes()
    {
        return new IntIterator()
        {
            private int index;

            private int nextIndex = findNext();

            @Override
            public boolean hasNext()
            {
                return nextIndex != nullIndex();
            }

            @Override
            public int next()
            {
                if (nextIndex != nullIndex())
                {
                    var result = nextIndex;
                    nextIndex = findNext();
                    return result;
                }
                return nullIndex();
            }

            private int findNext()
            {
                while (index < keys.length)
                {
                    var key = keys[index++];
                    if (!isEmpty(key))
                    {
                        return index - 1;
                    }
                }
                return nullIndex();
            }
        };
    }
}
