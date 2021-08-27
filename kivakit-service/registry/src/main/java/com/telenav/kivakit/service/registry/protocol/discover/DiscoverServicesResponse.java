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
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * The set of services that were found for a {@link DiscoverServicesRequest}.
 *
 * @author jonathanl (shibo)
 */
@Schema
@UmlClassDiagram(diagram = DiagramRest.class)
@LexakaiJavadoc(complete = true)
public class DiscoverServicesResponse extends BaseResponse<Set<Service>>
{
    @JsonProperty
    private Set<Service> services = new HashSet<>();

    public Service service()
    {
        return Sets.first(services);
    }

    public DiscoverServicesResponse services(final Set<Service> services)
    {
        this.services = services;
        return this;
    }

    @KivaKitIncludeProperty
    public Set<Service> services()
    {
        return services;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Set<Service> value)
    {
        services = value;
    }

    @Override
    protected Set<Service> value()
    {
        return services();
    }
}
