package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem.drawingSurface;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateHeight extends CoordinateDistance
{
    public static CoordinateHeight height(final CoordinateSystem system, final double units)
    {
        return new CoordinateHeight(system, units);
    }

    public static CoordinateHeight height(final double units)
    {
        return height(drawingSurface(), units);
    }

    public CoordinateHeight(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    public Coordinate asCoordinate()
    {
        return Coordinate.at(coordinateSystem(), 0, units());
    }

    @Override
    public CoordinateHeight scaledBy(final Percent percent)
    {
        return (CoordinateHeight) super.scaledBy(percent);
    }

    @Override
    public CoordinateHeight scaledBy(final double scaleFactor)
    {
        return (CoordinateHeight) super.scaledBy(scaleFactor);
    }

    @Override
    protected CoordinateDistance newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
