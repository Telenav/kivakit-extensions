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

import com.google.gson.Gson;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Performs serialization compatible with the Jersey REST API via *javax.ws.rs* interfaces.
 *
 * @author jonathanl (shibo)
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@LexakaiJavadoc(complete = true)
public class JerseyGsonSerializer<T> implements MessageBodyReader<T>, MessageBodyWriter<T>
{
    private Gson gson;

    public JerseyGsonSerializer()
    {
    }

    public JerseyGsonSerializer(GsonFactory factory)
    {
        gson = factory.gson();
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
            throws IOException, WebApplicationException
    {
        try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8))
        {
            return gson.fromJson(reader, type);
        }
    }

    @Override
    public void writeTo(T object, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream out)
            throws WebApplicationException
    {
        try (var writer = new PrintWriter(out))
        {
            var json = gson.toJson(object);
            writer.write(json);
            writer.flush();
        }
    }

    protected Gson gson()
    {
        return gson;
    }
}
