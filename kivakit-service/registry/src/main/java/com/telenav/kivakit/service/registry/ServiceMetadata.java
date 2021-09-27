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

package com.telenav.kivakit.service.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRegistry;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

/**
 * Metadata describing a {@link Service}.
 *
 * <p><b>Service Metadata</b></p>
 *
 * <p>
 * A service has the following metadata:
 * </p>
 *
 * <ul>
 *     <li>{@link #description()} - Description of the service</li>
 *     <li>{@link #version()} - The service version</li>
 *     <li>{@link #kivakitVersion()} - The KivaKit version that the service is running on</li>
 *     <li>{@link #contactEmail()} - An email to contact the service developer</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "Metadata describing a service")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@LexakaiJavadoc(complete = true)
public class ServiceMetadata
{
    @JsonProperty
    @OpenApiIncludeMember(description = "A description of the service")
    private String description;

    @JsonProperty
    @OpenApiIncludeMember(description = "The service version")
    @UmlAggregation(label = "service version")
    private Version version;

    @JsonProperty
    @OpenApiIncludeMember(description = "The version of the KivaKit that the service is running, if any")
    @UmlAggregation(label = "kivakit version")
    private Version kivakitVersion;

    @JsonProperty
    @OpenApiIncludeMember(description = "An email address to which concerns about the service can be directed")
    @UmlAggregation(label = "contact email")
    private EmailAddress contactEmail;

    public EmailAddress contactEmail()
    {
        return contactEmail;
    }

    public ServiceMetadata contactEmail(final EmailAddress contactEmail)
    {
        this.contactEmail = contactEmail;
        return this;
    }

    public ServiceMetadata description(final String description)
    {
        this.description = description;
        return this;
    }

    @KivaKitIncludeProperty
    public String description()
    {
        return description;
    }

    public ServiceMetadata kivakitVersion(final Version version)
    {
        kivakitVersion = version;
        return this;
    }

    public Version kivakitVersion()
    {
        return kivakitVersion;
    }

    public Version version()
    {
        return version;
    }

    public ServiceMetadata version(final Version version)
    {
        this.version = version;
        return this;
    }
}
