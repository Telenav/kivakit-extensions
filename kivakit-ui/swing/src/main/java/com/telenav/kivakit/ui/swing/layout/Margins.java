package com.telenav.kivakit.ui.swing.layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * @author jonathanl (shibo)
 */
public class Margins
{
    public static Margins bottomOf(final int size)
    {
        return new Margins(size, 0, 0, 0);
    }

    public static Margins leftAndRightOf(final int size)
    {
        return new Margins(0, size, 0, size);
    }

    public static Margins leftOf(final int size)
    {
        return new Margins(0, size, 0, 0);
    }

    public static Margins of(final int size)
    {
        return new Margins(size, size, size, size);
    }

    public static Margins rightOf(final int size)
    {
        return new Margins(0, 0, 0, size);
    }

    public static Margins topAndBottomOf(final int size)
    {
        return new Margins(size, 0, size, 0);
    }

    public static Margins topOf(final int size)
    {
        return new Margins(0, 0, size, 0);
    }

    private int bottom;

    private int left;

    private int top;

    private int right;

    private Margins(final int bottom, final int left, final int top, final int right)
    {
        this.bottom = bottom;
        this.left = left;
        this.top = top;
        this.right = right;
    }

    public Margins apply(final JComponent component)
    {
        component.setBorder(border());
        return this;
    }

    public Border border()
    {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    public Margins bottom(final int bottom)
    {
        this.bottom = bottom;
        return this;
    }

    public Border inside(final Border outside)
    {
        return new CompoundBorder(outside, border());
    }

    public Margins insideExisting(final JComponent component)
    {
        component.setBorder(inside(component.getBorder()));
        return this;
    }

    public Margins left(final int left)
    {
        this.left = left;
        return this;
    }

    public Border outside(final Border inside)
    {
        return new CompoundBorder(border(), inside);
    }

    public Margins outsideExisting(final JComponent component)
    {
        component.setBorder(outside(component.getBorder()));
        return this;
    }

    public Margins right(final int right)
    {
        this.right = right;
        return this;
    }

    public Margins size(final int size)
    {
        bottom = size;
        left = size;
        top = size;
        right = size;
        return this;
    }

    public Margins top(final int top)
    {
        this.top = top;
        return this;
    }
}
