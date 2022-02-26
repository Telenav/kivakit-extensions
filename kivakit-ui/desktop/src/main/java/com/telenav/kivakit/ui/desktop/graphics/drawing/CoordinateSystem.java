/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // https://www.apache.org/licenses/LICENSE-2.0
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

import com.telenav.kivakit.interfaces.naming.Named;
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
    default DrawingHeight height(double height)
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
    default DrawingLength length(double units)
    {
        return DrawingLength.length(this, units);
    }

    DrawingPoint origin();

    /**
     * @return The given Java 2D point as a {@link DrawingPoint}
     */
    default DrawingPoint point(Point2D point)
    {
        return point(point.getX(), point.getY());
    }

    /**
     * @return The given x, y location as a {@link DrawingPoint} in this {@link CoordinateSystem}
     */
    default DrawingPoint point(double x, double y)
    {
        return DrawingPoint.point(this, x, y);
    }

    /**
     * @return The given x, y, width and height as a {@link DrawingRectangle} in this {@link CoordinateSystem}
     */
    default DrawingRectangle rectangle(double x, double y, double width, double height)
    {
        return DrawingRectangle.rectangle(this, x, y, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default DrawingSize size(double width, double height)
    {
        return DrawingSize.size(this, width, height);
    }

    /**
     * @return The given size in the units of this coordinate system
     */
    default DrawingSize size(DrawingWidth width, DrawingHeight height)
    {
        return size(width.units(), height.units());
    }

    /**
     * @return The given {@link DrawingLength} converted from this coordinate system to the given coordinate system
     */
    default DrawingLength toCoordinates(Coordinated that, DrawingLength distance)
    {
        return toCoordinates(that, distance.asWidth());
    }

    /**
     * @return The given {@link DrawingHeight} converted from this coordinate system to the given coordinate system
     */
    DrawingHeight toCoordinates(Coordinated that, DrawingHeight height);

    /**
     * @return The given {@link DrawingSize} converted from this coordinate system to the given coordinate system
     */
    default DrawingSize toCoordinates(Coordinated that, DrawingSize size)
    {
        return size(toCoordinates(that, size.width()), toCoordinates(that, size.height()));
    }

    /**
     * @return The given {@link DrawingRectangle} converted from this coordinate system to the given coordinate system
     */
    default DrawingRectangle toCoordinates(Coordinated that, DrawingRectangle rectangle)
    {
        var a = toCoordinates(that, rectangle.topLeft());
        var b = toCoordinates(that, rectangle.bottomRight());
        return DrawingRectangle.rectangle(a, b);
    }

    /**
     * @return The given {@link DrawingWidth} converted from this coordinate system to the given coordinate system
     */
    DrawingWidth toCoordinates(Coordinated coordinated, DrawingWidth width);

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
    default DrawingWidth width(double width)
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
