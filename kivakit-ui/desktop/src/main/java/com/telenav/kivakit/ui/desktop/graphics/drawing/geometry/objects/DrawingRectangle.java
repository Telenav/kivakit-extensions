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

import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingObject;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem.PIXELS;

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
        return rectangle(PIXELS, x, y, width, height);
    }

    public static DrawingRectangle rectangle(final DrawingPoint a, final DrawingPoint b)
    {
        return new DrawingRectangle(a.coordinates(), a, a.sizeBetween(b));
    }

    public static DrawingRectangle rectangle(final DrawingPoint at, final DrawingSize size)
    {
        return new DrawingRectangle(at.coordinates(), at, size);
    }

    public static DrawingRectangle rectangle(final Coordinated coordinates,
                                             final double x,
                                             final double y,
                                             final double width,
                                             final double height)
    {
        return rectangle(DrawingPoint.point(coordinates, x, y), DrawingSize.size(coordinates, width, height));
    }

    private DrawingPoint at;

    private final DrawingSize size;

    protected DrawingRectangle(final Coordinated coordinates, final DrawingPoint at, final DrawingSize size)
    {
        super(coordinates);

        ensureNotNull(at);
        ensureNotNull(size);

        this.at = toCoordinates(at);
        this.size = toCoordinates(size);
    }

    protected DrawingRectangle(final DrawingRectangle that)
    {
        super(that.coordinates());
        at = that.at;
        size = that.size;
    }

    public DrawingHeight asHeight()
    {
        return size.height();
    }

    public DrawingPoint asPoint()
    {
        return at.plus(size);
    }

    public DrawingWidth asWidth()
    {
        return size.width();
    }

    public DrawingRectangle at(final DrawingPoint that)
    {
        final var at = toCoordinates(that);
        return rectangle(at, size);
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
        return DrawingPoint.point(coordinates(), left(), bottom());
    }

    public DrawingPoint bottomRight()
    {
        return DrawingPoint.point(coordinates(), right(), bottom());
    }

    public DrawingRectangle centeredIn(final DrawingSize that)
    {
        final var size = toCoordinates(that);
        final var copy = copy();
        copy.at = DrawingPoint.point(this.size.coordinates(),
                (size.widthInUnits() - this.size.widthInUnits()) / 2,
                (size.heightInUnits() - this.size.heightInUnits()) / 2);
        return copy;
    }

    public boolean contains(final DrawingPoint that)
    {
        final var point = toCoordinates(that);
        return point.x() >= left() &&
                point.y() >= top() &&
                point.x() < right() &&
                point.y() < bottom();
    }

    public DrawingRectangle copy()
    {
        return new DrawingRectangle(this);
    }

    public double height()
    {
        return size.heightInUnits();
    }

    public boolean intersects(final DrawingRectangle that)
    {
        final var rectangle = toCoordinates(that);

        return !((right() < rectangle.left())
                || (left() > rectangle.right())
                || (top() > rectangle.bottom())
                || (bottom() < rectangle.top()));
    }

    public double left()
    {
        return x();
    }

    public DrawingRectangle plus(final DrawingPoint that)
    {
        final var point = toCoordinates(that);
        return rectangle(at.plus(point), size);
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

    public DrawingRectangle toCoordinates(final Coordinated that)
    {
        return coordinates().toCoordinates(that, this);
    }

    @Override
    public String toString()
    {
        return at + ", " + size;
    }

    public double top()
    {
        return y();
    }

    public DrawingPoint topLeft()
    {
        return at;
    }

    public DrawingPoint topRight()
    {
        return DrawingPoint.point(at.coordinates(), right(), top());
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
