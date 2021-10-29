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

public class SplitLongArrayTest extends PrimitiveCollectionsUnitTest
{
    @Test
    public void testAdd()
    {
        var array = array();
        array.hasNullLong(false);
        var values = randomLongList(ALLOW_REPEATS);
        values.forEach(array::add);
        resetIndex();
        values.forEach(value -> ensureEqual(array.get(nextIndex()), value));
    }

    @Before
    public void testBefore()
    {
        iterations(1_000);
    }

    @Test
    public void testClear()
    {
        var array = array();
        array.hasNullLong(false);
        randomLongs(NO_REPEATS, Count.count(250), array::add);
        randomIndexes(NO_REPEATS, Count.count(250), index ->
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
        var map = new HashMap<SplitLongArray, Integer>();
        loop(() ->
        {
            var array = array();
            randomLongs(ALLOW_REPEATS, Count._16, array::add);
            map.put(array, 99);
            ensureEqual(99, map.get(array));
        });
    }

    @Test
    public void testFirstLast()
    {
        var array = array();

        ensureThrows(array::first);
        ensureThrows(array::last);

        var last = new MutableInteger(Integer.MIN_VALUE);

        resetIndex();
        randomLongs(ALLOW_REPEATS, value ->
        {
            var index = nextIndex();
            array.set(index, value);
            last.maximum(index);
            ensureEqual(array.get(0), array.first());
            ensureEqual(array.get(last.get()), array.last());
        });
    }

    @Test
    public void testGetSet()
    {
        {
            var array = array();

            resetIndex();
            randomLongs(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });

            resetIndex();
            randomLongs(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });
        }
        {
            var array = array();
            array.nullLong(-1);
            randomLongs(NO_REPEATS, value -> value != -1, array::add);
            array.safeGet(0);
            loop(() ->
            {
                var index = randomIndex();
                var value = array.safeGet(index);
                ensureEqual(index >= array.size(), array.isNull(value));
            });
        }
    }

    @Test
    public void testIsNull()
    {
        var array = array();
        var nullValue = randomValueFactory().newLong();
        array.nullLong(nullValue);
        ensure(array.hasNullLong());
        resetIndex();
        randomLongs(ALLOW_REPEATS, value -> value != array.nullLong(), value ->
        {
            var index = nextIndex();

            array.set(index, value);
            ensure(!array.isNull(array.get(index)));

            array.set(index, array.nullLong());
            ensure(array.isNull(array.get(index)));
        });
    }

    @Test
    public void testIteration()
    {
        var array = array();

        array.add(0);
        array.add(1);
        array.add(2);
        array.set(100, 100);

        var values = array.iterator();
        ensureEqual((long) 0, values.next());
        ensureEqual((long) 1, values.next());
        ensureEqual((long) 2, values.next());
        ensureEqual((long) 100, values.next());
        ensure(!values.hasNext());

        array.hasNullLong(false);

        values = array.iterator();
        ensureEqual((long) 0, values.next());
        ensureEqual((long) 1, values.next());
        ensureEqual((long) 2, values.next());
        ensureEqual(Long.MIN_VALUE, values.next());
        ensure(values.hasNext());
    }

    @Test
    public void testSerialization()
    {
        var array = array();
        randomLongs(ALLOW_REPEATS, array::add);
        serializationTest(array);
    }

    @Test
    public void testSizeIsEmpty()
    {
        {
            var array = array();

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
        }
        {
            var array = array();
            var maximum = new MutableInteger(Integer.MIN_VALUE);
            resetIndex();
            randomLongs(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                maximum.maximum(index);
                array.set(index, value);
                ensure(array.size() == maximum.get() + 1);
            });
        }
    }

    private SplitLongArray array()
    {
        var array = (SplitLongArray) new SplitLongArray("test")
                .nullLong(Long.MIN_VALUE)
                .initialChildSize(100);
        array.initialize();
        return array;
    }
}
