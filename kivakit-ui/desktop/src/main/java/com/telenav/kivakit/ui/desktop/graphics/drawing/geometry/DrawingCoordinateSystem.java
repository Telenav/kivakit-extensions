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

package com.telenav.kivakit.ui.desktop.graphics.drawing.geometry;

import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;

import java.util.Objects;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Maps to and from a given bounded coordinate system using simple rectangular interpolation.
 *
 * @author jonathanl (shibo)
 */
public class DrawingCoordinateSystem implements CoordinateSystem
{
    /**
     * @return A {@link DrawingCoordinateSystem} with the given origin and size
     */
    public static DrawingCoordinateSystem createCoordinateSystem(final double x, final double y, final DrawingSize size)
    {
        return new DrawingCoordinateSystem(x, y, size);
    }

    /**
     * @return A coordinate system with the given origin, but with no size and therefore no scaling
     */
    public static DrawingCoordinateSystem createCoordinateSystem(final double x, final double y)
    {
        return new DrawingCoordinateSystem(x, y);
    }

    /**
     * @return A {@link DrawingCoordinateSystem} with an origin of 0, 0 and no bounds
     */
    public static DrawingCoordinateSystem createCoordinateSystem()
    {
        return createCoordinateSystem(0, 0);
    }

    /** The origin of this coordinate system */
    private final DrawingPoint origin;

    /** The size of the coordinate system, or null if it is unbounded */
    private DrawingSize size;

    protected DrawingCoordinateSystem(final double x, final double y, final DrawingSize size)
    {
        this(x, y);
        this.size = size;
    }

    protected DrawingCoordinateSystem(final double x, final double y)
    {
        origin = DrawingPoint.at(this, x, y);
    }

    protected DrawingCoordinateSystem(final DrawingPoint origin)
    {
        this.origin = origin;
    }

    protected DrawingCoordinateSystem(final DrawingPoint origin, final DrawingSize size)
    {
        this.origin = origin;
        this.size = size;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof DrawingCoordinateSystem)
        {
            final DrawingCoordinateSystem that = (DrawingCoordinateSystem) object;
            return origin.equals(that.origin) && Objects.equals(size, that.size);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(origin, size);
    }

    /**
     * @return True if this coordinate system is bounded
     */
    @Override
    public boolean isBounded()
    {
        return size() != null;
    }

    @Override
    public DrawingPoint origin()
    {
        return origin;
    }

    @Override
    public DrawingSize size()
    {
        return size;
    }

    @Override
    public DrawingPoint to(final CoordinateSystem that, final DrawingPoint coordinate)
    {
        ensureNotNull(that);

        // If we are already in the same coordinate system,
        if (this == that)
        {
            // the coordinate does not need to be converted.
            return coordinate;
        }
        else
        {
            // If this coordinate system has a finite size,
            if (size != null)
            {
                // normalize the given coordinate to the unit interval from 0 to 1 in the this coordinate system,
                final var xUnit = (coordinate.x() - origin.x()) / size.widthInUnits();
                final var yUnit = (coordinate.y() - origin.y()) / size.heightInUnits();

                // then return the x, y coordinate in the given coordinate system scaled to the same relative position.
                final var x = that.origin().x() + xUnit * that.size().widthInUnits();
                final var y = that.origin().y() + yUnit * that.size().heightInUnits();

                return at(x, y);
            }
            else
            {
                // otherwise, get the offset of the given coordinate in this coordinate system,
                final var dx = coordinate.x() - origin.x();
                final var dy = coordinate.y() - origin.y();

                // and return the origin of the target coordinate system plus the offset.
                final var x = that.origin().x() + dx;
                final var y = that.origin().y() + dy;

                return at(x, y);
            }
        }
    }

    public DrawingCoordinateSystem withOrigin(final double x, final double y)
    {
        return new DrawingCoordinateSystem(DrawingPoint.at(this, x, y), size);
    }

    public DrawingCoordinateSystem withSize(final double dx, final double dy)
    {
        return new DrawingCoordinateSystem(origin, DrawingSize.size(this, dx, dy));
    }
}
