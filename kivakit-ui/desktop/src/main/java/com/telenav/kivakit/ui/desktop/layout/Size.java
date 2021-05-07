package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class Size
{
    private static final int INVALID = Integer.MIN_VALUE;

    public static Size heightOf(final int height)
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

    public static Size of(final int size)
    {
        return new Size(size, size);
    }

    public static Size widthOf(final int width)
    {
        return new Size(width, INVALID);
    }

    private final int width;

    private final int height;

    protected Size(final int width, final int height)
    {
        this.width = width;
        this.height = height;
    }

    public Size maximum(final JComponent component)
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

    public Size minimum(final JComponent component)
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

    public Size preferred(final JComponent component)
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

    private Size maximumHeight(final JComponent component)
    {
        final var maximumSize = component.getMaximumSize();
        maximumSize.height = height;
        component.setMaximumSize(maximumSize);
        return this;
    }

    private Size maximumWidth(final JComponent component)
    {
        final var maximumSize = component.getMaximumSize();
        maximumSize.width = width;
        component.setMaximumSize(maximumSize);
        return this;
    }

    private Size minimumHeight(final JComponent component)
    {
        final var minimumSize = component.getMinimumSize();
        minimumSize.height = height;
        component.setMinimumSize(minimumSize);
        return this;
    }

    private Size minimumWidth(final JComponent component)
    {
        final var minimumSize = component.getMinimumSize();
        minimumSize.width = width;
        component.setMinimumSize(minimumSize);
        return this;
    }

    private Size preferredHeight(final JComponent component)
    {
        final var preferredSize = component.getPreferredSize();
        preferredSize.height = height;
        component.setPreferredSize(preferredSize);
        return this;
    }

    private Size preferredWidth(final JComponent component)
    {
        final var preferredSize = component.getPreferredSize();
        preferredSize.width = width;
        component.setPreferredSize(preferredSize);
        return this;
    }
}
