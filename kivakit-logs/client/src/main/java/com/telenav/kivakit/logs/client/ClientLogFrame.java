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

import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.logs.client.project.LogsClientTheme;
import com.telenav.kivakit.logs.client.view.ClientLogPanel;

import javax.swing.JFrame;
import java.awt.Image;

public class ClientLogFrame extends JFrame
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public enum ExitMode
    {
        EXIT_ON_CLOSE,
        CONTINUE_ON_CLOSE
    }

    private final ClientLogPanel panel;

    public ClientLogFrame(ClientLog log, Maximum maximumRows, String title, Image icon,
                          ExitMode mode)
    {
        super(title);
        LOGGER.listenTo(panel = new ClientLogPanel(this, log, maximumRows));
        LogsClientTheme.configure(this, panel, icon);
        if (mode == ExitMode.EXIT_ON_CLOSE)
        {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public void add(LogEntry entry)
    {
        panel.tablePanel().add(entry);
    }

    public void clear()
    {
        panel.clear();
    }

    public void title(String title)
    {
        setTitle(title);
    }
}
