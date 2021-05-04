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
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinated;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.coordinates.CartesianCoordinateSystem.pixelCoordinateSystem;

/**
 * A length in {@link CoordinateSystem} units
 *
 * @author jonathanl (shibo)
 */
public class Length extends Coordinated
{
    public static Length pixels(final double units)
    {
        return new Length(pixelCoordinateSystem(), units);
    }

    public static Length units(final CoordinateSystem system, final double units)
    {
        return new Length(system, units);
    }

    private final double units;

    protected Length(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem);
        this.units = units;
    }

    public Height asHeight()
    {
        return Height.height(coordinateSystem(), units);
    }

    public Width asWidth()
    {
        return Width.width(coordinateSystem(), units);
    }

    public boolean isNonZero()
    {
        return units > 0;
    }

    public Length rounded()
    {
        return units(coordinateSystem(), Math.round(units));
    }

    public Length scaledBy(final Percent percent)
    {
        return newInstance(percent.scale(units));
    }

    public Length scaledBy(final double scaleFactor)
    {
        return newInstance(units * scaleFactor);
    }

    public Length to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    public double units()
    {
        return units;
    }

    protected Length newInstance(final double units)
    {
        return units(coordinateSystem(), units);
    }
}
