package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import java.awt.geom.Point2D;
import java.util.Objects;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem.drawingSurface;

/**
 * Represents a coordinate in
 *
 * @author jonathanl (shibo)
 */
public class Coordinate extends Coordinated
{
    public static Coordinate at(final CoordinateSystem system, final double x, final double y)
    {
        return new Coordinate(system, x, y);
    }

    public static Coordinate at(final double x, final double y)
    {
        return at(drawingSurface(), x, y);
    }

    public static Coordinate at(final Point2D point)
    {
        return at(point.getX(), point.getY());
    }

    private final double x;

    private final double y;

    public Coordinate(final CoordinateSystem system, final double x, final double y)
    {
        super(system);

        this.x = x;
        this.y = y;
    }

    public CoordinateSize asSize()
    {
        return CoordinateSize.size(coordinateSystem(), x, y);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Coordinate)
        {
            final Coordinate that = (Coordinate) object;
            return x == that.x && y == that.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    public Coordinate minus(final double dx, final double dy)
    {
        return at(x - dx, y - dy);
    }

    public Coordinate minus(final Coordinate that)
    {
        return at(x - that.x, y - that.y);
    }

    public Coordinate plus(final double dx, final double dy)
    {
        return at(x + dx, y + dy);
    }

    public Coordinate plus(final CoordinateSize size)
    {
        return at(x + size.widthInUnits(), y + size.heightInUnits());
    }

    public Coordinate plus(final Coordinate that)
    {
        return at(x + that.x, y + that.y);
    }

    public CoordinateRectangle rectangle(final CoordinateSize size)
    {
        return CoordinateRectangle.rectangle(this, size);
    }

    public Coordinate scaledBy(final Percent percent)
    {
        return at(coordinateSystem(), percent.scale(x), percent.scale(y));
    }

    public CoordinateSize size(final Coordinate that)
    {
        final var width = Math.abs(x() - that.x());
        final var height = Math.abs(y() - that.y());

        return CoordinateSize.size(that.coordinateSystem(), width, height);
    }

    public Coordinate times(final double scaleFactor)
    {
        return at(coordinateSystem(), x * scaleFactor, y * scaleFactor);
    }

    public Coordinate to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    @Override
    public String toString()
    {
        return x + ", " + y;
    }

    /**
     * @return The x location of this coordinate
     */
    public double x()
    {
        return x;
    }

    /**
     * @return The x location of this coordinate
     */
    public double y()
    {
        return y;
    }
}
