package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.language.reflection.Field;
import com.telenav.kivakit.core.language.reflection.Method;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * A {@link PropertyFilter} that uses a {@link CsvSchema} to filter methods and fields to those whose names match
 * columns in the schema. This property filter uses KivaKit naming conventions for getters and setters, without Java
 * Beans prefixes. For example, <i>void text(String)</i> and <i>String text()</i>.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class CsvPropertyFilter implements PropertyFilter
{
    private final CsvSchema schema;

    public CsvPropertyFilter(CsvSchema schema)
    {
        this.schema = schema;
    }

    @Override
    public boolean includeAsGetter(Method method)
    {
        return schema.columnForName(method.name()) != null;
    }

    @Override
    public boolean includeAsSetter(Method method)
    {
        return schema.columnForName(method.name()) != null;
    }

    @Override
    public boolean includeField(Field field)
    {
        return schema.columnForName(field.name()) != null;
    }

    @Override
    public String nameForField(Field field)
    {
        return field.name();
    }

    @Override
    public String nameForMethod(Method method)
    {
        return method.name();
    }
}
