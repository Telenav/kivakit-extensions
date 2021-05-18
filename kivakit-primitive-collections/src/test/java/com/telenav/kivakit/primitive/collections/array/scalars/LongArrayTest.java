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

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.language.values.mutable.MutableInteger;
import com.telenav.kivakit.primitive.collections.project.PrimitiveCollectionsUnitTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.telenav.kivakit.test.UnitTest.Repeats.ALLOW_REPEATS;
import static com.telenav.kivakit.test.UnitTest.Repeats.NO_REPEATS;

public class LongArrayTest extends PrimitiveCollectionsUnitTest
{
    @Test
    public void testAdd()
    {
        final var array = array();
        array.hasNullLong(false);
        final var values = randomLongList(ALLOW_REPEATS);
        values.forEach(array::add);
        resetIndex();
        values.forEach(value -> ensureEqual(array.get(nextIndex()), value));
    }

    @Before
    public void testBefore()
    {
        iterations(5_000);
    }

    @Test
    public void testClear()
    {
        final var array = array();
        array.hasNullLong(false);
        randomLongs(NO_REPEATS, array::add);
        randomIndexes(NO_REPEATS, index ->
        {
            ensure(!array.isNull(array.get(index)));
            array.clear(index);
            ensure(!array.isNull(array.get(index)));
        });

        array.nullLong(-1);
        randomLongs(NO_REPEATS, value ->
        {
            if (!array.isNull(value))
            {
                array.add(value);
            }
        });
        randomIndexes(ALLOW_REPEATS, index ->
        {
            if (index < array.size())
            {
                array.set(index, 99);
                ensure(!array.isNull(array.get(index)));
                array.clear(index);
                ensure(array.isNull(array.get(index)));
            }
        });
    }

    @Test
    public void testEqualsHashCode()
    {
        final var map = new HashMap<LongArray, Integer>();
        loop(() ->
        {
            final var array = array();
            randomLongs(ALLOW_REPEATS, Count._32, array::add);
            map.put(array, 99);
            ensureEqual(99, map.get(array));
        });
    }

    @Test
    public void testFirstLast()
    {
        final var array = array();

        ensureThrows(array::first);
        ensureThrows(array::last);

        final var last = new MutableInteger(Integer.MIN_VALUE);

        resetIndex();
        randomLongs(ALLOW_REPEATS, value ->
        {
            final var index = nextIndex();
            array.set(index, value);
            last.maximum(index);
            ensureEqual(array.get(0), array.first());
            ensureEqual(array.get(last.get()), array.last());
        });
    }

    @Test
    public void testGetSet()
    {
        final var array = array();

        resetIndex();
        randomLongs(ALLOW_REPEATS, value ->
        {
            final var index = nextIndex();
            array.set(index, value);
            ensureEqual(array.get(index), value);
        });

        resetIndex();
        randomLongs(ALLOW_REPEATS, value ->
        {
            final var index = nextIndex();
            array.set(index, value);
            ensureEqual(array.get(index), value);
        });

        array.clear();
        array.nullLong(-1);
        randomLongs(NO_REPEATS, value -> value != -1, array::add);
        loop(() ->
        {
            final var index = randomIndex();
            final var value = array.safeGet(index);
            ensureEqual(index >= array.size(), array.isNull(value));
        });
    }

    @Test
    public void testIsNull()
    {
        final var array = array();
        final var nullValue = randomValueFactory().newLong();
        array.nullLong(nullValue);
        ensure(array.hasNullLong());
        resetIndex();
        randomLongs(ALLOW_REPEATS, value -> value != array.nullLong(), value ->
        {
            final var index = nextIndex();

            array.set(index, value);
            ensure(!array.isNull(array.get(index)));

            array.set(index, array.nullLong());
            ensure(array.isNull(array.get(index)));
        });
    }

    @Test
    public void testIteration()
    {
        final var array = array();
        array.hasNullLong(false);

        array.add(0);
        array.add(1);
        array.add(2);
        array.set(32, 100);

        var values = array.iterator();
        ensureEqual(0L, values.next());
        ensureEqual(1L, values.next());
        ensureEqual(2L, values.next());
        ensureEqual(array.nullLong(), values.next());
        ensure(values.hasNext());

        array.hasNullLong(true);

        values = array.iterator();
        ensureEqual(1L, values.next());
        ensureEqual(2L, values.next());
        ensureEqual(100L, values.next());
        ensureFalse(values.hasNext());
    }

    @Test
    public void testSerialization()
    {
        if (!isQuickTest())
        {
            final var array = array();
            randomLongs(ALLOW_REPEATS, array::add);
            serializationTest(array);
        }
    }

    @Test
    public void testSizeIsEmpty()
    {
        final var array = array();

        ensure(array.isEmpty());
        ensure(array.size() == 0);
        array.add(0);
        ensure(array.size() == 1);
        array.add(1);
        ensure(array.size() == 2);
        array.add(2);
        ensure(array.size() == 3);
        array.set(1000, 1000);
        ensure(array.size() == 1001);
        array.clear(2);
        ensure(array.size() == 1001);
        array.clear();
        ensure(array.isEmpty());
        ensure(array.size() == 0);

        final var maximum = new MutableInteger(Integer.MIN_VALUE);
        resetIndex();
        randomLongs(ALLOW_REPEATS, value ->
        {
            final var index = nextIndex();
            maximum.maximum(index);
            array.set(index, value);
            ensure(array.size() == maximum.get() + 1);
        });
    }

    @Test
    public void testSubArray()
    {
        final var array = array();
        randomLongs(ALLOW_REPEATS, array::add);
        final var last = array.size() - 1;
        final var offset = Math.abs(randomInt(0, last));
        final var length = Math.abs(randomInt(0, last - offset));
        ensure(offset < array.size());
        ensure(length >= 0);
        ensure(offset + length < array.size());
        final var subArray = array.subArray(offset, length);
        for (var i = 0; i < length; i++)
        {
            ensureEqual(array.get(offset + i), subArray.get(i));
        }
    }

    private LongArray array()
    {
        final var array = new LongArray("test");
        array.initialize();
        return array;
    }
}
