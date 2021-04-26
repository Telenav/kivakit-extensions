package com.telenav.kivakit.ui.swing.layout;

import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class VerticalBoxLayout
{
    private int added;

    private final JComponent parent;

    private final Spacing style;

    public VerticalBoxLayout(final JComponent parent, final Spacing style)
    {
        this.parent = parent;
        this.style = style;
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
    }

    public VerticalBoxLayout(final JComponent parent)
    {
        this(parent, Spacing.AUTOMATIC_SPACING);
    }

    public VerticalBoxLayout add(final Component component)
    {
        if (style == Spacing.AUTOMATIC_SPACING && added++ > 0)
        {
            space(10);
        }
        parent.add(component);
        return this;
    }

    public VerticalBoxLayout addLeftAligned(final JComponent component)
    {
        add(Alignment.left(component));
        return this;
    }

    public VerticalBoxLayout addStretched(final JComponent component)
    {
        add(Alignment.stretched(component));
        return this;
    }

    public VerticalBoxLayout addTopAligned(final JComponent component)
    {
        add(Alignment.top(component));
        return this;
    }

    public VerticalBoxLayout horizontalGlue()
    {
        add(Box.createHorizontalGlue());
        return this;
    }

    public VerticalBoxLayout rigidArea(final int height)
    {
        add(Box.createRigidArea(new Dimension(0, height)));
        return this;
    }

    public VerticalBoxLayout separator()
    {
        add(KivaKitTheme.get().newHorizontalSeparator());
        return this;
    }

    public VerticalBoxLayout space(final int height)
    {
        parent.add(Box.createRigidArea(new Dimension(0, height)));
        return this;
    }

    public VerticalBoxLayout strut(final int height)
    {
        add(Box.createVerticalStrut(height));
        return this;
    }
}
