package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Height;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Length;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Rectangle;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;

import java.awt.geom.Point2D;

/**
 * An abstract, bounded coordinate system with an {@link #origin()} and a {@link #size()}. Coordinates can be mapped to
 * another coordinate system with {@link #to(CoordinateSystem, Point)}.
 *
 * @author jonathanl (shibo)
 */
public interface CoordinateSystem
{
    /**
     * @return The given Java 2D point as a {@link Point}
     */
    default Point at(final Point2D point)
    {
        return at(point.getX(), point.getY());
    }

    /**
     * @return The given x, y location as a {@link Point} in this {@link CoordinateSystem}
     */
    default Point at(final double x, final double y)
    {
        return Point.at(this, x, y);
    }

    /**
     * @return The given height in the units of this coordinate system
     */
    default Height height(final double height)
    {
        return Height.height(this, height);
    }

    /**
     * @return True if this coordinate system is bounded
     */
    boolean isBounded();

    /**
     * @return The given distance in the units of this coordinate system
     */
    default Length length(final double units)
    {
        return Length.units(this, units);
    }

    /**
     * @return The origin of this coordinate system
     */
    Point origin();

    /**
     * @return The size of this coordinate system from the origin
     */
    Size size();

    /**
     * @return The given size in the units of this coordinate system
     */
    default Size size(final double width, final double height)
    {
        return Size.size(this, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default Size size(final Width width, final Height height)
    {
        return size(width.units(), height.units());
    }

    /**
     * @return The given {@link Length} converted from this coordinate system to the given coordinate system
     */
    default Length to(final CoordinateSystem that, final Length distance)
    {
        return to(that, distance.asWidth());
    }

    /**
     * @return The given {@link Height} converted from this coordinate system to the given coordinate system
     */
    default Height to(final CoordinateSystem that, final Height height)
    {
        return height(to(that, height.asCoordinate()).y());
    }

    /**
     * @return The given {@link Size} converted from this coordinate system to the given coordinate system
     */
    default Size to(final CoordinateSystem that, final Size size)
    {
        return size(to(that, size.width()), to(that, size.height()));
    }

    /**
     * @return The given {@link Rectangle} converted from this coordinate system to the given coordinate system
     */
    default Rectangle to(final CoordinateSystem that, final Rectangle rectangle)
    {
        final var a = to(that, rectangle.at());
        final var b = to(that, rectangle.to());
        return Rectangle.rectangle(a, b);
    }

    /**
     * @return The given {@link Width} converted from this coordinate system to the given coordinate system
     */
    default Width to(final CoordinateSystem that, final Width width)
    {
        return width(to(that, width.asCoordinate()).x());
    }

    /**
     * @return The given {@link Point} from this coordinate system to the given coordinate system
     */
    Point to(CoordinateSystem that, Point coordinate);

    /**
     * @return The given width in the units of this coordinate system
     */
    default Width width(final double width)
    {
        return Width.width(this, width);
    }
}
