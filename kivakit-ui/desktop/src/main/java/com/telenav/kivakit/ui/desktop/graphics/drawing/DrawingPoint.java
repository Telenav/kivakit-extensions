package com.telenav.kivakit.ui.desktop.graphics.drawing;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * @author jonathanl (shibo)
 */
public class DrawingPoint
{
    public static DrawingPoint at(final double x, final double y)
    {
        return new DrawingPoint(x, y);
    }

    public static DrawingPoint at(final Point2D point)
    {
        return at(point.getX(), point.getY());
    }

    private final double x;

    private final double y;

    public DrawingPoint(final double x, final double y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof DrawingPoint)
        {
            final DrawingPoint that = (DrawingPoint) object;
            return x == that.x && y == that.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    public DrawingPoint plus(final double dx, final double dy)
    {
        return at(x + dx, y + dy);
    }

    public DrawingPoint plus(final DrawingSize size)
    {
        return at(x + size.width(), y + size.height());
    }

    @Override
    public String toString()
    {
        return x + ", " + y;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }
}
