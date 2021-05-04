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
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.createCoordinateSystem;

/**
 * @author jonathanl (shibo)
 */
public class DrawingHeight extends DrawingLength
{
    public static DrawingHeight height(final CoordinateSystem system, final double units)
    {
        return new DrawingHeight(system, units);
    }

    public static DrawingHeight pixels(final double units)
    {
        return height(createCoordinateSystem(), units);
    }

    protected DrawingHeight(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    public DrawingPoint asCoordinate()
    {
        return DrawingPoint.at(coordinateSystem(), 0, units());
    }

    @Override
    public DrawingHeight rounded()
    {
        return height(coordinateSystem(), Math.round(units()));
    }

    @Override
    public DrawingHeight scaledBy(final Percent percent)
    {
        return (DrawingHeight) super.scaledBy(percent);
    }

    @Override
    public DrawingHeight scaledBy(final double scaleFactor)
    {
        return (DrawingHeight) super.scaledBy(scaleFactor);
    }

    @Override
    public DrawingHeight to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    @Override
    protected DrawingLength newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
