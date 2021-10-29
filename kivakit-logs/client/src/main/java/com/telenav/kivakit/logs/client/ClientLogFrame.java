////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
