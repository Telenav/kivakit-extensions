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

package com.telenav.kivakit.web.jersey;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.resources.InputResource;
import com.telenav.kivakit.resource.resources.OutputResource;
import com.telenav.kivakit.resource.serialization.SerializableObject;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Performs JSON serialization compatible with the Jersey REST API via <i>javax.ws.rs</i> interfaces.
 *
 * @author jonathanl (shibo)
 */
@Provider
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class JerseyJsonSerializer<T> implements
        MessageBodyReader<T>,
        MessageBodyWriter<T>
{
    private final GsonObjectSerializer serializer;

    public JerseyJsonSerializer(GsonObjectSerializer serializer)
    {
        this.serializer = serializer;
    }

    @Override
    public long getSize(T object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType)
    {
        return -1;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType)
    {
        return true;
    }

    @Override
    public T readFrom(Class<T> type, Type genericType,
                      Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> map, InputStream in)
            throws WebApplicationException
    {
        return serializer.readObject(new InputResource(in), type).object();
    }

    @Override
    public void writeTo(T object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream out)
            throws WebApplicationException
    {
        serializer.writeObject(new OutputResource(out), new SerializableObject<>(object));
    }
}
