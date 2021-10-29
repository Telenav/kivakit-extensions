package com.telenav.kivakit.ui.desktop.layout;

import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author jonathanl (shibo)
 */
public class VerticalBoxLayout
{
    private int added;

    private final JComponent parent;

    private final Spacing style;

    public VerticalBoxLayout(JComponent parent, Spacing style)
    {
        this.parent = parent;
        this.style = style;
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
    }

    public VerticalBoxLayout(JComponent parent)
    {
        this(parent, Spacing.AUTOMATIC_SPACING);
    }

    public VerticalBoxLayout add(Component component)
    {
        if (style == Spacing.AUTOMATIC_SPACING && added++ > 0)
        {
            space(10);
        }
        parent.add(component);
        return this;
    }

    public VerticalBoxLayout addLeftAligned(JComponent component)
    {
        add(Alignment.left(component));
        return this;
    }

    public VerticalBoxLayout addStretched(JComponent component)
    {
        add(Alignment.stretched(component));
        return this;
    }

    public VerticalBoxLayout addTopAligned(JComponent component)
    {
        add(Alignment.top(component));
        return this;
    }

    public VerticalBoxLayout horizontalGlue()
    {
        add(Box.createHorizontalGlue());
        return this;
    }

    public VerticalBoxLayout rigidArea(int height)
    {
        add(Box.createRigidArea(new Dimension(0, height)));
        return this;
    }

    public VerticalBoxLayout separator()
    {
        add(KivaKitTheme.get().newHorizontalSeparator());
        return this;
    }

    public VerticalBoxLayout space(int height)
    {
        parent.add(Box.createRigidArea(new Dimension(0, height)));
        return this;
    }

    public VerticalBoxLayout strut(int height)
    {
        add(Box.createVerticalStrut(height));
        return this;
    }
}
