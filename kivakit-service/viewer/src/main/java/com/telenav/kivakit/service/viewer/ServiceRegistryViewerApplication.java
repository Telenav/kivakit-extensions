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

package com.telenav.kivakit.service.viewer;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.network.core.project.NetworkCoreProject;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.Scope.Type;
import com.telenav.kivakit.service.registry.ServiceMetadata;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.client.ServiceRegistryClient;

import java.util.Set;

import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;
import static com.telenav.kivakit.service.registry.Scope.Type.scopeTypeSwitchParser;

/**
 * Identifier to view the KivaKit services on a machine.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class ServiceRegistryViewerApplication extends Application
{
    public static void main(final String[] arguments)
    {
        new ServiceRegistryViewerApplication().run(arguments);
    }

    private final SwitchParser<Type> SCOPE_TYPE = scopeTypeSwitchParser().build();

    private final Debug DEBUG = new Debug(this);

    private ServiceRegistryViewerApplication()
    {
        super(NetworkCoreProject.get());
    }

    @Override
    protected void onRun()
    {
        showCommandLine();

        // Connect to service registry server
        final var client = listenTo(new ServiceRegistryClient());

        // register a server log
        if (DEBUG.isDebugOn())
        {
            final var metadata = new ServiceMetadata()
                    .description("Server log for " + identifier())
                    .kivakitVersion(KivaKit.get().kivakitVersion())
                    .version(KivaKit.get().projectVersion());
            final var serverLog = client.register(Scope.localhost(), new ServiceType("kivakit-server-log"), metadata);
            System.out.println("Registered service " + serverLog);
        }

        // and show available services
        final var services = client.discoverServices(Scope.scope(get(SCOPE_TYPE)));
        if (services.failed())
        {
            Message.println("\nUnable to find services: $\n", services.why().formatted(WITH_EXCEPTION));
        }
        else
        {
            final var lines = new StringList();
            final var format = "%-24s %-8s %-32s %-48s %s";
            lines.add("");
            lines.add(String.format(format, "renewed", "port", "service", "application", "description"));
            lines.add(AsciiArt.line(200));
            final var sorted = ObjectList.objectList(services.get()).sorted();
            for (final var service : sorted)
            {
                lines.add(String.format(format, service.renewedAt().elapsedSince() + " ago", service.port().number(), service.type(), service.application(), service.metadata().description()));
            }
            lines.add("");
            System.out.println(lines.join("\n"));
        }
        System.out.flush();
    }

    @Override
    protected Set<SwitchParser<?>> switchParsers()
    {
        return Set.of(SCOPE_TYPE);
    }
}
