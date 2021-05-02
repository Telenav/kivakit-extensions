package com.telenav.kivakit.ui.desktop.graphics.geometry;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateSlope
{
    public static CoordinateSlope degrees(final double degrees)
    {
        return new CoordinateSlope(Math.toRadians(degrees));
    }

    public static CoordinateSlope radians(final double radians)
    {
        return new CoordinateSlope(radians);
    }

    private final double radians;

    protected CoordinateSlope(final double radians)
    {
        this.radians = radians;
    }

    public double degrees()
    {
        return Math.toDegrees(radians);
    }

    public double radians()
    {
        return radians;
    }
}
