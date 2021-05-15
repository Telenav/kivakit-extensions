////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
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
import com.telenav.kivakit.application.ApplicationIdentifier;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRest;
import com.telenav.kivakit.service.registry.protocol.BaseResponse;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter.Format.MULTILINE;

/**
 * The set of applications found for an {@link DiscoverApplicationsRequest}.
 *
 * @author jonathanl (shibo)
 */
@Schema(description = "The applications discovered by a DiscoverApplicationsRequest")
@UmlClassDiagram(diagram = DiagramRest.class)
@LexakaiJavadoc(complete = true)
public class DiscoverApplicationsResponse extends BaseResponse<Set<ApplicationIdentifier>>
{
    @JsonProperty
    @Schema(description = "The applications that were found")
    private Set<ApplicationIdentifier> applications = new HashSet<>();

    @KivaKitIncludeProperty
    public Set<ApplicationIdentifier> applications()
    {
        return applications;
    }

    public DiscoverApplicationsResponse applications(final Set<ApplicationIdentifier> applications)
    {
        this.applications = applications;
        return this;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString(MULTILINE);
    }

    @Override
    protected void value(final Set<ApplicationIdentifier> value)
    {
        applications = value;
    }

    @Override
    protected Set<ApplicationIdentifier> value()
    {
        return applications();
    }
}