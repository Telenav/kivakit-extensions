package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.JComponent;

/**
 * @author jonathanl (shibo)
 */
public class Size
{
    private static final int INVALID = Integer.MIN_VALUE;

    public static Size heightOf(int height)
    {
        return new Size(INVALID, height);
    }

    public static Size maximumHeight()
    {
        return heightOf(Integer.MAX_VALUE);
    }

    public static Size maximumWidth()
    {
        return widthOf(Integer.MAX_VALUE);
    }

    public static Size of(int size)
    {
        return new Size(size, size);
    }

    public static Size widthOf(int width)
    {
        return new Size(width, INVALID);
    }

    private final int width;

    private final int height;

    protected Size(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public Size maximum(JComponent component)
    {
        if (width != INVALID)
        {
            maximumWidth(component);
        }
        if (height != INVALID)
        {
            maximumHeight(component);
        }
        return this;
    }

    public Size minimum(JComponent component)
    {
        if (width != INVALID)
        {
            minimumWidth(component);
        }
        if (height != INVALID)
        {
            minimumHeight(component);
        }
        return this;
    }

    public Size preferred(JComponent component)
    {
        if (width != INVALID)
        {
            preferredWidth(component);
        }
        if (height != INVALID)
        {
            preferredHeight(component);
        }
        return this;
    }

    private Size maximumHeight(JComponent component)
    {
        var maximumSize = component.getMaximumSize();
        maximumSize.height = height;
        component.setMaximumSize(maximumSize);
        return this;
    }

    private Size maximumWidth(JComponent component)
    {
        var maximumSize = component.getMaximumSize();
        maximumSize.width = width;
        component.setMaximumSize(maximumSize);
        return this;
    }

    private Size minimumHeight(JComponent component)
    {
        var minimumSize = component.getMinimumSize();
        minimumSize.height = height;
        component.setMinimumSize(minimumSize);
        return this;
    }

    private Size minimumWidth(JComponent component)
    {
        var minimumSize = component.getMinimumSize();
        minimumSize.width = width;
        component.setMinimumSize(minimumSize);
        return this;
    }

    private Size preferredHeight(JComponent component)
    {
        var preferredSize = component.getPreferredSize();
        preferredSize.height = height;
        component.setPreferredSize(preferredSize);
        return this;
    }

    private Size preferredWidth(JComponent component)
    {
        var preferredSize = component.getPreferredSize();
        preferredSize.width = width;
        component.setPreferredSize(preferredSize);
        return this;
    }
}
