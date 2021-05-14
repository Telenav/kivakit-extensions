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
import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;

import java.util.Objects;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint.pixels;
import static java.lang.Double.MAX_VALUE;

/**
 * Maps to and from a given bounded coordinate system using simple rectangular interpolation.
 *
 * @author jonathanl (shibo)
 * @see CoordinateSystem
 */
public class DrawingCoordinateSystem implements CoordinateSystem
{
    /**
     * A {@link DrawingCoordinateSystem} with an origin of 0, 0 and no bounds
     */
    public static final DrawingCoordinateSystem PIXELS = drawingCoordinateSystem("pixels")
            .withOrigin(0, 0)
            .unbounded();

    /**
     * @return A new coordinate system with the given name, but no origin or size
     */
    public static DrawingCoordinateSystem drawingCoordinateSystem(final String name)
    {
        return new DrawingCoordinateSystem(name);
    }

    /** The origin of this coordinate system */
    private double x, y;

    /** The size of the coordinate system, or null if it is unbounded */
    private double dx = -1, dy = -1;

    /** The name of this coordinate system */
    private String name;

    protected DrawingCoordinateSystem(final String name)
    {
        this.name = name;
    }

    protected DrawingCoordinateSystem(final DrawingCoordinateSystem that)
    {
        name = that.name;

        x = that.x;
        y = that.y;
        dx = that.dx;
        dy = that.dy;
    }

    public DrawingCoordinateSystem copy()
    {
        return new DrawingCoordinateSystem(this);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof DrawingCoordinateSystem)
        {
            final DrawingCoordinateSystem that = (DrawingCoordinateSystem) object;
            return x == that.x &&
                    y == that.y &&
                    dx == that.dx &&
                    dy == that.dy;
        }
        return false;
    }

    public void extent(final double dx, final double dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, dx, dy);
    }

    @Override
    public double height()
    {
        return dy;
    }

    /**
     * @return True if this coordinate system is bounded
     */
    @Override
    public boolean isBounded()
    {
        return dx != MAX_VALUE || dy != MAX_VALUE;
    }

    @Override
    public String name()
    {
        if (name.equals("pixels"))
        {
            return name;
        }
        return name + " (" + x + ", " + y + " : " + (isBounded() ? dx + " x " + dy : "unbounded") + ")";
    }

    @Override
    public DrawingPoint origin()
    {
        return pixels(x, y);
    }

    public DrawingCoordinateSystem origin(final double x, final double y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public DrawingWidth toCoordinates(final Coordinated coordinated, final DrawingWidth width)
    {
        ensureNotNull(coordinated);

        final var that = coordinated.coordinates();

        // If we are already in the same coordinate system,
        if (equals(that))
        {
            // the coordinate does not need to be mapped.
            return width;
        }
        else
        {
            // If this coordinate systems are both bounded,
            if (isBounded() && that.isBounded())
            {
                // normalize the width to the unit interval from 0 to 1 in this coordinate system,
                final var dxUnit = width.units() / dx;

                // then return the scaled width in the given coordinate system.
                final var dx = dxUnit * that.width();

                return DrawingWidth.width(coordinated, dx);
            }
            else
            {
                return width;
            }
        }
    }

    @Override
    public DrawingHeight toCoordinates(final Coordinated coordinated, final DrawingHeight height)
    {
        ensureNotNull(coordinated);

        final var that = coordinated.coordinates();

        // If we are already in the same coordinate system,
        if (equals(that))
        {
            // the coordinate does not need to be mapped.
            return height;
        }
        else
        {
            // If this coordinate systems are both bounded,
            if (isBounded() && that.isBounded())
            {
                // normalize the width to the unit interval from 0 to 1 in this coordinate system,
                final var dyUnit = height.units() / dy;

                // then return the scaled width in the given coordinate system.
                final var dy = dyUnit * that.height();

                return DrawingHeight.height(coordinated, dy);
            }
            else
            {
                return height;
            }
        }
    }

    @Override
    public DrawingPoint toCoordinates(final Coordinated coordinated, final DrawingPoint point)
    {
        ensureNotNull(coordinated);

        final var that = coordinated.coordinates();

        // If we are already in the same coordinate system,
        if (equals(that))
        {
            // the coordinate does not need to be mapped.
            return point;
        }
        else
        {
            // If this coordinate systems are both bounded,
            if (isBounded() && that.isBounded())
            {
                // normalize the given coordinate to the unit interval from 0 to 1 in this coordinate system,
                final var xUnit = (point.x() - x) / dx;
                final var yUnit = (point.y() - y) / dy;

                // then return the x, y coordinate in the given coordinate system scaled to the same relative position.
                final var x = that.x() + xUnit * that.width();
                final var y = that.y() + yUnit * that.height();

                return point(x, y);
            }
            else
            {
                // otherwise, get the offset of the given coordinate in this coordinate system,
                final var dx = point.x() - x;
                final var dy = point.y() - y;

                // and return the origin of the target coordinate system plus the offset.
                final var x = that.x() + dx;
                final var y = that.y() + dy;

                return point(x, y);
            }
        }
    }

    @Override
    public String toString()
    {
        return name();
    }

    public DrawingCoordinateSystem unbounded()
    {
        return withExtent(MAX_VALUE, MAX_VALUE);
    }

    @Override
    public double width()
    {
        return dx;
    }

    public DrawingCoordinateSystem withExtent(final double dx, final double dy)
    {
        final var copy = copy();
        copy.dx = dx;
        copy.dy = dy;
        return copy;
    }

    public DrawingCoordinateSystem withName(final String name)
    {
        final var copy = copy();
        copy.name = name;
        return copy;
    }

    public DrawingCoordinateSystem withOrigin(final double x, final double y)
    {
        final var copy = copy();
        copy.x = x;
        copy.y = y;
        return copy;
    }

    @Override
    public double x()
    {
        return x;
    }

    @Override
    public double y()
    {
        return y;
    }
}
