package com.telenav.kivakit.microservice.protocols.rest.http;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.microservice.microservlet.MicroservletResponse;

import static com.telenav.kivakit.core.collections.map.ObjectMap.map;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

/**
 * Inspects and stores {@link RestSerializer} instances for types.
 *
 * @author Jonathan locke
 */
public class RestSerializers
{
    private static final ObjectMap<Class<?>, RestSerializer<?, ?>> typeToSerializer = map();

    private static final ObjectMap<Class<?>, Boolean> inspected = map();

    /**
     * Returns any REST serializer defined for the given type. Types can declare a {@link RestSerializer} by defining
     * <i>public static RestSerializer restSerializer()</i>.
     *
     * @param requestType The type
     * @return Any {@link RestSerializer} defined by the type
     */
    @SuppressWarnings("unchecked")
    public static <Request extends MicroservletRequest, Response extends MicroservletResponse>
    RestSerializer<Request, Response> restSerializer(Class<?> requestType)
    {
        RestSerializer<Request, Response> serializer = (RestSerializer<Request, Response>) typeToSerializer.get(requestType);
        if (serializer == null)
        {
            if (inspected.get(requestType) == null)
            {
                try
                {
                    var method = requestType.getDeclaredMethod("restSerializer");
                    if ((method.getModifiers() & (STATIC | PUBLIC)) == (STATIC | PUBLIC))
                    {
                        serializer = (RestSerializer<Request, Response>) method.invoke(null);
                    }
                }
                catch (Exception ignored)
                {
                }

                typeToSerializer.putIfNotNull(requestType, serializer);
                inspected.put(requestType, true);
            }
        }
        return serializer;
    }
}
