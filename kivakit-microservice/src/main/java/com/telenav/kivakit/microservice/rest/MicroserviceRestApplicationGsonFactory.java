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

package com.telenav.kivakit.microservice.rest;

import com.google.gson.GsonBuilder;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.serialization.json.serializers.ProblemGsonSerializer;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;

/**
 * Factory for GSON serializers
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public class MicroserviceRestApplicationGsonFactory extends GsonFactory implements ComponentMixin
{
    @Override
    protected final GsonBuilder addSerializers(final GsonBuilder builder)
    {
        addSerializer(builder, Problem.class, new ProblemGsonSerializer(WITHOUT_EXCEPTION));

        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        builder.disableHtmlEscaping();

        onAddSerializers(builder);

        builder.setPrettyPrinting();

        return builder;
    }

    protected GsonBuilder onAddSerializers(final GsonBuilder builder)
    {
        return builder;
    }
}
