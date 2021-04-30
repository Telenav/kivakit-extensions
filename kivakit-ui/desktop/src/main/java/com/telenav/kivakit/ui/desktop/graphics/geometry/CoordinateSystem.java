package com.telenav.kivakit.ui.desktop.graphics.geometry;

/**
 * An abstract, bounded coordinate system with an {@link #origin()} and a {@link #size()}. Coordinates can be mapped to
 * another coordinate system with {@link #to(CoordinateSystem, Coordinate)}. The mapping may be Cartesian (rectilinear)
 * or it might be some other mapping such as a map surface projection.
 *
 * @author jonathanl (shibo)
 */
public interface CoordinateSystem
{
    static CartesianCoordinateSystem drawingSurface()
    {
        return new CartesianCoordinateSystem()
                .withOrigin(0, 0)
                .withSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @return The given x, y location in this {@link CoordinateSystem}
     */
    default Coordinate at(final double x, final double y)
    {
        return Coordinate.at(this, x, y);
    }

    /**
     * @return The given distance in the units of this coordinate system
     */
    default CoordinateDistance distance(final double units)
    {
        return CoordinateDistance.units(this, units);
    }

    /**
     * @return The given height in the units of this coordinate system
     */
    default CoordinateHeight height(final double height)
    {
        return CoordinateHeight.height(this, height);
    }

    /**
     * @return The origin of this coordinate system
     */
    Coordinate origin();

    /**
     * @return The size of this coordinate system from the origin
     */
    CoordinateSize size();

    /**
     * @return The given size in the units of this coordinate system
     */
    default CoordinateSize size(final double width, final double height)
    {
        return CoordinateSize.size(this, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default CoordinateSize size(final CoordinateWidth width, final CoordinateHeight height)
    {
        return size(width.units(), height.units());
    }

    default CoordinateDistance to(final CoordinateSystem that, final CoordinateDistance distance)
    {
        return to(that, distance.asWidth());
    }

    default CoordinateDistance to(final CoordinateSystem that, final CoordinateHeight height)
    {
        return distance(to(that, height.asCoordinate()).y());
    }

    default CoordinateSize to(final CoordinateSystem that, final CoordinateSize size)
    {
        return size(size.widthInUnits(), size.heightInUnits());
    }

    default CoordinateDistance to(final CoordinateSystem that, final CoordinateWidth width)
    {
        return distance(to(that, width.asCoordinate()).x());
    }

    /**
     * Converts the given coordinate from this coordinate system to the given coordinate system
     *
     * @param that The system to convert to
     * @param coordinate The coordinate to convert
     * @return The given coordinate in the given coordinate system
     */
    Coordinate to(CoordinateSystem that, Coordinate coordinate);

    /**
     * @return The given width in the units of this coordinate system
     */
    default CoordinateWidth width(final double width)
    {
        return CoordinateWidth.width(this, width);
    }
}
