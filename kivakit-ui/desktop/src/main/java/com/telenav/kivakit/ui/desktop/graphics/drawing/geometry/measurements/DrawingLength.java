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
import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.PIXELS;

/**
 * A length in {@link CoordinateSystem} units
 *
 * @author jonathanl (shibo)
 */
public class DrawingLength extends DrawingObject
{
    public static DrawingLength length(Coordinated coordinates, double units)
    {
        return new DrawingLength(coordinates, units);
    }

    public static DrawingLength pixels(double pixels)
    {
        return length(PIXELS, pixels);
    }

    private final double units;

    protected DrawingLength(Coordinated coordinates, double units)
    {
        super(coordinates);

        this.units = units;
    }

    public DrawingHeight asHeight()
    {
        return height(units);
    }

    public DrawingWidth asWidth()
    {
        return width(units);
    }

    public boolean isNonZero()
    {
        return !isZero();
    }

    public boolean isZero()
    {
        return units == 0;
    }

    public DrawingLength rounded()
    {
        return length(Math.round(units));
    }

    public DrawingLength scaledBy(Percent percent)
    {
        return newInstance(percent.scale(units));
    }

    public DrawingLength scaledBy(double scaleFactor)
    {
        return newInstance(units * scaleFactor);
    }

    public DrawingLength toCoordinates(Coordinated that)
    {
        return coordinates().toCoordinates(that, this);
    }

    @Override
    public String toString()
    {
        return super.toString() + ": " + units + " units";
    }

    public double units()
    {
        return units;
    }

    protected DrawingLength newInstance(double units)
    {
        return length(units);
    }
}
