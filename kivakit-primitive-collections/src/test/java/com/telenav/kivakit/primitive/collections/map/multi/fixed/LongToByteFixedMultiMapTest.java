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

import com.telenav.kivakit.primitive.collections.array.scalars.ByteArray;
import com.telenav.kivakit.primitive.collections.project.PrimitiveCollectionsUnitTest;
import org.junit.Test;

public class LongToByteFixedMultiMapTest extends PrimitiveCollectionsUnitTest
{
    @Test
    public void test()
    {
        var map = new LongToByteFixedMultiMap("map");
        map.initialize();
        var bytes = new ByteArray("bytes");
        bytes.initialize();
        bytes.add((byte) 1);
        bytes.add((byte) 2);
        bytes.add((byte) 3);
        map.putAll(1L, bytes);
        map.putAll(2L, bytes);
        map.putAll(3L, bytes);
        ensureEqual(bytes, map.get(1L));
        ensureEqual(bytes, map.get(2L));
        ensureEqual(bytes, map.get(3L));
    }
}
