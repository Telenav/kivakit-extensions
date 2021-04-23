package com.telenav.kivakit.ui.swing.component.panel.section;

import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.component.layout.separator.HorizontalSeparator;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.layout.Spacing;
import com.telenav.kivakit.ui.swing.layout.VerticalBoxLayout;

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

    public void content(final JComponent child)
    {
        final var title = newComponentLabel(this.title);
        title.setOpaque(false);
        theme().styleTitle().apply(title);
        if (icon != null)
        {
            title.setIcon(icon);
        }

        Margins.of(5).apply(title);
        Margins.of(10).apply(child);

        final var header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(title);

        setOpaque(false);
        new VerticalBoxLayout(this, Spacing.MANUAL_SPACING)
                .add(header)
                .addStretched(new HorizontalSeparator(theme().colorSeparator()))
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
