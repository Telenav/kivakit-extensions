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
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.logs.server.ServerLog;
import com.telenav.kivakit.logs.server.project.LogsServerProject;

/**
 * Application to view remote {@link ServerLog}s.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("InfiniteLoopStatement")
public class LogViewerTesterApplication extends Application
{
    public static void main(final String[] arguments)
    {
        new LogViewerTesterApplication().run(arguments);
    }

    private LogViewerTesterApplication()
    {
        super(LogsServerProject.get());
    }

    @Override
    protected void onRun()
    {
        int i = 0;
        while (true)
        {
            information("Testing $", i++);
            Duration.seconds(1).sleep();
        }
    }
}
