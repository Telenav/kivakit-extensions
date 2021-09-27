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

import com.telenav.kivakit.kernel.language.values.identifier.StringIdentifier;
import com.telenav.kivakit.microservice.rest.microservlet.openapi.OpenApiIncludeType;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRegistry;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * An identifier for a particular kind of service
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(description = "A value that uniquely identifies the type of service. "
        + "The use of domain-name qualified names is encouraged to avoid conflicts.")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
public class ServiceType extends StringIdentifier
{
    public ServiceType(final String identifier)
    {
        super(identifier);
    }

    protected ServiceType()
    {
    }
}
