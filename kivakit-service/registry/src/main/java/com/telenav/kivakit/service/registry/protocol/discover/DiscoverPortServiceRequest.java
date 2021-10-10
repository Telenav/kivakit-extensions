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

package com.telenav.kivakit.service.registry.protocol.discover;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.microservlet.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.network.core.Port;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.DISCOVER_PORT_SERVICE;

/**
 * Looks up the service on a particular port. Note that {@link Port} includes the host so it uniquely identifies one and
 * only one service.
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "A request to determine what service, if any, is running on the given host and port.")
@UmlClassDiagram(diagram = DiagramRest.class)
@LexakaiJavadoc(complete = true)
public class DiscoverPortServiceRequest extends BaseRequest
{
    @JsonProperty
    @OpenApiIncludeMember(description = "The port to examine")
    private Port port;

    @Override
    public String path()
    {
        return DISCOVER_PORT_SERVICE;
    }

    public DiscoverPortServiceRequest port(final Port port)
    {
        this.port = port;
        return this;
    }

    @KivaKitIncludeProperty
    public Port port()
    {
        return port;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }
}
