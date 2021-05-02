package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateWidth extends CoordinateDistance
{
    public static CoordinateWidth width(final CoordinateSystem system, final double units)
    {
        return new CoordinateWidth(system, units);
    }

    protected CoordinateWidth(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    public Coordinate asCoordinate()
    {
        return Coordinate.at(coordinateSystem(), units(), 0);
    }

    @Override
    public CoordinateWidth scaledBy(final Percent percent)
    {
        return (CoordinateWidth) super.scaledBy(percent);
    }

    @Override
    public CoordinateWidth scaledBy(final double scaleFactor)
    {
        return (CoordinateWidth) super.scaledBy(scaleFactor);
    }

    @Override
    protected CoordinateDistance newInstance(final double units)
    {
        return width(coordinateSystem(), units);
    }
}
