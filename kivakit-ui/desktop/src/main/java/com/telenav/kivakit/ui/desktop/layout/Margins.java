package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * @author jonathanl (shibo)
 */
public class Margins
{
    public static Margins bottomOf(int size)
    {
        return new Margins(size, 0, 0, 0);
    }

    public static Margins leftAndRightOf(int size)
    {
        return new Margins(0, size, 0, size);
    }

    public static Margins leftOf(int size)
    {
        return new Margins(0, size, 0, 0);
    }

    public static Margins of(int size)
    {
        return new Margins(size, size, size, size);
    }

    public static Margins rightOf(int size)
    {
        return new Margins(0, 0, 0, size);
    }

    public static Margins topAndBottomOf(int size)
    {
        return new Margins(size, 0, size, 0);
    }

    public static Margins topOf(int size)
    {
        return new Margins(0, 0, size, 0);
    }

    private int bottom;

    private int left;

    private int top;

    private int right;

    private Margins(int bottom, int left, int top, int right)
    {
        this.bottom = bottom;
        this.left = left;
        this.top = top;
        this.right = right;
    }

    public Margins apply(JComponent component)
    {
        component.setBorder(border());
        return this;
    }

    public Border border()
    {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    public Margins bottom(int bottom)
    {
        this.bottom = bottom;
        return this;
    }

    public Border inside(Border outside)
    {
        return new CompoundBorder(outside, border());
    }

    public Margins insideExisting(JComponent component)
    {
        component.setBorder(inside(component.getBorder()));
        return this;
    }

    public Margins left(int left)
    {
        this.left = left;
        return this;
    }

    public Border outside(Border inside)
    {
        return new CompoundBorder(border(), inside);
    }

    public Margins outsideExisting(JComponent component)
    {
        component.setBorder(outside(component.getBorder()));
        return this;
    }

    public Margins right(int right)
    {
        this.right = right;
        return this;
    }

    public Margins size(int size)
    {
        bottom = size;
        left = size;
        top = size;
        right = size;
        return this;
    }

    public Margins top(int top)
    {
        this.top = top;
        return this;
    }
}
