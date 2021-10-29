package com.telenav.kivakit.logs.client.view.panels.tool;

import com.telenav.kivakit.logs.client.view.ClientLogPanel;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;

import static com.telenav.kivakit.ui.desktop.layout.Spacing.MANUAL_SPACING;

/**
 * @author jonathanl (shibo)
 */
public class ToolPanel extends KivaKitPanel
{
    public ToolPanel(ClientLogPanel parent)
    {
        setOpaque(false);

        new HorizontalBoxLayout(this, MANUAL_SPACING, 24)
                .add(parent.connectionPanel())
                .separator()
                .add(parent.sessionPanel())
                .separator()
                .add(parent.searchPanel());
    }
}
