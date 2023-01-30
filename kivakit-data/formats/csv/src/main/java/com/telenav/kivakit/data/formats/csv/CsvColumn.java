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

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.name.Name;
import com.telenav.kivakit.data.formats.csv.internal.lexakai.DiagramCsv;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * A typesafe model of a CSV column that belongs to a {@link CsvSchema} and can convert values for the column between
 * strings and objects.
 *
 * <p>
 * {@link CsvColumn}s are added to a {@link CsvSchema} with {@link CsvSchema#CsvSchema(CsvColumn[])} and then used as a
 * key to retrieve values from {@link CsvLine}. This model includes the CSV schema that owns it, as well as the name and
 * index of the column in the data. A {@link StringConverter} converts values for the column to and from objects.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCsv.class)
@LexakaiJavadoc(complete = true)
public class CsvColumn<T> extends Name
{
    public static <T> CsvColumn<T> csvColumn(String name)
    {
        return csvColumn(name, null);
    }

    public static <T> CsvColumn<T> csvColumn(String name, StringConverter<T> converter)
    {
        return new CsvColumn<>(name, converter);
    }

    /** The zero-based index of the column in the CSV schema */
    private int index;

    /** The schema that this column has been added to */
    private CsvSchema schema;

    /** The converter to convert this column to and from its type */
    private final StringConverter<T> converter;

    /**
     * Construct the named column
     */
    protected CsvColumn(String name, StringConverter<T> converter)
    {
        super(name);
        this.converter = converter;
    }

    /**
     * Returns the text for the given value in this column
     */
    public String asString(T value, StringConverter<T> converter)
    {
        ensureNotNull(converter);
        return converter.unconvert(value);
    }

    /**
     * Returns the text for the given value in this column
     */
    public String asString(T value)
    {
        return asString(value, converter);
    }

    /**
     * Returns the value of the given text if it is in this column
     */
    public ObjectList<T> asType(String text, BaseStringConverter<T> converter)
    {
        ensureNotNull(converter);
        return converter.convertToList(text, ",");
    }

    /**
     * Returns the value of the given text if it is in this column
     */
    public T asType(String text, StringConverter<T> converter)
    {
        ensureNotNull(converter);
        return converter.convert(text);
    }

    /**
     * Returns the value of the given text if it is in this column
     */
    public T asType(String text)
    {
        return asType(text, converter);
    }

    /**
     * Returns the schema that defines this column
     */
    public CsvSchema schema()
    {
        return schema;
    }

    public Class<T> type()
    {
        return converter.toType();
    }

    /**
     * Returns the index of this column in the schema that references it
     */
    int index()
    {
        return index;
    }

    /**
     * Sets the index for this column
     */
    void index(int index)
    {
        this.index = index;
    }

    /**
     * Sets the schema for this column
     */
    void schema(CsvSchema schema)
    {
        if (this.schema != null)
        {
            throw new IllegalStateException("Column " + this + " has already been added to schema " + this.schema);
        }
        this.schema = schema;
    }
}
