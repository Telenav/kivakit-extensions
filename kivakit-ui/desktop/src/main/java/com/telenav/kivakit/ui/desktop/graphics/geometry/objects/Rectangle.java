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

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.ui.desktop.graphics.geometry.coordinates.CartesianCoordinateSystem.pixelCoordinateSystem;

/**
 * A rectangle at a given {@link Point} with a given {@link Size}, in the same coordinate system.
 *
 * @author jonathanl (shibo)
 */
public class Rectangle extends Coordinated
{
    public static Rectangle pixels(final double x,
                                   final double y,
                                   final double width,
                                   final double height)
    {
        return rectangle(pixelCoordinateSystem(), x, y, width, height);
    }

    public static Rectangle rectangle(final Point a, final Point b)
    {
        ensure(a.sameCoordinateSystem(b));

        return new Rectangle(a.coordinateSystem(), a, a.sizeBetween(b));
    }

    public static Rectangle rectangle(final Point at, final Size size)
    {
        ensure(at.sameCoordinateSystem(size));

        return new Rectangle(at.coordinateSystem(), at, size);
    }

    public static Rectangle rectangle(final CoordinateSystem system,
                                      final double x,
                                      final double y,
                                      final double width,
                                      final double height)
    {
        return rectangle(Point.at(system, x, y), Size.size(system, width, height));
    }

    private Point at;

    private final Size size;

    protected Rectangle(final CoordinateSystem system, final Point at, final Size size)
    {
        super(system);

        ensureNotNull(at);
        ensureNotNull(size);

        this.at = normalized(at);
        this.size = normalized(size);
    }

    public Rectangle at(final Point at)
    {
        final var normalized = normalized(at);
        return rectangle(normalized, size);
    }

    public Point at()
    {
        return at;
    }

    public double bottom()
    {
        return top() + height();
    }

    public Point bottomLeft()
    {
        return Point.at(coordinateSystem(), left(), bottom());
    }

    public Point bottomRight()
    {
        return Point.at(coordinateSystem(), right(), bottom());
    }

    public Rectangle centeredIn(final Size that)
    {
        final var normalized = normalized(that);
        final var copy = copy();
        copy.at = Point.at(size.coordinateSystem(),
                (normalized.widthInUnits() - size.widthInUnits()) / 2,
                (normalized.heightInUnits() - size.heightInUnits()) / 2);
        return copy;
    }

    public boolean contains(final Point that)
    {
        final var normalized = normalized(that);
        return normalized.x() >= left() &&
                normalized.y() >= top() &&
                normalized.x() < right() &&
                normalized.y() < bottom();
    }

    public Rectangle copy()
    {
        return new Rectangle(coordinateSystem(), at, size);
    }

    public double height()
    {
        return size.heightInUnits();
    }

    public boolean intersects(final Rectangle that)
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

    public Rectangle plus(final Point that)
    {
        final var normalized = normalized(that);
        return rectangle(at.plus(normalized), size);
    }

    public double right()
    {
        return left() + width();
    }

    public Rectangle rounded()
    {
        return rectangle(at().rounded(), size().rounded());
    }

    public Size size()
    {
        return size;
    }

    public Rectangle to(final CoordinateSystem that)
    {
        return coordinateSystem().to(that, this);
    }

    public Point to()
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

    public Point topLeft()
    {
        return at;
    }

    public Point topRight()
    {
        return Point.at(at.coordinateSystem(), right(), top());
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
