package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingDistance;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSize;

/**
 * An abstract coordinate
 *
 * @author jonathanl (shibo)
 */
public interface CoordinateSystem
{
    /**
     * @return The given x, y location in the {@link CoordinateSystem} of this drawing surface
     */
    default Coordinate at(final double x, final double y)
    {
        return Coordinate.at(this, x, y);
    }

    /**
     * @return The given distance in abstract units in the coordinate system of this drawing surface
     */
    default CoordinateDistance distance(final double units)
    {
        return CoordinateDistance.units(this, units);
    }

    /**
     * @return The given height in abstract units in the coordinate system of this drawing surface
     */
    default CoordinateHeight height(final double height)
    {
        return CoordinateHeight.height(this, height);
    }

    /**
     * @return The given height in abstract units in the coordinate system of this drawing surface
     */
    default CoordinateSize size(final double width, final double height)
    {
        return CoordinateSize.size(this, width, height);
    }

    default CoordinateSlope slope(final Coordinate a, final Coordinate b)
    {
        final var point = b.minus(a);
        final var opposite = point.y();
        final var adjacent = point.x();
        return CoordinateSlope.radians(Math.atan(opposite / adjacent));
    }

    default Coordinate toCoordinates(final double x, final double y)
    {
        return toCoordinates(DrawingPoint.at(x, y));
    }

    Coordinate toCoordinates(DrawingPoint point);

    CoordinateSize toCoordinates(DrawingSize size);

    CoordinateDistance toCoordinates(DrawingDistance distance);

    DrawingDistance toDrawingUnits(CoordinateDistance distance);

    DrawingDistance toDrawingUnits(CoordinateHeight height);

    DrawingDistance toDrawingUnits(CoordinateWidth width);

    DrawingPoint toDrawingUnits(final Coordinate coordinate);

    DrawingSize toDrawingUnits(final CoordinateSize coordinate);

    /**
     * @return The given width in abstract units in the coordinate system of this drawing surface
     */
    default CoordinateWidth width(final double width)
    {
        return CoordinateWidth.width(this, width);
    }
}
