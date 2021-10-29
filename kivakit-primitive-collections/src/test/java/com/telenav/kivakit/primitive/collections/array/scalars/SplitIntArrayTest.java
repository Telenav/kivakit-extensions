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

public class SplitIntArrayTest extends PrimitiveCollectionsUnitTest
{
    @Test
    public void testAdd()
    {
        var array = array();
        array.hasNullInt(false);
        var values = randomIntList(ALLOW_REPEATS);
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
        array.hasNullInt(false);
        randomInts(NO_REPEATS, Count.count(250), array::add);
        randomIndexes(NO_REPEATS, Count.count(250), index ->
        {
            ensure(!array.isNull(array.get(index)));
            array.clear(index);
            ensure(!array.isNull(array.get(index)));
        });

        array.nullInt(-1);
        randomInts(NO_REPEATS, value ->
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
        var map = new HashMap<SplitIntArray, Integer>();
        loop(() ->
        {
            var array = array();
            randomInts(ALLOW_REPEATS, Count._16, array::add);
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
        randomInts(ALLOW_REPEATS, value ->
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
            randomInts(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });

            resetIndex();
            randomInts(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                array.set(index, value);
                ensureEqual(array.get(index), value);
            });
        }
        {
            var array = new SplitIntArray("test");
            array.nullInt(-1);
            array.initialize();

            randomInts(NO_REPEATS, value -> value != -1, array::add);
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
        var nullValue = randomInt();
        array.nullInt(nullValue);
        ensure(array.hasNullInt());
        resetIndex();
        randomInts(ALLOW_REPEATS, value -> value != array.nullInt(), value ->
        {
            var index = nextIndex();

            array.set(index, value);
            ensure(!array.isNull(array.get(index)));

            array.set(index, array.nullInt());
            ensure(array.isNull(array.get(index)));
        });
    }

    @Test
    public void testIteration()
    {
        var array = array();
        array.hasNullInt(false);

        array.add(0);
        array.add(1);
        array.add(2);
        array.set(100, 100);

        var values = array.iterator();
        ensureEqual(0, values.next());
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(-1, values.next());
        ensure(values.hasNext());

        array.hasNullInt(true);
        array.nullInt(0);

        values = array.iterator();
        ensureEqual(1, values.next());
        ensureEqual(2, values.next());
        ensureEqual(-1, values.next());
        ensure(values.hasNext());
    }

    @Test
    public void testSerialization()
    {
        var array = array();
        randomInts(ALLOW_REPEATS, array::add);
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
            randomInts(ALLOW_REPEATS, value ->
            {
                var index = nextIndex();
                maximum.maximum(index);
                array.set(index, value);
                ensure(array.size() == maximum.get() + 1);
            });
        }
    }

    private SplitIntArray array()
    {
        var array = (SplitIntArray) new SplitIntArray("test")
                .nullInt(-1)
                .initialChildSize(100);
        array.initialize();
        return array;
    }
}
