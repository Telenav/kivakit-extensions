package com.telenav.kivakit.ui.desktop.layout;

import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BoxLayout.X_AXIS;

/**
 * @author jonathanl (shibo)
 */
public class HorizontalBoxLayout
{
    private int added;

    private final JComponent parent;

    private final Spacing spacing;

    public HorizontalBoxLayout(final JComponent parent, final Spacing spacing)
    {
        this(parent, spacing, -1);
    }

    public HorizontalBoxLayout(final JComponent parent, final Spacing spacing, final int height)
    {
        if (height >= 0)
        {
            Size.heightOf(height).maximum(parent);
        }
        this.parent = parent;
        this.spacing = spacing;
        parent.setLayout(new BoxLayout(parent, X_AXIS));
    }

    public HorizontalBoxLayout add(final Component component)
    {
        add(component, null);
        return this;
    }

    public HorizontalBoxLayout add(final Component component, final Object constraints)
    {
        if (spacing == Spacing.AUTOMATIC_SPACING && added++ > 0)
        {
            space(8);
        }
        if (constraints == null)
        {
            parent.add(component);
        }
        else
        {
            parent.add(component, constraints);
        }
        return this;
    }

    public HorizontalBoxLayout addLeftAligned(final JComponent component)
    {
        add(Alignment.left(component));
        return this;
    }

    public HorizontalBoxLayout addStretched(final JComponent component)
    {
        add(Alignment.stretched(component));
        return this;
    }

    public HorizontalBoxLayout addTopAligned(final JComponent component)
    {
        add(Alignment.top(component));
        return this;
    }

    public HorizontalBoxLayout horizontalGlue()
    {
        add(Box.createHorizontalGlue());
        return this;
    }

    public HorizontalBoxLayout separator()
    {
        add(KivaKitTheme.get().newVerticalSeparator());
        return this;
    }

    public HorizontalBoxLayout space(final int width)
    {
        parent.add(Box.createRigidArea(new Dimension(width, 0)));
        return this;
    }

    public HorizontalBoxLayout verticalGlue()
    {
        add(Box.createVerticalGlue());
        return this;
    }
}
