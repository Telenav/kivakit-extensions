package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem.drawingSurface;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateDistance extends Coordinated
{
    public static CoordinateDistance units(final double units)
    {
        return units(drawingSurface(), units);
    }

    public static CoordinateDistance units(final CoordinateSystem system, final double units)
    {
        return new CoordinateDistance(system, units);
    }

    private final double units;

    public CoordinateDistance(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem);
        this.units = units;
    }

    public CoordinateWidth asWidth()
    {
        return CoordinateWidth.width(coordinateSystem(), units);
    }

    public boolean isNonZero()
    {
        return units > 0;
    }

    public CoordinateDistance scaledBy(final Percent percent)
    {
        return newInstance(percent.scale(units));
    }

    public CoordinateDistance scaledBy(final double scaleFactor)
    {
        return newInstance(units * scaleFactor);
    }

    public CoordinateDistance to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    public double units()
    {
        return units;
    }

    protected CoordinateDistance newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
