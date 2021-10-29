package com.telenav.kivakit.ui.desktop.component.panel.titled;

import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.layout.Spacing;
import com.telenav.kivakit.ui.desktop.layout.VerticalBoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.awt.FlowLayout;

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

    public void content(JComponent child)
    {
        var title = newComponentLabel(this.title);
        title.setOpaque(true);
        theme().styleTitle().apply(title);
        if (icon != null)
        {
            title.setIcon(icon);
        }

        var header = newContainerPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(title);

        Margins.of(10).apply(child);

        new VerticalBoxLayout(this, Spacing.MANUAL_SPACING)
                .add(header.margins(5))
                .addStretched(newHorizontalSeparator())
                .addStretched(child);
    }

    public void icon(ImageIcon icon)
    {
        this.icon = icon;
    }

    public void title(String title)
    {
        this.title = title;
    }
}
