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

package com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;

/**
 * A length in {@link CoordinateSystem} units
 *
 * @author jonathanl (shibo)
 */
public class DrawingLength extends DrawingObject
{
    public static DrawingLength pixels(final double units)
    {
        return new DrawingLength(DrawingCoordinateSystem.drawingCoordinateSystem(), units);
    }

    public static DrawingLength units(final CoordinateSystem system, final double units)
    {
        return new DrawingLength(system, units);
    }

    private final double units;

    protected DrawingLength(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem);
        this.units = units;
    }

    public DrawingHeight asHeight()
    {
        return DrawingHeight.height(coordinateSystem(), units);
    }

    public DrawingWidth asWidth()
    {
        return DrawingWidth.width(coordinateSystem(), units);
    }

    public boolean isNonZero()
    {
        return units > 0;
    }

    public DrawingLength rounded()
    {
        return units(coordinateSystem(), Math.round(units));
    }

    public DrawingLength scaledBy(final Percent percent)
    {
        return newInstance(percent.scale(units));
    }

    public DrawingLength scaledBy(final double scaleFactor)
    {
        return newInstance(units * scaleFactor);
    }

    public DrawingLength to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    public double units()
    {
        return units;
    }

    protected DrawingLength newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
