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

import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingSlope;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A line between two {@link DrawingPoint}s with a {@link DrawingSlope}. The points must be in the same {@link
 * CoordinateSystem}.
 *
 * @author jonathanl (shibo)
 */
public class DrawingLine extends DrawingObject
{
    public static DrawingLine line(final DrawingPoint a, final DrawingPoint b)
    {
        ensure(a.sameCoordinateSystem(b));

        return new DrawingLine(a, b);
    }

    private final DrawingPoint a;

    private final DrawingPoint b;

    protected DrawingLine(final DrawingPoint a, final DrawingPoint b)
    {
        super(a.coordinateSystem());

        this.a = a;
        this.b = b;
    }

    public DrawingPoint a()
    {
        return a;
    }

    public DrawingPoint b()
    {
        return b;
    }

    public DrawingSlope slope()
    {
        final var point = b.minus(a);
        final var opposite = point.y();
        final var adjacent = point.x();
        return DrawingSlope.radians(Math.atan(opposite / adjacent));
    }
}
