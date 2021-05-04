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

import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;

import java.awt.geom.Point2D;

/**
 * An abstract, bounded coordinate system with an {@link #origin()} and a {@link #size()}. Coordinates can be mapped to
 * another coordinate system with {@link #to(CoordinateSystem, DrawingPoint)}.
 *
 * @author jonathanl (shibo)
 */
public interface CoordinateSystem
{
    /**
     * @return The given Java 2D point as a {@link DrawingPoint}
     */
    default DrawingPoint at(final Point2D point)
    {
        return at(point.getX(), point.getY());
    }

    /**
     * @return The given x, y location as a {@link DrawingPoint} in this {@link CoordinateSystem}
     */
    default DrawingPoint at(final double x, final double y)
    {
        return DrawingPoint.at(this, x, y);
    }

    /**
     * @return The given height in the units of this coordinate system
     */
    default DrawingHeight height(final double height)
    {
        return DrawingHeight.height(this, height);
    }

    /**
     * @return True if this coordinate system is bounded
     */
    boolean isBounded();

    /**
     * @return The given distance in the units of this coordinate system
     */
    default DrawingLength length(final double units)
    {
        return DrawingLength.units(this, units);
    }

    /**
     * @return The origin of this coordinate system
     */
    DrawingPoint origin();

    /**
     * @return The size of this coordinate system from the origin
     */
    DrawingSize size();

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
    default DrawingLength to(final CoordinateSystem that, final DrawingLength distance)
    {
        return to(that, distance.asWidth());
    }

    /**
     * @return The given {@link DrawingHeight} converted from this coordinate system to the given coordinate system
     */
    default DrawingHeight to(final CoordinateSystem that, final DrawingHeight height)
    {
        return height(to(that, height.asCoordinate()).y());
    }

    /**
     * @return The given {@link DrawingSize} converted from this coordinate system to the given coordinate system
     */
    default DrawingSize to(final CoordinateSystem that, final DrawingSize size)
    {
        return size(to(that, size.width()), to(that, size.height()));
    }

    /**
     * @return The given {@link DrawingRectangle} converted from this coordinate system to the given coordinate system
     */
    default DrawingRectangle to(final CoordinateSystem that, final DrawingRectangle rectangle)
    {
        final var a = to(that, rectangle.at());
        final var b = to(that, rectangle.to());
        return DrawingRectangle.rectangle(a, b);
    }

    /**
     * @return The given {@link DrawingWidth} converted from this coordinate system to the given coordinate system
     */
    default DrawingWidth to(final CoordinateSystem that, final DrawingWidth width)
    {
        return width(to(that, width.asCoordinate()).x());
    }

    /**
     * @return The given {@link DrawingPoint} from this coordinate system to the given coordinate system
     */
    DrawingPoint to(CoordinateSystem that, DrawingPoint coordinate);

    /**
     * @return The given width in the units of this coordinate system
     */
    default DrawingWidth width(final double width)
    {
        return DrawingWidth.width(this, width);
    }
}
