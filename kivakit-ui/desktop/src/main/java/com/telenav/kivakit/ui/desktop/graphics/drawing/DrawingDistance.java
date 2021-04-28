package com.telenav.kivakit.ui.desktop.graphics.drawing;

/**
 * @author jonathanl (shibo)
 */
public class DrawingDistance
{
    public static DrawingDistance of(final double units)
    {
        return new DrawingDistance(units);
    }

    private final double units;

    public DrawingDistance(final double units)
    {
        this.units = units;
    }

    public boolean isNonZero()
    {
        return units > 0;
    }

    public DrawingDistance scaled(final double scaleFactor)
    {
        return of(units * scaleFactor);
    }

    @Override
    public String toString()
    {
        return units + " units";
    }

    public double units()
    {
        return units;
    }
}
