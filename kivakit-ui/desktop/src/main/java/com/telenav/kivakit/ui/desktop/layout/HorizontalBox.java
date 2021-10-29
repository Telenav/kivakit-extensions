package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.JComponent;

/**
 * @author jonathanl (shibo)
 */
public class HorizontalBox extends JComponent
{
    private final HorizontalBoxLayout layout;

    public HorizontalBox(Spacing style, int height)
    {
        setOpaque(false);

        layout = new HorizontalBoxLayout(this, style, height);
    }

    public HorizontalBox(int height)
    {
        this(Spacing.AUTOMATIC_SPACING, height);
    }

    public HorizontalBox add(JComponent component)
    {
        layout.add(component);
        return this;
    }

    public HorizontalBox space(int width)
    {
        layout.space(width);
        return this;
    }
}
