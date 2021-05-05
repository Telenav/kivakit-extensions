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
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;

/**
 * @author jonathanl (shibo)
 */
public class DrawingWidth extends DrawingLength
{
    public static DrawingWidth pixels(final double units)
    {
        return width(DrawingCoordinateSystem.drawingCoordinateSystem(), units);
    }

    public static DrawingWidth width(final CoordinateSystem system, final double units)
    {
        return new DrawingWidth(system, units);
    }

    protected DrawingWidth(final CoordinateSystem coordinateSystem, final double units)
    {
        super(coordinateSystem, units);
    }

    public DrawingPoint asCoordinate()
    {
        return DrawingPoint.at(coordinateSystem(), units(), 0);
    }

    @Override
    public DrawingWidth rounded()
    {
        return width(coordinateSystem(), Math.round(units()));
    }

    @Override
    public DrawingWidth scaledBy(final Percent percent)
    {
        return (DrawingWidth) super.scaledBy(percent);
    }

    @Override
    public DrawingWidth scaledBy(final double scaleFactor)
    {
        return (DrawingWidth) super.scaledBy(scaleFactor);
    }

    @Override
    public DrawingWidth to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    @Override
    protected DrawingLength newInstance(final double units)
    {
        return width(coordinateSystem(), units);
    }
}
