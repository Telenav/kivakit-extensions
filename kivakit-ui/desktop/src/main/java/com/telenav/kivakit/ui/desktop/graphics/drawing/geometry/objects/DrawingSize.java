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
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.createCoordinateSystem;

/**
 * A {@link DrawingWidth} and {@link DrawingHeight}
 *
 * @author jonathanl (shibo)
 */
public class DrawingSize extends DrawingObject
{
    public static DrawingSize pixels(final double width, final double height)
    {
        return size(createCoordinateSystem(), width, height);
    }

    public static DrawingSize size(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        return new DrawingSize(coordinateSystem, width, height);
    }

    private final double width;

    private final double height;

    protected DrawingSize(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        super(coordinateSystem);
        this.width = width;
        this.height = height;
    }

    public DrawingPoint asCoordinate()
    {
        return DrawingPoint.at(coordinateSystem(), width, height);
    }

    public DrawingRectangle asRectangle()
    {
        return DrawingRectangle.rectangle(coordinateSystem(), 0, 0, width, height);
    }

    public DrawingRectangle centeredIn(final DrawingRectangle that)
    {
        final var normalized = normalized(that);
        return normalized
                .topLeft()
                .plus(normalized.size().minus(this).times(0.5))
                .rectangle(this);
    }

    public DrawingHeight height()
    {
        return DrawingHeight.height(coordinateSystem(), height);
    }

    public double heightInUnits()
    {
        return height;
    }

    public DrawingSize minus(final DrawingSize that)
    {
        final var normalized = normalized(that);
        return withSize(widthInUnits() - normalized.widthInUnits(), heightInUnits() - normalized.heightInUnits());
    }

    public DrawingSize plus(final double width, final double height)
    {
        return withSize(this.width + width, this.height + height);
    }

    public DrawingSize plus(final DrawingSize that)
    {
        final var normalized = normalized(that);
        return withSize(width + normalized.width, height + normalized.height);
    }

    public DrawingSize rounded()
    {
        return size(coordinateSystem(), Math.round(width), Math.round(height));
    }

    public DrawingSize scaledBy(final double scaleFactor)
    {
        return withSize(width * scaleFactor, height * scaleFactor);
    }

    public DrawingSize scaledBy(final Percent percent)
    {
        return withSize(percent.scale(width), percent.scale(height));
    }

    public DrawingSize times(final double scaleFactor)
    {
        return withSize(widthInUnits() * scaleFactor, heightInUnits() * scaleFactor);
    }

    public DrawingSize to(final CoordinateSystem system)
    {
        return system.to(system, this);
    }

    @Override
    public String toString()
    {
        return width + ", " + height;
    }

    public DrawingWidth width()
    {
        return DrawingWidth.width(coordinateSystem(), width);
    }

    public double widthInUnits()
    {
        return width;
    }

    public DrawingSize withHeight(final double height)
    {
        return size(coordinateSystem(), width, height);
    }

    public DrawingSize withSize(final double width, final double height)
    {
        return size(coordinateSystem(), width, height);
    }

    public DrawingSize withWidth(final double width)
    {
        return size(coordinateSystem(), width, height);
    }
}
