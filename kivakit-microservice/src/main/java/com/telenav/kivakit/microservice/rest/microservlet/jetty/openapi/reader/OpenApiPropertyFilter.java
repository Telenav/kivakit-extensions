package com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.reader;

import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class OpenApiPropertyFilter implements PropertyFilter
{
    @Override
    public boolean includeAsGetter(final Method method)
    {
        return method.annotation();
    }

    @Override
    public boolean includeAsSetter(final Method method)
    {
        return false;
    }

    @Override
    public boolean includeField(final Field field)
    {
        return false;
    }

    @Override
    public String nameForField(final Field field)
    {
        return null;
    }

    @Override
    public String nameForMethod(final Method method)
    {
        return null;
    }
}
