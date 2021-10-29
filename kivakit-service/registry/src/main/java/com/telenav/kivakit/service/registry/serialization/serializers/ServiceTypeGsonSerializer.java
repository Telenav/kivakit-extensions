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

package com.telenav.kivakit.service.registry.serialization.serializers;

import com.telenav.kivakit.serialization.json.PrimitiveGsonSerializer;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Serializes {@link ServiceType}s to and from JSON.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceTypeGsonSerializer extends PrimitiveGsonSerializer<ServiceType, String>
{
    public ServiceTypeGsonSerializer()
    {
        super(String.class);
    }

    @Override
    protected ServiceType toObject(String identifier)
    {
        return new ServiceType(identifier);
    }

    @Override
    protected String toPrimitive(ServiceType type)
    {
        return type.identifier();
    }
}
