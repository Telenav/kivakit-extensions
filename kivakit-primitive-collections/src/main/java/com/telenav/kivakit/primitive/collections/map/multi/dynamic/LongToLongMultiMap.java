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

package com.telenav.kivakit.primitive.collections.map.multi.dynamic;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import com.telenav.kivakit.primitive.collections.array.scalars.LongArray;
import com.telenav.kivakit.primitive.collections.iteration.LongIterator;
import com.telenav.kivakit.primitive.collections.list.PrimitiveList;
import com.telenav.kivakit.primitive.collections.list.store.LongLinkedListStore;
import com.telenav.kivakit.primitive.collections.map.multi.LongMultiMap;
import com.telenav.kivakit.primitive.collections.map.multi.PrimitiveMultiMap;
import com.telenav.kivakit.primitive.collections.map.multi.PrimitiveScalarMultiMap;
import com.telenav.kivakit.primitive.collections.map.split.SplitLongToIntMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A map from long -&gt; list of longs.
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public final class LongToLongMultiMap extends PrimitiveMultiMap implements LongMultiMap, PrimitiveScalarMultiMap
{
    /** The array of arrays */
    private LongLinkedListStore values;

    /** Map from key to lists index */
    private SplitLongToIntMap indexes;

    public LongToLongMultiMap(String objectName)
    {
        super(objectName);
    }

    private LongToLongMultiMap()
    {
    }

    /**
     * Adds the given value to the list for the given key
     */
    public void add(long key, long value)
    {
        if (ensureHasRoomFor(1))
        {
            var indexes = this.indexes;
            var index = indexes.get(key);
            var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = values.add(head, value);
            indexes.put(key, index);
        }
    }

    /**
     * @return True if this map contains a list for the given key
     */
    public boolean containsKey(long key)
    {
        var indexes = this.indexes;
        return !indexes.isNull(indexes.get(key));
    }

    /**
     * @return An array of longs for the given key. This method is convenient in some cases, but it is less efficient
     * than {@link #iterator(long)}.
     */
    @Override
    public LongArray get(long key)
    {
        var array = new LongArray("get");
        array.initialSize(initialChildSizeAsInt());
        array.initialize();

        var iterator = iterator(key);
        if (iterator != null)
        {
            while (iterator.hasNext())
            {
                array.add(iterator.next());
            }
        }
        return array;
    }

    @Override
    public PrimitiveList getPrimitiveList(long key)
    {
        return get(key);
    }

    @Override
    public boolean isScalarKeyNull(long key)
    {
        return isNull(key);
    }

    /**
     * @return An iterator over the list of longs for the given key
     */
    public LongIterator iterator(long key)
    {
        var index = indexes.get(key);
        if (!indexes.isNull(index))
        {
            return values.list(index);
        }
        return null;
    }

    /**
     * @return An {@link Iterable} over the keys in this map
     */
    public LongIterator keys()
    {
        return indexes.keys();
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
            indexes.compress(method);
            values.compress(method);

            return method;
        }
    }

    @Override
    public void onInitialize()
    {
        super.onInitialize();

        indexes = new SplitLongToIntMap(objectName() + ".indexes");
        indexes.initialSize(initialSize());
        indexes.initialize();

        values = new LongLinkedListStore(objectName() + ".values");
        indexes.initialSize(initialSize());
        values.initialize();
    }

    /**
     * Adds all the given values to the list for the given key
     */
    public void putAll(long key, long[] values)
    {
        if (ensureHasRoomFor(1))
        {
            var indexes = this.indexes;
            var index = indexes.get(key);
            var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    @Override
    public void putAll(long key, List<? extends Quantizable> values)
    {
        if (ensureHasRoomFor(1))
        {
            var indexes = this.indexes;
            var index = indexes.get(key);
            var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    /**
     * Adds all the given values to the list for the given key
     */
    @Override
    public void putAll(long key, LongArray values)
    {
        if (ensureHasRoomFor(1))
        {
            var indexes = this.indexes;
            var index = indexes.get(key);
            var head = indexes.isNull(index) ? LongLinkedListStore.NEW_LIST : index;
            index = this.values.addAll(head, values);
            indexes.put(key, index);
        }
    }

    @Override
    public void putPrimitiveList(long key, PrimitiveList values)
    {
        putAll(key, (LongArray) values);
    }

    @Override
    public void putPrimitiveList(long key, List<? extends Quantizable> values)
    {
        putAll(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);

        values = kryo.readObject(input, LongLinkedListStore.class);
        indexes = kryo.readObject(input, SplitLongToIntMap.class);
    }

    /**
     * @return The number of entries in this map
     */
    @Override
    public int size()
    {
        return indexes.size();
    }

    @Override
    public String toString()
    {
        return "[" + getClass().getSimpleName() + " name = " + objectName() + ", size = " + size() + "]\n" +
                toString(keys(), key ->
                        {
                            var list = get(key);
                            return list.iterator();
                        },
                        (key, values) -> values == null ? "null" : key + " -> " + values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, values);
        kryo.writeObject(output, indexes);
    }
}
