package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class HorizontalBox extends JComponent
{
    private final HorizontalBoxLayout layout;

    public HorizontalBox(final Spacing style, final int height)
    {
        setOpaque(false);

        layout = new HorizontalBoxLayout(this, style, height);
    }

    public HorizontalBox(final int height)
    {
        this(Spacing.AUTOMATIC_SPACING, height);
    }

    public HorizontalBox add(final JComponent component)
    {
        layout.add(component);
        return this;
    }

    public HorizontalBox space(final int width)
    {
        layout.space(width);
        return this;
    }
}
