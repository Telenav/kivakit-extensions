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

package com.telenav.kivakit.service.registry.serialization;

import com.google.gson.GsonBuilder;
import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.serialization.json.serializers.ProblemGsonSerializer;
import com.telenav.kivakit.serialization.json.serializers.TimeInMillisecondsGsonSerializer;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.serialization.serializers.ApplicationIdentifierGsonSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.ServiceTypeGsonSerializer;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * Factory for GSON serializers
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistryGsonFactory extends GsonFactory
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    protected GsonBuilder addSerializers(final GsonBuilder builder)
    {
        addSerializer(builder, Port.class, serializer(new Port.Converter(LOGGER)));
        addSerializer(builder, Application.Identifier.class, new ApplicationIdentifierGsonSerializer());
        addSerializer(builder, ServiceType.class, new ServiceTypeGsonSerializer());
        addSerializer(builder, Problem.class, new ProblemGsonSerializer(WITH_EXCEPTION));
        addSerializer(builder, Time.class, new TimeInMillisecondsGsonSerializer());
        return builder;
    }
}
