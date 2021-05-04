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

package com.telenav.kivakit.ui.desktop.graphics.geometry.measurements;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.coordinates.CartesianCoordinateSystem.pixelCoordinateSystem;

/**
 * @author jonathanl (shibo)
 */
public class Width extends Length
{
    public static Width pixels(final double units)
    {
        return width(pixelCoordinateSystem(), units);
    }

    public static Width width(final CoordinateSystem system, final double units)
    {
        return new Width(system, units);
    }

    protected Width(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    public Point asCoordinate()
    {
        return Point.at(coordinateSystem(), units(), 0);
    }

    @Override
    public Width rounded()
    {
        return width(coordinateSystem(), Math.round(units()));
    }

    @Override
    public Width scaledBy(final Percent percent)
    {
        return (Width) super.scaledBy(percent);
    }

    @Override
    public Width scaledBy(final double scaleFactor)
    {
        return (Width) super.scaledBy(scaleFactor);
    }

    @Override
    public Width to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    @Override
    protected Length newInstance(final double units)
    {
        return width(coordinateSystem(), units);
    }
}
