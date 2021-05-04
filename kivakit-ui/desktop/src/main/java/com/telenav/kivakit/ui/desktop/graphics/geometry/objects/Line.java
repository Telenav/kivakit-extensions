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

import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Slope;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A line between two {@link Point}s with a {@link Slope}. The points must be in the same {@link CoordinateSystem}.
 *
 * @author jonathanl (shibo)
 */
public class Line extends Coordinated
{
    public static Line line(final Point a, final Point b)
    {
        ensure(a.sameCoordinateSystem(b));

        return new Line(a, b);
    }

    private final Point a;

    private final Point b;

    protected Line(final Point a, final Point b)
    {
        super(a.coordinateSystem());

        this.a = a;
        this.b = b;
    }

    public Point a()
    {
        return a;
    }

    public Point b()
    {
        return b;
    }

    public Slope slope()
    {
        final var point = b.minus(a);
        final var opposite = point.y();
        final var adjacent = point.x();
        return Slope.radians(Math.atan(opposite / adjacent));
    }
}
