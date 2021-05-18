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

package com.telenav.kivakit.primitive.collections.list.store;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.telenav.kivakit.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.array.scalars.IntArray;
import com.telenav.kivakit.primitive.collections.array.scalars.LongArray;
import com.telenav.kivakit.primitive.collections.iteration.LongIterator;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * Stores linked lists of long values in a space efficient way.
 * <p>
 * The {@link #add(int, long)} method takes a list identifier and adds the given value to the head of the list,
 * returning an identifier to the new list. To start a new list, pass {@link #NEW_LIST} as the list identifier. To
 * retrieve the list as an {@link LongIterator}, call {@link #list(int)} with the list identifier.
 *
 * @author jonathanl (shibo)
 * @see LongIterator
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveList.class)
public class LongLinkedListStore extends PrimitiveListStore
{
    /** List identifier to start a new list */
    public static final int NEW_LIST = 0;

    /** Pointer value for end of list */
    private static final int END_OF_LIST = 0;

    /** Array of list values that correspond with next pointers */
    private LongArray values;

    /** List next pointers */
    private IntArray next;

    /** The next available spot to add to in the arrays */
    private int addAt = 1;

    public LongLinkedListStore(final String objectName)
    {
        super(objectName);
    }

    protected LongLinkedListStore()
    {
    }

    /**
     * Adds the given value to the given list by head insertion.
     * <p>
     * To start a new list, pass LongLinkedListStore.NEW_LIST as the list identifier.
     */
    public int add(final int list, final long value)
    {
        // Get a new list index,
        final var newList = addAt++;

        // store the value at that index,
        values.set(newList, value);

        // then set the next item for the new list to point to the old list
        next.set(newList, list);

        // and return the new list.
        return newList;
    }

    /**
     * Adds each of the given values to the identified list
     */
    public int addAll(int list, final LongArray values)
    {
        for (var index = 0; index < values.size(); index++)
        {
            list = add(list, values.get(index));
        }
        return list;
    }

    /**
     * Adds each of the given values to the identified list
     */
    public int addAll(int list, final long[] values)
    {
        for (final var value : values)
        {
            list = add(list, value);
        }
        return list;
    }

    /**
     * Adds the quantum of each value to the identified list
     */
    public int addAll(int list, final List<? extends Quantizable> values)
    {
        for (final var value : values)
        {
            list = add(list, value.quantum());
        }
        return list;
    }

    @Override
    public Count capacity()
    {
        return values.capacity().plus(next.capacity());
    }

    /**
     * @return An iterator over the values in the identifier list
     */
    public LongIterator list(final int list)
    {
        final var outer = this;
        return new LongIterator()
        {
            private int index = list;

            @Override
            public boolean hasNext()
            {
                return index != END_OF_LIST;
            }

            @Override
            public long next()
            {
                final var value = outer.values.get(index);
                index = outer.next.get(index);
                return value;
            }
        };
    }

    @Override
    public Method onCompress(final Method method)
    {
        values.compress(method);
        next.compress(method);

        return Method.RESIZE;
    }

    @Override
    public void read(final Kryo kryo, final Input input)
    {
        super.read(kryo, input);
        values = kryo.readObject(input, LongArray.class);
        next = kryo.readObject(input, IntArray.class);
    }

    /**
     * Removes the given value from the identified list
     */
    public int remove(final int list, final long value)
    {
        var at = list;
        var previous = NEW_LIST;
        while (!next.isNull(at))
        {
            final var next = this.next.get(at);
            if (values.get(at) == value)
            {
                if (at == list)
                {
                    return next;
                }
                else
                {
                    this.next.set(previous, next);
                    break;
                }
            }
            previous = at;
            at = next;
        }
        return list;
    }

    @Override
    public void write(final Kryo kryo, final Output output)
    {
        super.write(kryo, output);
        kryo.writeObject(output, values);
        kryo.writeObject(output, next);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInitialize()
    {
        super.onInitialize();

        values = new LongArray(objectName() + ".values");
        values.initialSize(initialSize());
        values.initialize();

        next = new IntArray(objectName() + ".next");
        next.initialSize(initialSize());
        next.nullInt(END_OF_LIST);
        next.initialize();
    }
}
