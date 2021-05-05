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

package com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;

import java.util.Objects;

/**
 * Represents an x, y point in a {@link CoordinateSystem}.
 *
 * @author jonathanl (shibo)
 */
public class DrawingPoint extends DrawingObject
{
    /**
     * @return The given x, y coordinate in the given coordinate system
     */
    public static DrawingPoint at(final CoordinateSystem system, final double x, final double y)
    {
        return new DrawingPoint(system, x, y);
    }

    /**
     * @return The given x, y coordinate in an unbounded coordinate system of pixels
     */
    public static DrawingPoint pixels(final double x, final double y)
    {
        return new DrawingPoint(DrawingCoordinateSystem.drawingCoordinateSystem(), x, y);
    }

    /** The x coordinate */
    private final double x;

    /** The y coordinate */
    private final double y;

    protected DrawingPoint(final CoordinateSystem system, final double x, final double y)
    {
        super(system);

        this.x = x;
        this.y = y;
    }

    /**
     * @return This coordinate as a {@link DrawingSize}, where the x coordinate is the width and the y coordinate is the
     * height
     */
    public DrawingSize asSize()
    {
        return DrawingSize.size(coordinateSystem(), x, y);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof DrawingPoint)
        {
            final DrawingPoint that = (DrawingPoint) object;
            return coordinateSystem() == that.coordinateSystem() && x == that.x && y == that.y;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(System.identityHashCode(coordinateSystem()), x, y);
    }

    /**
     * @return This coordinate minus the given x and y delta values
     */
    public DrawingPoint minus(final double dx, final double dy)
    {
        return at(coordinateSystem(), x - dx, y - dy);
    }

    /**
     * @return This coordinate's x and y values minus the given coordinate's x and y values
     */
    public DrawingPoint minus(final DrawingPoint that)
    {
        final var normalized = normalized(that);
        return at(coordinateSystem(), x - normalized.x, y - normalized.y);
    }

    /**
     * @return This coordinate plus the given x and y delta values
     */
    public DrawingPoint plus(final double dx, final double dy)
    {
        return at(coordinateSystem(), x + dx, y + dy);
    }

    /**
     * @return This coordinate plus the given size as an offset
     */
    public DrawingPoint plus(final DrawingSize size)
    {
        final var normalized = normalized(size);
        return at(coordinateSystem(), x + normalized.widthInUnits(), y + normalized.heightInUnits());
    }

    /**
     * @return This coordinate's x and y values plus the given coordinate's x and y values
     */
    public DrawingPoint plus(final DrawingPoint that)
    {
        final var normalized = normalized(that);
        return at(coordinateSystem(), x + normalized.x, y + normalized.y);
    }

    /**
     * @return This coordinate as a rectangle whose width and height are determined by the given size
     */
    public DrawingRectangle rectangle(final DrawingSize size)
    {
        final var normalized = normalized(size);
        return DrawingRectangle.rectangle(this, normalized);
    }

    /**
     * @return This coordinate rounded to the nearest integer x and y values
     */
    public DrawingPoint rounded()
    {
        return at(coordinateSystem(), Math.round(x), Math.round(y));
    }

    /**
     * @return This coordinate with x and y values scaled by the given {@link Percent}
     */
    public DrawingPoint scaledBy(final Percent percent)
    {
        return at(coordinateSystem(), percent.scale(x), percent.scale(y));
    }

    /**
     * @return The width and height between this coordinate and the given one
     */
    public DrawingSize sizeBetween(final DrawingPoint that)
    {
        final var normalized = normalized(that);

        final var width = Math.abs(x() - normalized.x());
        final var height = Math.abs(y() - normalized.y());

        return DrawingSize.size(coordinateSystem(), width, height);
    }

    /**
     * @return This coordinate scaled by the given scale factor
     */
    public DrawingPoint times(final double scaleFactor)
    {
        return at(coordinateSystem(), x * scaleFactor, y * scaleFactor);
    }

    /**
     * @return This coordinate converted to the given coordinate system
     */
    public DrawingPoint to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    @Override
    public String toString()
    {
        return x + ", " + y;
    }

    /**
     * @return The x location of this coordinate in {@link #coordinateSystem()}
     */
    public double x()
    {
        return x;
    }

    /**
     * @return The x location of this coordinate in {@link #coordinateSystem()}
     */
    public double y()
    {
        return y;
    }
}
