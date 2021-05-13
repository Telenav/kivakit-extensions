/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */

package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;

import java.awt.geom.Point2D;

import static java.lang.Double.MAX_VALUE;

/**
 * An abstract, bounded coordinate system with an {@link #x()}, {@link #y()} {@link #origin()} and a {@link #width()}
 * and {@link #height()} {@link #extent()}, all in coordinate system defined units. Coordinates can be mapped to another
 * coordinate system with {@link #toCoordinates(Coordinated, DrawingPoint)} and other similar convenience methods.
 *
 * @author jonathanl (shibo)
 */
public interface CoordinateSystem extends Named, Coordinated
{
    /**
     * @return This coordinate system
     */
    @Override
    default CoordinateSystem coordinates()
    {
        return this;
    }

    /**
     * @return The size of this coordinate system
     */
    default DrawingSize extent()
    {
        return size(width(), height());
    }

    /**
     * @return The given height in the units of this coordinate system
     */
    default DrawingHeight height(final double height)
    {
        return DrawingHeight.height(this, height);
    }

    /**
     * @return The width of this coordinate system in units
     */
    double height();

    /**
     * @return True if this coordinate system is bounded
     */
    default boolean isBounded()
    {
        return width() != MAX_VALUE || height() != MAX_VALUE;
    }

    /**
     * @return The given distance in the units of this coordinate system
     */
    default DrawingLength length(final double units)
    {
        return DrawingLength.length(this, units);
    }

    DrawingPoint origin();

    /**
     * @return The given Java 2D point as a {@link DrawingPoint}
     */
    default DrawingPoint point(final Point2D point)
    {
        return point(point.getX(), point.getY());
    }

    /**
     * @return The given x, y location as a {@link DrawingPoint} in this {@link CoordinateSystem}
     */
    default DrawingPoint point(final double x, final double y)
    {
        return DrawingPoint.point(this, x, y);
    }

    /**
     * @return The given x, y, width and height as a {@link DrawingRectangle} in this {@link CoordinateSystem}
     */
    default DrawingRectangle rectangle(final double x, final double y, final double width, final double height)
    {
        return DrawingRectangle.rectangle(this, x, y, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default DrawingSize size(final double width, final double height)
    {
        return DrawingSize.size(this, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default DrawingSize size(final DrawingWidth width, final DrawingHeight height)
    {
        return size(width.units(), height.units());
    }

    /**
     * @return The given {@link DrawingLength} converted from this coordinate system to the given coordinate system
     */
    default DrawingLength toCoordinates(final Coordinated that, final DrawingLength distance)
    {
        return toCoordinates(that, distance.asWidth());
    }

    /**
     * @return The given {@link DrawingHeight} converted from this coordinate system to the given coordinate system
     */
    DrawingHeight toCoordinates(final Coordinated that, final DrawingHeight height);

    /**
     * @return The given {@link DrawingSize} converted from this coordinate system to the given coordinate system
     */
    default DrawingSize toCoordinates(final Coordinated that, final DrawingSize size)
    {
        return size(toCoordinates(that, size.width()), toCoordinates(that, size.height()));
    }

    /**
     * @return The given {@link DrawingRectangle} converted from this coordinate system to the given coordinate system
     */
    default DrawingRectangle toCoordinates(final Coordinated that, final DrawingRectangle rectangle)
    {
        final var a = toCoordinates(that, rectangle.topLeft());
        final var b = toCoordinates(that, rectangle.bottomRight());
        return DrawingRectangle.rectangle(a, b);
    }

    /**
     * @return The given {@link DrawingWidth} converted from this coordinate system to the given coordinate system
     */
    DrawingWidth toCoordinates(Coordinated coordinated, final DrawingWidth width);

    /**
     * @return The given {@link DrawingPoint} from this coordinate system to the given coordinate system
     */
    DrawingPoint toCoordinates(Coordinated coordinated, DrawingPoint coordinate);

    /**
     * @return The width of this coordinate system in units
     */
    double width();

    /**
     * @return The given width in the units of this coordinate system
     */
    default DrawingWidth width(final double width)
    {
        return DrawingWidth.width(this, width);
    }

    /**
     * @return The x coordinate of the origin in units
     */
    double x();

    /**
     * @return The y coordinate of the origin in units
     */
    double y();
}
