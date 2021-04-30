package com.telenav.kivakit.ui.desktop.graphics.geometry;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem.drawingSurface;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateRectangle
{
    public static CoordinateRectangle rectangle(final Coordinate point, final CoordinateSize size)
    {
        return new CoordinateRectangle(point, size);
    }

    public static CoordinateRectangle rectangle(final Coordinate a, final Coordinate b)
    {

        return new CoordinateRectangle(a, a.size(b));
    }

    public static CoordinateRectangle rectangle(final double x,
                                                final double y,
                                                final double width,
                                                final double height)
    {
        return rectangle(drawingSurface(), x, y, width, height);
    }

    public static CoordinateRectangle rectangle(final CoordinateSystem system,
                                                final double x,
                                                final double y,
                                                final double width,
                                                final double height)
    {
        return new CoordinateRectangle(Coordinate.at(system, x, y), CoordinateSize.size(system, width, height));
    }

    private Coordinate at;

    private final CoordinateSize size;

    public CoordinateRectangle(final Coordinate at, final CoordinateSize size)
    {
        this.at = at;
        this.size = size;
    }

    public Coordinate at()
    {
        return at;
    }

    public CoordinateRectangle centeredIn(final CoordinateSize size)
    {
        final var copy = copy();
        at = Coordinate.at(size.coordinateSystem(),
                (size.widthInUnits() - this.size.widthInUnits()) / 2,
                (size.heightInUnits() - this.size.heightInUnits()) / 2);
        return copy;
    }

    public CoordinateRectangle copy()
    {
        return new CoordinateRectangle(at, size);
    }

    public double height()
    {
        return size.heightInUnits();
    }

    public CoordinateSize size()
    {
        return size;
    }

    public Coordinate to()
    {
        return at.plus(size);
    }

    @Override
    public String toString()
    {
        return at + ", " + size;
    }

    public Coordinate topLeft()
    {
        return at;
    }

    public double width()
    {
        return size.widthInUnits();
    }

    public double x()
    {
        return at.x();
    }

    public double y()
    {
        return at.y();
    }
}
