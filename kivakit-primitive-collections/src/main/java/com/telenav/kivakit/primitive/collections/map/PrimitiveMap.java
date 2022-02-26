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

package com.telenav.kivakit.primitive.collections.map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.kernel.language.strings.Indent;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.primitive.collections.PrimitiveCollection;
import com.telenav.kivakit.primitive.collections.iteration.ByteIterator;
import com.telenav.kivakit.primitive.collections.iteration.IntIterator;
import com.telenav.kivakit.primitive.collections.iteration.LongIterator;
import com.telenav.kivakit.primitive.collections.iteration.PrimitiveIterator;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Arrays;
import java.util.Iterator;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.illegalState;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

@SuppressWarnings("UnusedReturnValue")
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public abstract class PrimitiveMap extends PrimitiveCollection
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /** Tombstone value that marks a removed key */
    protected static final long TOMBSTONE_LONG = Long.MIN_VALUE + 1;

    /** Tombstone value that marks a removed key */
    protected static final int TOMBSTONE_INT = Integer.MIN_VALUE + 1;

    /** Tombstone value that marks a removed key */
    protected static final int TOMBSTONE_BYTE = Byte.MIN_VALUE + 1;

    /** Tombstone String value that marks a removed key */
    protected static final String TOMBSTONE_STRING = "Tombstone";

    protected interface MapToString
    {
        String toString(long key, long value);
    }

    /** The current hashingStrategy of this map */
    private HashingStrategy hashingStrategy;

    /** The threshold at which we should resize */
    private int rehashThreshold;

    protected PrimitiveMap(String name)
    {
        super(name);
    }

    protected PrimitiveMap()
    {
    }

    @Override
    public Count capacity()
    {
        return hashingStrategy.rehashThreshold();
    }

    /**
     * @return True if this set contains the given value
     */
    public final boolean contains(int[] values, int value)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return Arrays.binarySearch(values, value) >= 0;
        }
        else
        {
            return !isEmpty(values[index(values, value)]);
        }
    }

    /**
     * @return True if this set contains the given value
     */
    public final boolean contains(long[] values, long value)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return Arrays.binarySearch(values, value) >= 0;
        }
        else
        {
            return !isEmpty(values[index(values, value)]);
        }
    }

    /**
     * @return True if this set contains the given value
     */
    public final <T> boolean contains(T[] values, T value)
    {
        if (compressionMethod() == Method.FREEZE)
        {
            return Arrays.binarySearch(values, value) >= 0;
        }
        else
        {
            return values[index(values, value)] != null;
        }
    }

    public boolean isEmpty(int key)
    {
        return isNull(key) || isTombstone(key);
    }

    public boolean isEmpty(byte key)
    {
        return isNull(key) || isTombstone(key);
    }

    public boolean isEmpty(long key)
    {
        return isNull(key) || isTombstone(key);
    }

    public boolean isEmpty(String key)
    {
        return key == null || isTombstone(key);
    }

    @Override
    public Method onCompress(Method method)
    {
        if (method == Method.RESIZE)
        {
            // We temporarily use a maximum occupancy of 100% during rehashing  to ensure that
            // adding elements during the rehash for trimming a collection does not cause another resize.
            var maximumOccupancy = hashingStrategy().maximumOccupancy();
            rehash(DefaultHashingStrategy.of(count().asEstimate(), Percent._100));
            hashingStrategy(DefaultHashingStrategy.of(count().asEstimate(), maximumOccupancy));
            return Method.RESIZE;
        }

        return Method.NONE;
    }

    @Override
    @MustBeInvokedByOverriders
    public void onInitialize()
    {
        hashingStrategy = DefaultHashingStrategy.of(initialSize());
        initialSize(hashingStrategy.recommendedSize());
        rehashThreshold = hashingStrategy.rehashThreshold().asInt();

        super.onInitialize();
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        super.read(kryo, input);

        rehashThreshold = kryo.readObject(input, int.class);
        hashingStrategy = kryo.readObject(input, DefaultHashingStrategy.class);
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        super.write(kryo, output);

        kryo.writeObject(output, rehashThreshold);
        kryo.writeObject(output, hashingStrategy);
    }

    protected void compare(PrimitiveMap that)
    {
    }

    protected void copy(PrimitiveMap that)
    {
        super.copy(that);

        rehashThreshold = that.rehashThreshold;
        hashingStrategy = that.hashingStrategy;

        initialSize(hashingStrategy.recommendedSize());
    }

    protected abstract void copyEntries(PrimitiveMap that, ProgressReporter reporter);

    /**
     * @return Hash of value
     */
    protected final int hash(int value)
    {
        var hash = Hash.SEED * value;
        return hash < 0 ? -hash : hash;
    }

    /**
     * @return Hash of key
     */
    protected final int hash(long value)
    {
        var hash = (int) ((value ^ (value >>> 32)) * Hash.KNUTH_SEED);
        return hash < 0 ? -hash : hash;
    }

    protected HashingStrategy hashingStrategy()
    {
        return hashingStrategy;
    }

    protected PrimitiveMap hashingStrategy(HashingStrategy hashingStrategy)
    {
        this.hashingStrategy = hashingStrategy;
        rehashThreshold = hashingStrategy.rehashThreshold().asInt();

        // The hashing strategy may change the estimated size given to it, for example, to a prime number
        initialSize(hashingStrategy.recommendedSize());
        return this;
    }

    protected void increaseSize()
    {
        // Increase the size
        int size = incrementSize();

        // If we're out of room,
        if (size > rehashThreshold)
        {
            // rehash to a larger size
            rehash(hashingStrategy().withIncreasedCapacity());
        }
    }

    protected final int index(int index)
    {
        return Math.abs(index % slots());
    }

    /**
     * @return The index of the given value, resolved with linear probing
     */
    protected int index(int[] values, int value)
    {
        // Linear probe resolution is quite efficient due to chip caching
        var index = index(hash(value));
        var tombstoneIndex = -1;
        for (var offset = 0; offset < values.length; offset++)
        {
            // If we find the value we're looking for,
            var at = index(index + offset);
            var current = values[at];
            if (current == value)
            {
                // return the index we're at
                return at;
            }

            // If we're looking for a non-null value and the value we're at is null,
            if (!isNull(value) && isNull(current))
            {
                // then we didn't find the value, so we return the first free index we encountered
                return tombstoneIndex != -1 ? tombstoneIndex : at;
            }

            // If we haven't already found a tombstone and we're looking at one,
            if (tombstoneIndex == -1 && isTombstone(current))
            {
                // save the index so we can return it later as an empty slot
                tombstoneIndex = at;
            }
        }

        // The set should never be this full
        return illegalState("Internal error (index = $, size = $). Check the null value being used to initialize keys and values.", index, size());
    }

    protected final long index(long hash)
    {
        return Math.abs(hash % slots());
    }

    /**
     * @return The index of the given value, resolved with linear probing
     */
    protected int index(long[] values, long value)
    {
        // Linear probe resolution is quite efficient due to chip caching
        var index = index(hash(value));
        var tombstoneIndex = -1;
        boolean isNull = isNull(value);
        for (var offset = 0; offset < values.length; offset++)
        {
            // If we find the value we're looking for,
            var at = index(index + offset);
            var current = values[at];
            if (current == value)
            {
                // return the index we're at
                return at;
            }

            // If we're looking for a non-null value and the value we're at is null,
            if (!isNull && isNull(current))
            {
                // then we didn't find the value, so we return the first free index we encountered
                return tombstoneIndex != -1 ? tombstoneIndex : at;
            }

            // If we haven't already found a tombstone and we're looking at one,
            if (tombstoneIndex == -1 && isTombstone(current))
            {
                // save the index so we can return it later as an empty slot
                tombstoneIndex = at;
            }
        }

        // The set should never be this full
        return illegalState("Internal error (index = $, size = $). Check the null value being used to initialize keys and values.", index, size());
    }

    /**
     * @return The index of the given value, resolved with linear probing
     */
    protected int index(Object[] values, Object value)
    {
        // Linear probe resolution is quite efficient due to chip caching
        var index = index(value.hashCode());
        var tombstoneIndex = -1;
        for (var offset = 0; offset < values.length; offset++)
        {
            // If we find the value we're looking for,
            var at = index(index + offset);
            var current = values[at];
            if (current == value)
            {
                // return the index we're at
                return at;
            }

            // If we're looking for a non-null value and the value we're at is null,
            if (current == null)
            {
                // then we didn't find the value, so we return the first free index we encountered
                return tombstoneIndex != -1 ? tombstoneIndex : at;
            }

            // If we haven't already found a tombstone and we're looking at one,
            if (tombstoneIndex == -1 && isTombstone(current))
            {
                // save the index so we can return it later as an empty slot
                tombstoneIndex = at;
            }
        }

        // The set should never be this full
        fail("Internal error (index = " + index + ", size = " + size() + ")");
        return -1;
    }

    protected final boolean isTombstone(int key)
    {
        return key == TOMBSTONE_INT;
    }

    protected final boolean isTombstone(long key)
    {
        return key == TOMBSTONE_LONG;
    }

    protected final boolean isTombstone(Object key)
    {
        return key == TOMBSTONE_STRING;
    }

    protected abstract PrimitiveMap newMap();

    /**
     * @return The indexes with values
     */
    protected IntIterator nonEmptyIndexes(byte[] values)
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
                while (index < values.length)
                {
                    int value = values[index++];
                    if (!isEmpty((byte) value))
                    {
                        return index - 1;
                    }
                }
                return nullIndex();
            }
        };
    }

    /**
     * @return The indexes with values
     */
    protected IntIterator nonEmptyIndexes(int[] values)
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
                while (index < values.length)
                {
                    var value = values[index++];
                    if (!isEmpty(value))
                    {
                        return index - 1;
                    }
                }
                return nullIndex();
            }
        };
    }

    /**
     * @return The indexes with values
     */
    protected IntIterator nonEmptyIndexes(long[] values)
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
                if (values != null)
                {
                    while (index < values.length)
                    {
                        var value = values[index++];
                        if (!isEmpty(value))
                        {
                            return index - 1;
                        }
                    }
                }
                return nullIndex();
            }
        };
    }

    /**
     * @return The indexes with values
     */
    protected <T> IntIterator nonEmptyIndexes(T[] values)
    {
        return new IntIterator()
        {
            private int index;

            private int nextIndex = findNext();

            @Override
            public boolean hasNext()
            {
                return nextIndex != -1;
            }

            @Override
            public int next()
            {
                if (nextIndex != -1)
                {
                    var result = nextIndex;
                    nextIndex = findNext();
                    return result;
                }
                return nullIndex();
            }

            private int findNext()
            {
                while (index < values.length)
                {
                    var value = values[index++];
                    if (value != null)
                    {
                        return index - 1;
                    }
                }
                return nullIndex();
            }
        };
    }

    protected ByteIterator nonEmptyValues(byte[] values)
    {
        return new ByteIterator()
        {
            private final IntIterator indexes = nonEmptyIndexes(values);

            @Override
            public boolean hasNext()
            {
                return indexes.hasNext();
            }

            @Override
            public byte next()
            {
                var next = indexes.next();
                return values[next];
            }
        };
    }

    protected IntIterator nonEmptyValues(int[] values)
    {
        return new IntIterator()
        {
            private final IntIterator indexes = nonEmptyIndexes(values);

            @Override
            public boolean hasNext()
            {
                return indexes.hasNext();
            }

            @Override
            public int next()
            {
                return values[indexes.next()];
            }
        };
    }

    protected LongIterator nonEmptyValues(long[] values)
    {
        return new LongIterator()
        {
            private final IntIterator indexes = nonEmptyIndexes(values);

            @Override
            public boolean hasNext()
            {
                return indexes.hasNext();
            }

            @Override
            public long next()
            {
                var next = indexes.next();
                return values[next];
            }
        };
    }

    protected <T> Iterator<T> nonEmptyValues(T[] values)
    {
        return new Iterator<>()
        {
            private final IntIterator indexes = nonEmptyIndexes(values);

            @Override
            public boolean hasNext()
            {
                return indexes.hasNext();
            }

            @Override
            public T next()
            {
                return values[indexes.next()];
            }
        };
    }

    protected int slots()
    {
        return unsupported();
    }

    protected String toString(PrimitiveIterator keys, PrimitiveIterator values,
                              MapToString toStringer)
    {
        return Indent.by(4, toString(keys, values, ", ", 5, "\n", toStringer));
    }

    protected String toString(PrimitiveIterator keys, PrimitiveIterator values, String separator,
                              int every, String section, MapToString toStringer)
    {
        var count = Math.min(size(), TO_STRING_MAXIMUM_ELEMENTS);
        var builder = new StringBuilder();
        if (keys != null && keys.hasNext() && values != null && values.hasNext())
        {
            for (var i = 0; keys.hasNext() && i < count; i++)
            {
                if (i > 0)
                {
                    if (every > 0 && i % every == 0)
                    {
                        builder.append(section);
                    }
                    else
                    {
                        builder.append(separator);
                    }
                }
                var key = keys.nextLong();
                var value = values.nextLong();
                builder.append(toStringer.toString(key, value));
            }
        }
        else
        {
            builder.append("null");
        }

        if (size() > TO_STRING_MAXIMUM_ELEMENTS)
        {
            builder.append(separator);
            builder.append("[...]");
        }
        return builder.toString();
    }

    private void rehash(HashingStrategy hasher)
    {
        // Create a new map and assign a hashing strategy with increased capacity,
        var copy = newMap();
        copy.hashingStrategy(hasher);

        // but the map cannot be initialized because we need to set the right null values and initialize it ourselves
        assert !copy.isInitialized();

        // then copy all the configuration fields like null values from this object into the copy
        copy.copyNullValues(this);

        // then copy the entries from this object into the copy
        copy.initialize();
        var progress = size() > 10_000_000 ? Progress.create(LOGGER, "entries") : Progress.NULL;
        progress.steps(count().asMaximum());
        progress.start("Rehashing " + objectName());
        copy.copyEntries(this, progress);
        progress.end("Finished rehashing " + objectName());

        // and at this point, the objects should be identical
        if (DEBUG.isDebugOn())
        {
            if (!copy.equals(this))
            {
                LOGGER.warning("Unable to rehash $ ($)", objectName(), NamedObject.syntheticName(this));
                compare(copy);
            }
        }

        // and we can copy the copy's fields back into this object
        copy(copy);
        allocated(this, "resized", this, copy.size());
    }
}
