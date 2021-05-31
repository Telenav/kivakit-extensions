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

package com.telenav.kivakit.logs.viewer;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.logs.client.ClientLog;
import com.telenav.kivakit.logs.server.ServerLog;
import com.telenav.kivakit.logs.server.project.LogsServerProject;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.kivakit.ui.desktop.graphics.image.ImageResource;

import java.awt.Taskbar;
import java.util.Set;

/**
 * Application to view remote {@link ServerLog}s.
 *
 * @author jonathanl (shibo)
 */
public class LogViewerApplication extends Application
{
    public static void main(final String[] arguments)
    {
        new LogViewerApplication().run(arguments);
    }

    private final SwitchParser<Maximum> MAXIMUM_ENTRIES =
            SwitchParser.maximumSwitch("maximum-entries", "The maximum number of entries to keep at a time")
                    .optional()
                    .defaultValue(Maximum.maximum(20_000))
                    .build();

    private LogViewerApplication()
    {
        super(LogsServerProject.get());
    }

    @SuppressWarnings("resource")
    @Override
    protected void onRun()
    {
        final var icon = ImageResource.of(getClass(), "tdk-icon-128.png").image();
        Taskbar.getTaskbar().setIconImage(icon);

        final var configuration = PropertyMap.create();
        configuration.put("maximum-entries", get(MAXIMUM_ENTRIES).toString());

        // Create and show client log
        final var log = new ClientLog();
        log.configure(configuration);
        log.show("TDK - Log Viewer", icon);
    }

    @Override
    protected Set<SwitchParser<?>> switchParsers()
    {
        return Set.of(MAXIMUM_ENTRIES, QUIET);
    }
}
