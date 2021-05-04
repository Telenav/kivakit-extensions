////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.ui.desktop.graphics.geometry.objects;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Height;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.coordinates.CartesianCoordinateSystem.pixelCoordinateSystem;

/**
 * A {@link Width} and {@link Height}
 *
 * @author jonathanl (shibo)
 */
public class Size extends Coordinated
{
    public static Size pixels(final double width, final double height)
    {
        return size(pixelCoordinateSystem(), width, height);
    }

    public static Size size(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        return new Size(coordinateSystem, width, height);
    }

    private final double width;

    private final double height;

    protected Size(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        super(coordinateSystem);
        this.width = width;
        this.height = height;
    }

    public Point asCoordinate()
    {
        return Point.at(coordinateSystem(), width, height);
    }

    public Rectangle asRectangle()
    {
        return Rectangle.rectangle(coordinateSystem(), 0, 0, width, height);
    }

    public Rectangle centeredIn(final Rectangle that)
    {
        final var normalized = normalized(that);
        return normalized
                .topLeft()
                .plus(normalized.size().minus(this).times(0.5))
                .rectangle(this);
    }

    public Height height()
    {
        return Height.height(coordinateSystem(), height);
    }

    public double heightInUnits()
    {
        return height;
    }

    public Size minus(final Size that)
    {
        final var normalized = normalized(that);
        return withSize(widthInUnits() - normalized.widthInUnits(), heightInUnits() - normalized.heightInUnits());
    }

    public Size plus(final double width, final double height)
    {
        return withSize(this.width + width, this.height + height);
    }

    public Size plus(final Size that)
    {
        final var normalized = normalized(that);
        return withSize(width + normalized.width, height + normalized.height);
    }

    public Size rounded()
    {
        return size(coordinateSystem(), Math.round(width), Math.round(height));
    }

    public Size scaledBy(final double scaleFactor)
    {
        return withSize(width * scaleFactor, height * scaleFactor);
    }

    public Size scaledBy(final Percent percent)
    {
        return withSize(percent.scale(width), percent.scale(height));
    }

    public Size times(final double scaleFactor)
    {
        return withSize(widthInUnits() * scaleFactor, heightInUnits() * scaleFactor);
    }

    public Size to(final CoordinateSystem system)
    {
        return system.to(system, this);
    }

    @Override
    public String toString()
    {
        return width + ", " + height;
    }

    public Width width()
    {
        return Width.width(coordinateSystem(), width);
    }

    public double widthInUnits()
    {
        return width;
    }

    public Size withHeight(final double height)
    {
        return size(coordinateSystem(), width, height);
    }

    public Size withSize(final double width, final double height)
    {
        return size(coordinateSystem(), width, height);
    }

    public Size withWidth(final double width)
    {
        return size(coordinateSystem(), width, height);
    }
}
