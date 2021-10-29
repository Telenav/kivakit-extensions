package com.telenav.kivakit.ui.desktop.component.panel.section;

import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.component.layout.separator.HorizontalSeparator;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.layout.Spacing;
import com.telenav.kivakit.ui.desktop.layout.VerticalBoxLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * @author jonathanl (shibo)
 */
public class Section extends KivaKitPanel
{
    private String title;

    private ImageIcon icon;

    public Section()
    {
    }

    public void content(JComponent child)
    {
        var title = newComponentLabel(this.title);
        title.setOpaque(false);
        theme().styleTitle().apply(title);
        if (icon != null)
        {
            title.setIcon(icon);
        }

        Margins.of(5).apply(title);
        Margins.of(10).apply(child);

        var header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(title);

        setOpaque(false);
        new VerticalBoxLayout(this, Spacing.MANUAL_SPACING)
                .add(header)
                .addStretched(new HorizontalSeparator(theme().colorSeparator()))
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
