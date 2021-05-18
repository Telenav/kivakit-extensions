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

package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.kernel.language.values.identifier.Identifier;
import com.telenav.kivakit.kernel.language.values.identifier.IdentifierFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.Objects;

/**
 * Identifies a stream between the proxy server and a client
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
class StreamHandle
{
    private static final IdentifierFactory identifiers = new IdentifierFactory();

    public static StreamHandle create()
    {
        return new StreamHandle(identifiers.newInstance());
    }

    public static StreamHandle of(final long identifier)
    {
        return new StreamHandle(new Identifier(identifier));
    }

    private final Identifier identifier;

    private StreamHandle(final Identifier identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof StreamHandle)
        {
            final StreamHandle that = (StreamHandle) object;
            return identifier.equals(that.identifier);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(identifier);
    }

    public Identifier identifier()
    {
        return identifier;
    }
}
