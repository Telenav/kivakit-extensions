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

package com.telenav.kivakit.service.registry.protocol.renew;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.protocol.BaseRequest;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;
import static com.telenav.kivakit.service.registry.protocol.ServiceRegistryProtocol.RENEW_SERVICE;

/**
 * A request from an application to renew the lease for a service
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "Request to renew a service's lease on a port")
@LexakaiJavadoc(complete = true)
public class RenewServiceRequest extends BaseRequest
{
    /** The service to renew */
    @JsonProperty
    @OpenApiIncludeMember(description = "The service to renew")
    private Service service;

    public RenewServiceRequest(final Service service)
    {
        this.service = service;
    }

    protected RenewServiceRequest()
    {
    }

    @Override
    public String path()
    {
        return RENEW_SERVICE;
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
