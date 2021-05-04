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

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.createCoordinateSystem;

/**
 * A rectangle at a given {@link DrawingPoint} with a given {@link DrawingSize}, in the same coordinate system.
 *
 * @author jonathanl (shibo)
 */
public class DrawingRectangle extends DrawingObject
{
    public static DrawingRectangle pixels(final double x,
                                          final double y,
                                          final double width,
                                          final double height)
    {
        return rectangle(createCoordinateSystem(), x, y, width, height);
    }

    public static DrawingRectangle rectangle(final DrawingPoint a, final DrawingPoint b)
    {
        ensure(a.sameCoordinateSystem(b));

        return new DrawingRectangle(a.coordinateSystem(), a, a.sizeBetween(b));
    }

    public static DrawingRectangle rectangle(final DrawingPoint at, final DrawingSize size)
    {
        ensure(at.sameCoordinateSystem(size));

        return new DrawingRectangle(at.coordinateSystem(), at, size);
    }

    public static DrawingRectangle rectangle(final CoordinateSystem system,
                                             final double x,
                                             final double y,
                                             final double width,
                                             final double height)
    {
        return rectangle(DrawingPoint.at(system, x, y), DrawingSize.size(system, width, height));
    }

    private DrawingPoint at;

    private final DrawingSize size;

    protected DrawingRectangle(final CoordinateSystem system, final DrawingPoint at, final DrawingSize size)
    {
        super(system);

        ensureNotNull(at);
        ensureNotNull(size);

        this.at = normalized(at);
        this.size = normalized(size);
    }

    public DrawingRectangle at(final DrawingPoint at)
    {
        final var normalized = normalized(at);
        return rectangle(normalized, size);
    }

    public DrawingPoint at()
    {
        return at;
    }

    public double bottom()
    {
        return top() + height();
    }

    public DrawingPoint bottomLeft()
    {
        return DrawingPoint.at(coordinateSystem(), left(), bottom());
    }

    public DrawingPoint bottomRight()
    {
        return DrawingPoint.at(coordinateSystem(), right(), bottom());
    }

    public DrawingRectangle centeredIn(final DrawingSize that)
    {
        final var normalized = normalized(that);
        final var copy = copy();
        copy.at = DrawingPoint.at(size.coordinateSystem(),
                (normalized.widthInUnits() - size.widthInUnits()) / 2,
                (normalized.heightInUnits() - size.heightInUnits()) / 2);
        return copy;
    }

    public boolean contains(final DrawingPoint that)
    {
        final var normalized = normalized(that);
        return normalized.x() >= left() &&
                normalized.y() >= top() &&
                normalized.x() < right() &&
                normalized.y() < bottom();
    }

    public DrawingRectangle copy()
    {
        return new DrawingRectangle(coordinateSystem(), at, size);
    }

    public double height()
    {
        return size.heightInUnits();
    }

    public boolean intersects(final DrawingRectangle that)
    {
        final var normalized = normalized(that);

        return !((right() < normalized.left())
                || (left() > normalized.right())
                || (top() > normalized.bottom())
                || (bottom() < normalized.top()));
    }

    public double left()
    {
        return at().x();
    }

    public DrawingRectangle plus(final DrawingPoint that)
    {
        final var normalized = normalized(that);
        return rectangle(at.plus(normalized), size);
    }

    public double right()
    {
        return left() + width();
    }

    public DrawingRectangle rounded()
    {
        return rectangle(at().rounded(), size().rounded());
    }

    public DrawingSize size()
    {
        return size;
    }

    public DrawingRectangle to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    public DrawingPoint to()
    {
        return at.plus(size);
    }

    @Override
    public String toString()
    {
        return at + ", " + size;
    }

    public double top()
    {
        return at().y();
    }

    public DrawingPoint topLeft()
    {
        return at;
    }

    public DrawingPoint topRight()
    {
        return DrawingPoint.at(at.coordinateSystem(), right(), top());
    }

    public double width()
    {
        return size.widthInUnits();
    }

    public double x()
    {
        return at.x();
    }

    public double y()
    {
        return at.y();
    }
}
