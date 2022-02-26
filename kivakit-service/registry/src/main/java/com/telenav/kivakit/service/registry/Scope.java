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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.Collections;
import com.telenav.kivakit.kernel.language.monads.Result;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeMember;
import com.telenav.kivakit.microservice.protocols.rest.openapi.OpenApiIncludeType;
import com.telenav.kivakit.network.core.cluster.ClusterIdentifier;
import com.telenav.kivakit.service.registry.project.lexakai.diagrams.DiagramRegistry;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.service.registry.Scope.Type.CLUSTER;
import static com.telenav.kivakit.service.registry.Scope.Type.LOCALHOST;
import static com.telenav.kivakit.service.registry.Scope.Type.NETWORK;

/**
 * Specifies a visibility and search scope for registering and discovering services.
 *
 * @author jonathanl (shibo)
 */
@OpenApiIncludeType(
        description = "The scope of visibility of a service, used to limit what services are discovered by a locate request.")
@UmlClassDiagram(diagram = DiagramRegistry.class)
@UmlExcludeSuperTypes
@LexakaiJavadoc(complete = true)
public class Scope implements Named, Comparable<Scope>
{
    public static Scope cluster(ClusterIdentifier cluster)
    {
        return new Scope(CLUSTER, cluster);
    }

    /**
     * @return The local host scope
     */
    public static Scope localhost()
    {
        return new Scope(LOCALHOST, null);
    }

    public static List<String> names(Result<Set<Service>> result)
    {
        return Collections.sorted(scopes(result)
                .stream()
                .map(Scope::name)
                .collect(Collectors.toSet()));
    }

    /**
     * @return The network-wide scope
     */
    public static Scope network()
    {
        return new Scope(NETWORK, null);
    }

    public static Scope scope(Scope.Type type)
    {
        return new Scope(type, null);
    }

    public static Set<Scope> scopes(Result<Set<Service>> result)
    {
        var scopes = new HashSet<Scope>();
        if (result.succeeded())
        {
            scopes.addAll(result.get()
                    .stream()
                    .map(Service::scope)
                    .collect(Collectors.toSet()));
        }
        return scopes;
    }

    /**
     * The type of scope
     */
    @OpenApiIncludeType(description = "The type of scope")
    @UmlClassDiagram(diagram = DiagramRegistry.class)
    @LexakaiJavadoc(complete = true)
    public enum Type
    {
        /** Services on the local host */
        LOCALHOST,

        /** Services on a cluster */
        CLUSTER,

        /** Services anywhere on the network */
        NETWORK;

        public static SwitchParser.Builder<Type> scopeTypeSwitchParser(Listener listener)
        {
            return SwitchParser
                    .enumSwitchParser(listener, "scope", "The scope to search", Type.class)
                    .optional()
                    .defaultValue(LOCALHOST);
        }
    }

    /** The name of a cluster, if any */
    @JsonProperty
    @OpenApiIncludeMember(description = "A cluster identifier, if the scope type is CLUSTER")
    @UmlAggregation
    private ClusterIdentifier cluster;

    /** The scope type, either local, a particular host, a cluster or the whole network */
    @JsonProperty
    @OpenApiIncludeMember(description = "The type of scope")
    @UmlAggregation
    private Type type;

    protected Scope(Type type, ClusterIdentifier cluster)
    {
        ensure(type != CLUSTER || cluster != null);

        this.type = type;
        this.cluster = cluster;
    }

    protected Scope()
    {
    }

    @KivaKitIncludeProperty
    public ClusterIdentifier cluster()
    {
        return cluster;
    }

    @Override
    public int compareTo(@NotNull Scope that)
    {
        return name().compareTo(that.name());
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Scope)
        {
            Scope that = (Scope) object;
            return name().equals(that.name());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name());
    }

    @JsonIgnore
    public boolean isCluster()
    {
        return type == Type.CLUSTER;
    }

    @JsonIgnore
    public boolean isLocal()
    {
        return type == LOCALHOST;
    }

    @JsonIgnore
    public boolean isNetwork()
    {
        return type == Type.NETWORK;
    }

    @Override
    public String name()
    {
        switch (type)
        {
            case NETWORK:
                return "network";

            case LOCALHOST:
                return "localhost";

            case CLUSTER:
                return "cluster '" + cluster.identifier() + "'";

            default:
                return unsupported();
        }
    }

    @Override
    public String toString()
    {
        return name();
    }

    @KivaKitIncludeProperty
    public Type type()
    {
        return type;
    }
}
