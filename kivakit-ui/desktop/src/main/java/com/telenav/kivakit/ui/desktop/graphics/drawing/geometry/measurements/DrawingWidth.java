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

package com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements;

import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.PIXELS;

/**
 * @author jonathanl (shibo)
 */
public class DrawingWidth extends DrawingLength
{
    public static DrawingWidth pixels(double pixels)
    {
        return width(PIXELS, pixels);
    }

    public static DrawingWidth width(Coordinated coordinates, double units)
    {
        return new DrawingWidth(coordinates, units);
    }

    protected DrawingWidth(Coordinated coordinates, double units)
    {
        super(coordinates, units);
    }

    public DrawingPoint asPoint()
    {
        return point(units(), 0);
    }

    @Override
    public DrawingWidth rounded()
    {
        return width(Math.round(units()));
    }

    @Override
    public DrawingWidth scaledBy(Percent percent)
    {
        return (DrawingWidth) super.scaledBy(percent);
    }

    @Override
    public DrawingWidth scaledBy(double scaleFactor)
    {
        return (DrawingWidth) super.scaledBy(scaleFactor);
    }

    @Override
    public DrawingWidth toCoordinates(Coordinated that)
    {
        return coordinates().toCoordinates(that, this);
    }

    @Override
    public String toString()
    {
        return super.toString() + " wide";
    }

    @Override
    protected DrawingLength newInstance(double units)
    {
        return width(units);
    }
}
