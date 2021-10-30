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

package com.telenav.kivakit.logs.client;

import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.logs.server.ServerLogProject;

import javax.swing.SwingUtilities;
import java.awt.Image;

import static com.telenav.kivakit.logs.client.ClientLogFrame.ExitMode.EXIT_ON_CLOSE;

public class ClientLog extends BaseLog
{
    private Maximum maximumEntries;

    private final CompletionLatch session = new CompletionLatch();

    private ClientLogFrame frame;

    public ClientLog()
    {
        ServerLogProject.get().initialize();
    }

    @Override
    public void clear()
    {
        if (frame != null)
        {
            frame.clear();
        }
    }

    @Override
    public void configure(VariableMap<String> properties)
    {
        var maximum = properties.get("maximum-entries");
        if (maximum != null)
        {
            maximumEntries = Maximum.parse(maximum);
        }
    }

    public void exit()
    {
        session.completed();
    }

    public Maximum maximumEntries()
    {
        return maximumEntries;
    }

    @Override
    public String name()
    {
        return "Viewer";
    }

    @Override
    public synchronized void onLog(LogEntry entry)
    {
        if (frame != null)
        {
            frame.add(entry);
        }
    }

    public void show(String title, Image icon)
    {
        SwingUtilities.invokeLater(() -> frame = new ClientLogFrame(this, maximumEntries, title, icon, EXIT_ON_CLOSE));
    }
}
