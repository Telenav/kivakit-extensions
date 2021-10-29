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

package com.telenav.kivakit.primitive.collections.map.multi;

import com.telenav.kivakit.kernel.interfaces.collection.Keyed;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.Indent;
import com.telenav.kivakit.primitive.collections.PrimitiveCollection;
import com.telenav.kivakit.primitive.collections.iteration.PrimitiveIterator;
import com.telenav.kivakit.primitive.collections.map.PrimitiveMap;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public abstract class PrimitiveMultiMap extends PrimitiveMap
{
    protected interface MultiMapToString
    {
        String toString(long key, PrimitiveIterator value);
    }

    protected PrimitiveMultiMap(String name)
    {
        super(name);
    }

    protected PrimitiveMultiMap()
    {
    }

    @Override
    protected final void copyEntries(PrimitiveMap that, ProgressReporter reporter)
    {
        unsupported();
    }

    @Override
    protected final PrimitiveMap newMap()
    {
        return unsupported();
    }

    protected String toString(PrimitiveIterator keys, Keyed<Long, PrimitiveIterator> values,
                              MultiMapToString toStringer)
    {
        return Indent.by(4, toString(keys, values, ", ", 5, "\n", toStringer));
    }

    protected String toString(PrimitiveIterator keys, Keyed<Long, PrimitiveIterator> values,
                              String separator, int every, String section,
                              MultiMapToString toStringer)
    {
        var count = Math.min(size(), PrimitiveCollection.TO_STRING_MAXIMUM_ELEMENTS);
        var builder = new StringBuilder();
        if (keys != null && keys.hasNext() && values != null)
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
                var value = values.get(key);
                builder.append(toStringer.toString(key, value));
            }
        }
        else
        {
            builder.append("null");
        }
        if (size() > PrimitiveCollection.TO_STRING_MAXIMUM_ELEMENTS)
        {
            builder.append(separator);
            builder.append("[...]");
        }
        return builder.toString();
    }
}
