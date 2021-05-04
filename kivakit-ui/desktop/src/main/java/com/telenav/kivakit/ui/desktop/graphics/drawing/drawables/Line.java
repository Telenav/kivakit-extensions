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

package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Slope;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Area;

/**
 * A {@link Drawable} line, with optional arrowheads
 *
 * @author jonathanl (shibo)
 */
public class Line extends BaseDrawable
{
    public static Line line()
    {
        return line(null, null, null);
    }

    public static Line line(final Style style, final Point from, final Point to)
    {
        return new Line(style, from, to.minus(from).asSize());
    }

    private Drawable fromArrowHead;

    private Drawable toArrowHead;

    private Size offset;

    protected Line(final Style style, final Point from, final Size offset)
    {
        super(style, from);
        this.offset = offset;
    }

    protected Line(final Line that)
    {
        super(that);
        offset = that.offset;
        fromArrowHead = that.fromArrowHead;
        toArrowHead = that.toArrowHead;
    }

    @Override
    public Line at(final Point at)
    {
        return (Line) super.at(at);
    }

    @Override
    public Line copy()
    {
        return new Line(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        final var shape = new Area();

        shape.add(new Area(shape(surface.drawLine(style(), from(), to()))));
        shape.add(new Area(fromArrowHead.at(from()).draw(surface)));
        shape.add(new Area(toArrowHead.at(to()).draw(surface)));

        return shape;
    }

    public Point from()
    {
        return at();
    }

    public Size offset()
    {
        return offset;
    }

    @Override
    public Line scaledBy(final double scaleFactor)
    {
        final var copy = copy();
        copy.offset = offset.scaledBy(scaleFactor);
        return copy;
    }

    public Slope slope()
    {
        final var point = to().minus(from());
        final var opposite = point.y();
        final var adjacent = point.x();
        return Slope.radians(Math.atan(opposite / adjacent));
    }

    public Point to()
    {
        return at().plus(offset);
    }

    @Override
    public Line withColors(final Style style)
    {
        return (Line) super.withColors(style);
    }

    @Override
    public Line withDrawColor(final Color color)
    {
        return (Line) super.withDrawColor(color);
    }

    @Override
    public Line withDrawStroke(final Stroke stroke)
    {
        return (Line) super.withDrawStroke(stroke);
    }

    @Override
    public Line withDrawStrokeWidth(final Width width)
    {
        return (Line) super.withDrawStrokeWidth(width);
    }

    @Override
    public Line withFillColor(final Color color)
    {
        return (Line) super.withFillColor(color);
    }

    @Override
    public Line withFillStroke(final Stroke stroke)
    {
        return (Line) super.withFillStroke(stroke);
    }

    @Override
    public Line withFillStrokeWidth(final Width width)
    {
        return (Line) super.withFillStrokeWidth(width);
    }

    public Line withFrom(final Point from)
    {
        return at(from);
    }

    public Line withFromArrowHead(final Drawable arrowHead)
    {
        final var copy = copy();
        copy.fromArrowHead = arrowHead;
        return copy;
    }

    public Line withOffset(final Size offset)
    {
        final var copy = copy();
        copy.offset = offset;
        return copy;
    }

    @Override
    public Line withStyle(final Style style)
    {
        return (Line) super.withStyle(style);
    }

    @Override
    public Line withTextColor(final Color color)
    {
        return (Line) super.withTextColor(color);
    }

    public Line withTo(final Point to)
    {
        return withOffset(to.minus(at()).asSize());
    }

    public Line withToArrowHead(final Drawable arrowHead)
    {
        final var copy = copy();
        copy.toArrowHead = arrowHead;
        return copy;
    }
}
