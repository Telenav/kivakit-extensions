////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.client;

import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.logs.server.ServerLogProject;

import javax.swing.SwingUtilities;
import java.awt.Image;
import java.util.Map;

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
    public void configure(final Map<String, String> properties)
    {
        final var maximum = properties.get("maximum-entries");
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
    public synchronized void onLog(final LogEntry entry)
    {
        if (frame != null)
        {
            frame.add(entry);
        }
    }

    public void show(final String title, final Image icon)
    {
        SwingUtilities.invokeLater(() -> frame = new ClientLogFrame(this, maximumEntries, title, icon, EXIT_ON_CLOSE));
    }
}
