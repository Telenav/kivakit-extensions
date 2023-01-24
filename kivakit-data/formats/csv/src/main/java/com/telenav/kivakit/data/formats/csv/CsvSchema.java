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

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.data.formats.csv.internal.lexakai.DiagramCsv;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.List;

import static com.telenav.kivakit.core.collections.map.StringMap.KeyCaseSensitivity.FOLD_CASE_LOWER;
import static com.telenav.kivakit.core.collections.map.StringMap.KeyCaseSensitivity.FOLD_CASE_UPPER;

/**
 * An ordered collection of {@link CsvColumn} objects, specifying the structure of a line in a CSV (Comma Separated
 * Data) file. {@link CsvColumn} objects are passed to the variable arguments constructor or added with
 * {@link #add(CsvColumn)}. Columns are assigned indexes in the order that they are added. The columns can then be
 * retrieved by name with {@link #columnForName(String)}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" }) @UmlClassDiagram(diagram = DiagramCsv.class)
@LexakaiJavadoc(complete = true)
public class CsvSchema extends BaseRepeater
{
    public static CsvSchema csvSchema(CsvColumn<?>... columns)
    {
        return new CsvSchema(columns);
    }

    /** Columns by name */
    private final StringMap<CsvColumn<?>> columnForName = new StringMap<>(Maximum._1_000);

    /** List of columns */
    @UmlAggregation
    private final ObjectList<CsvColumn<?>> columns = new ObjectList<>(Maximum._1_000);

    /** Column set for contains testing */
    private final ObjectSet<CsvColumn<?>> included = new ObjectSet<>(Maximum._1_000);

    /** The column index to assign to the next added column */
    private int index;

    /**
     * Constructs a schema with the given columns
     */
    protected CsvSchema(CsvColumn<?>... columns)
    {
        addAll(columns);
    }

    /**
     * Adds a column to this schema
     */
    public CsvSchema add(CsvColumn<?> column)
    {
        if (column != null)
        {
            column.schema(this);
            column.index(index++);
            columnForName.put(column.name(), column);
            columns.add(column);
            included.add(column);
        }
        return this;
    }

    /**
     * Adds a list of columns to this schema
     */
    public CsvSchema addAll(List<? extends CsvColumn<?>> columns)
    {
        for (CsvColumn<?> column : columns)
        {
            add(column);
        }
        return this;
    }

    /**
     * Adds the given columns to this schema
     */
    public CsvSchema addAll(CsvColumn<?>... columns)
    {
        for (var column : columns)
        {
            add(column);
        }
        return this;
    }

    /**
     * Returns the named column or null if no such column exists
     */
    public CsvColumn<?> columnForName(String name)
    {
        return columnForName.get(name);
    }

    public ObjectList<CsvColumn<?>> columns()
    {
        return columns;
    }

    /**
     * Returns true if this schema contains the given column
     */
    public boolean contains(CsvColumn<?> column)
    {
        return included.contains(column);
    }

    /**
     * Returns the columns in this schema joined by commas
     */
    @Override
    public String toString()
    {
        return columns.join(",");
    }
}
