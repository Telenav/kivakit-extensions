package com.telenav.kivakit.ui.swing.layout;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class VerticalBox extends JComponent
{
    private final VerticalBoxLayout layout;

    public VerticalBox(final int width)
    {
        layout = new VerticalBoxLayout(this);
        setOpaque(false);
        Size.widthOf(width).maximum(this);
    }

    public VerticalBox add(final JComponent component)
    {
        layout.add(component);
        return this;
    }

    public VerticalBox space(final int height)
    {
        layout.space(height);
        return this;
    }
}
