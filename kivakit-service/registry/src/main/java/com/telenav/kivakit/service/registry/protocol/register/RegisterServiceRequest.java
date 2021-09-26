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

package com.telenav.kivakit.service.registry.protocol.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.openapi.annotations.OpenApiIncludeType;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * A request from an application (usually a server) to register a service and begin leasing a port for it.
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "Request to register a service and receive a port lease")
@LexakaiJavadoc(complete = true)
public class RegisterServiceRequest extends BaseRequest
{
    /** The potentially unbound service to register or renew */
    @JsonProperty
    @OpenApiIncludeMember(description = "The service that should be registered and allocated a port")
    private Service service;

    public RegisterServiceRequest(final Service service)
    {
        this.service = service;
    }

    protected RegisterServiceRequest()
    {
    }

    @Override
    public String path()
    {
        return ServiceRegistryProtocol.REGISTER_SERVICE;
    }

    @KivaKitIncludeProperty
    public Service service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }
}
