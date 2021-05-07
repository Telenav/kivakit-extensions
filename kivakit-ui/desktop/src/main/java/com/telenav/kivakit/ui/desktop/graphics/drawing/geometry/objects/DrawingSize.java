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
import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.PIXELS;

/**
 * A {@link DrawingWidth} and {@link DrawingHeight}
 *
 * @author jonathanl (shibo)
 */
public class DrawingSize extends DrawingObject
{
    public static DrawingSize pixels(final double width, final double height)
    {
        return size(PIXELS, width, height);
    }

    public static DrawingSize size(final Coordinated coordinates, final double width, final double height)
    {
        return new DrawingSize(coordinates, width, height);
    }

    private final double width;

    private final double height;

    protected DrawingSize(final Coordinated coordinates, final double width, final double height)
    {
        super(coordinates);
        this.width = width;
        this.height = height;
    }

    public DrawingPoint asPoint()
    {
        return point(width, height);
    }

    public DrawingRectangle asRectangle()
    {
        return rectangle(0, 0, width, height);
    }

    public DrawingRectangle centeredIn(final DrawingRectangle that)
    {
        final var rectangle = toCoordinates(that);
        return rectangle
                .topLeft()
                .plus(rectangle.size().minus(this).times(0.5))
                .rectangle(this);
    }

    public DrawingHeight height()
    {
        return height(height);
    }

    public double heightInUnits()
    {
        return height;
    }

    public DrawingSize minus(final DrawingSize that)
    {
        final var size = toCoordinates(that);
        return withSize(widthInUnits() - size.widthInUnits(), heightInUnits() - size.heightInUnits());
    }

    public DrawingSize plus(final double width, final double height)
    {
        return withSize(this.width + width, this.height + height);
    }

    public DrawingSize plus(final DrawingSize that)
    {
        final var size = toCoordinates(that);
        return withSize(width + size.width, height + size.height);
    }

    public DrawingSize rounded()
    {
        return size(Math.round(width), Math.round(height));
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

    public DrawingSize toCoordinates(final Coordinated that)
    {
        return coordinates().toCoordinates(that, this);
    }

    @Override
    public String toString()
    {
        return super.toString() + ": " + width + " x " + height;
    }

    public DrawingWidth width()
    {
        return width(width);
    }

    public double widthInUnits()
    {
        return width;
    }

    public DrawingSize withHeight(final double height)
    {
        return size(width, height);
    }

    public DrawingSize withSize(final double width, final double height)
    {
        return size(width, height);
    }

    public DrawingSize withWidth(final double width)
    {
        return size(width, height);
    }
}
