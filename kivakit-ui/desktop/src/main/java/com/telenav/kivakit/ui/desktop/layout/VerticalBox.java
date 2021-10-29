package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.JComponent;

/**
 * @author jonathanl (shibo)
 */
public class VerticalBox extends JComponent
{
    private final VerticalBoxLayout layout;

    public VerticalBox(int width)
    {
        layout = new VerticalBoxLayout(this);
        setOpaque(false);
        Size.widthOf(width).maximum(this);
    }

    public VerticalBox add(JComponent component)
    {
        layout.add(component);
        return this;
    }

    public VerticalBox space(int height)
    {
        layout.space(height);
        return this;
    }
}
