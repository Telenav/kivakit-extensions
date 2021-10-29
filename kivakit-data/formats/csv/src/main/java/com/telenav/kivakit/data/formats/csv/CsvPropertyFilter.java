package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        return schema.columnForName(method.getName()) != null;
    }

    @Override
    public boolean includeAsSetter(Method method)
    {
        return schema.columnForName(method.getName()) != null;
    }

    @Override
    public boolean includeField(Field field)
    {
        return schema.columnForName(field.getName()) != null;
    }

    @Override
    public String nameForField(Field field)
    {
        return field.getName();
    }

    @Override
    public String nameForMethod(Method method)
    {
        return method.getName();
    }
}
