package com.telenav.kivakit.microservice;

import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;

public class MicroserviceSettings
{
    @KivaKitPropertyConverter(IntegerConverter.class)
    private int port;

    int port()
    {
        return port;
    }
}
