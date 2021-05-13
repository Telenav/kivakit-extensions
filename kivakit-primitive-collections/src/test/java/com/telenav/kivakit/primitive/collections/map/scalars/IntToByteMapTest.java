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

package com.telenav.kivakit.primitive.collections.map.scalars;

import com.telenav.kivakit.primitive.collections.project.PrimitiveCollectionsUnitTest;
import com.telenav.kivakit.kernel.language.collections.CompressibleCollection;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static com.telenav.kivakit.test.UnitTest.Repeats.ALLOW_REPEATS;
import static com.telenav.kivakit.test.UnitTest.Repeats.NO_REPEATS;

public class IntToByteMapTest extends PrimitiveCollectionsUnitTest
{
    @FunctionalInterface
    interface MapTest
    {
        void test(IntToByteMap map, List<Integer> keys, List<Byte> values);
    }

    @Test
    public void testClear()
    {
        iterations(10);
        withPopulatedMap((map, keys, values) ->
        {
            ensureFalse(map.isEmpty());
            map.clear();
            ensure(map.isEmpty());
        });
    }

    @Test
    public void testContainsKey()
    {
        withPopulatedMap((map, keys, values) -> keys.forEach(key -> ensure(map.containsKey(key))));
    }

    @Test
    public void testEqualsHashCode()
    {
        withPopulatedMap((a, keys, values) ->
        {
            final var b = map();
            putAll(b, keys, values);
            ensureEqual(a, b);
            b.put(99, (byte) -1);
            ensureNotEqual(a, b);
        });
    }

    @Test
    public void testFreeze()
    {
        withPopulatedMap((a, keys, values) ->
        {
            final var b = map();
            putAll(b, keys, values);
            ensureEqual(a, b);
            b.compress(CompressibleCollection.Method.FREEZE);
            ensureEqual(a, b);
        });
        withPopulatedMap((a, keys, values) ->
        {
            final var b = map();
            putAll(b, keys, values);
            putAll(b, keys, values);
            ensureEqual(a, b);
            b.compress(CompressibleCollection.Method.FREEZE);
            ensureEqual(a, b);
        });
    }

    @Test
    public void testGetPut()
    {
        withPopulatedMap((map, keys, values) ->
        {
            final var iterator = values.iterator();
            keys.forEach(key ->
            {
                ensure(map.containsKey(key));
                final var mapValue = map.get(key);
                final var expectedValue = iterator.next();
                ensureEqual(expectedValue, mapValue);
            });
        });
    }

    @Test
    public void testKeys()
    {
        withPopulatedMap((map, keys, values) ->
        {
            final var iterator = map.keys();
            int count = 0;
            while (iterator.hasNext())
            {
                final var key = iterator.next();
                ensure(map.containsKey(key));
                count++;
            }
            ensureEqual(map.size(), count);
        });
    }

    @Test
    public void testRemove()
    {
        withPopulatedMap((map, keys, values) ->
                keys.forEach(key ->
                {
                    final int size = map.size();
                    final boolean exists = map.containsKey(key);
                    map.remove(key);
                    ensure(map.isEmpty(map.get(key)));
                    if (exists)
                    {
                        ensureEqual(map.size(), size - 1);
                    }
                }));
    }

    @Test
    public void testSerialization()
    {
        withPopulatedMap((map, keys, values) -> serializationTest(map));
    }

    @Test
    public void testValues()
    {
        withPopulatedMap((map, keys, values) ->
        {
            final var iterator = map.values();
            int count = 0;
            final var valueSet = new HashSet<>(values);
            while (iterator.hasNext())
            {
                final var value = iterator.next();
                ensure(valueSet.contains(value));
                count++;
            }
            ensureEqual(map.size(), count);
        });
    }

    private IntToByteMap map()
    {
        return (IntToByteMap) new IntToByteMap("test").nullInt(Integer.MIN_VALUE).nullByte(Byte.MIN_VALUE).initialize();
    }

    private void putAll(final IntToByteMap map, final List<Integer> keys, final List<Byte> values)
    {
        resetIndex();
        keys.forEach(key -> map.put(key, values.get(nextIndex() % values.size())));
    }

    private void withPopulatedMap(final MapTest test)
    {
        final var map = map();
        final var keys = randomIntList(NO_REPEATS);
        final var values = randomByteList(ALLOW_REPEATS);
        putAll(map, keys, values);
        test.test(map, keys, values);
    }
}
