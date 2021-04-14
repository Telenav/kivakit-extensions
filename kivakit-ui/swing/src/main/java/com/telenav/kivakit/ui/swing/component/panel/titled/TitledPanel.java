package com.telenav.kivakit.ui.swing.component.panel.titled;

import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.layout.Spacing;
import com.telenav.kivakit.ui.swing.layout.VerticalBoxLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class TitledPanel extends KivaKitPanel
{
    private String title;

    private ImageIcon icon;

    public TitledPanel()
    {
        configureShadedPanel(this);
    }

    public void content(final JComponent child)
    {
        final var title = newComponentLabel(this.title);
        title.setOpaque(true);
        colorTitle().foreground(title);
        title.setFont(fontComponent().deriveFont(14.0f));
        colorTitleBackground().background(title);
        if (icon != null)
        {
            title.setIcon(icon);
        }

        final var header = newContainerPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(title);

        Margins.of(10).apply(child);

        new VerticalBoxLayout(this, Spacing.MANUAL_SPACING)
                .add(header.margins(5))
                .addStretched(newHorizontalSeparator())
                .addStretched(child);
    }

    public void icon(final ImageIcon icon)
    {
        this.icon = icon;
    }

    public void title(final String title)
    {
        this.title = title;
    }
}
