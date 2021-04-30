////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.primitive.collections.array.arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.primitive.collections.array.PrimitiveArrayArray;
import com.telenav.kivakit.primitive.collections.iteration.ByteIterable;
import com.telenav.kivakit.primitive.collections.array.scalars.ByteArray;
import com.telenav.kivakit.primitive.collections.array.scalars.IntArray;
import com.telenav.kivakit.primitive.collections.iteration.ByteIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveArrayArray;
import com.telenav.kivakit.core.kernel.language.collections.CompressibleCollection;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.objects.Objects;

/**
 * Optimized storage of two dimensional byte arrays. A sequence of {@link ByteArray} objects can be added by calling
 * {@link #add(ByteIterable)}. The stored array can be retrieved later by calling {@link #get(int)} passing in the
 * identifier that was returned by add. The number of byte arrays can be retrieved with {@link #size()}
 * <p>
 * This class supports the {@link #hashCode()} / {@link #equals(Object)} contract and is {@link KryoSerializable}
 *
 * @author jonathanl (shibo)
 * @see ByteIterable
 * @see ByteIterator
 * @see ByteArray
 * @see KryoSerializable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArrayArray.class)
public final class ByteArrayArray extends PrimitiveArrayArray
{
    private IntArray indexes;

    private IntArray sizes;

    private ByteArray store;

    public ByteArrayArray(final String objectName)
    {
        super(objectName);
    }

    protected ByteArrayArray()
    {
    }

    /**
     * Adds the given values and returns an identifier that can be used to retrieve the values as a {@link ByteArray}
     * with {@link #get(int)}.
     *
     * @param values The values to add
     * @return An identifier for the values that were added
     */
    public int add(final ByteIterable values)
    {
        return add(values.iterator());
    }

    /**
     * Adds the given values and returns an identifier that can be used to retrieve the values as a {@link ByteArray}
     * with {@link #get(int)}.
     *
     * @param values The values to add
     * @return An identifier for the values that were added
     */
    public int add(final byte[] values)
    {
        assert ensureHasRoomFor(1);

        final var index = store.size();
        indexes.add(index);
        sizes.add(values.length);
        store.addAll(values);

        return index;
    }

    /**
     * Adds the given values and returns an identifier that can be used to retrieve the values as a {@link ByteArray}
     * with {@link #get(int)}.
     *
     * @param values The values to add
     * @return An identifier for the values that were added
     */
    public int add(final ByteIterator values)
    {
        assert ensureHasRoomFor(1);

        final var index = store.size();
        indexes.add(index);

        // Add all the values to the store
        var size = 0;
        while (values.hasNext())
        {
            store.add(values.next());
            size++;
        }
        sizes.add(size);

        return indexes.size() - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ByteArrayArray)
        {
            final var that = (ByteArrayArray) object;
            return Objects.equalPairs(indexes, that.indexes, sizes, that.sizes, store, that.store);
        }
        return false;
    }

    /**
     * @return The byte array for the given identifier
     */
    public ByteArray get(final int identifier)
    {
        return store.sublist(indexes.get(identifier), sizes.get(identifier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Hash.many(indexes, sizes, store);
    }

    /**
     * @return The size of the identified array
     */
    public int length(final int identifier)
    {
        return sizes.get(identifier);
    }

    @Override
    public CompressibleCollection.Method onCompress(final CompressibleCollection.Method method)
    {
        indexes.compress(method);
        sizes.compress(method);
        store.compress(method);

        return CompressibleCollection.Method.RESIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);

        indexes = kryo.readObject(input, IntArray.class);
        sizes = kryo.readObject(input, IntArray.class);
        store = kryo.readObject(input, ByteArray.class);
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
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, indexes);
        kryo.writeObject(output, sizes);
        kryo.writeObject(output, store);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        store = new ByteArray(objectName() + ".bytes");
        store.initialSize(initialSizeAsInt() * initialChildSizeAsInt());
        store.initialize();

        indexes = new IntArray(objectName() + ".indexes");
        indexes.initialize();

        sizes = new IntArray(objectName() + ".sizes");
        sizes.initialize();
    }
}
