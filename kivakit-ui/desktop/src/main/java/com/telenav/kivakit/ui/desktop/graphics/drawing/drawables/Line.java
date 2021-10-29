////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
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
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingSlope;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

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

    public static Line line(Style style, DrawingPoint from, DrawingPoint to)
    {
        return new Line(style, from, to.minus(from).asSize());
    }

    private Drawable fromArrowHead;

    private Drawable toArrowHead;

    private DrawingSize offset;

    protected Line(Style style, DrawingPoint from, DrawingSize offset)
    {
        super(style, from);
        this.offset = offset;
    }

    protected Line(Line that)
    {
        super(that);
        offset = that.offset;
        fromArrowHead = that.fromArrowHead;
        toArrowHead = that.toArrowHead;
    }

    @Override
    public Line copy()
    {
        return new Line(this);
    }

    @Override
    public Shape draw(DrawingSurface surface)
    {
        var shape = new Area();

        shape.add(new Area(shape(surface.drawLine(style(), from(), to()))));
        shape.add(new Area(fromArrowHead.withLocation(from()).draw(surface)));
        shape.add(new Area(toArrowHead.withLocation(to()).draw(surface)));

        return shape;
    }

    public DrawingPoint from()
    {
        return withLocation();
    }

    public DrawingSize offset()
    {
        return offset;
    }

    @Override
    public Line scaledBy(double scaleFactor)
    {
        var copy = copy();
        copy.offset = offset.scaledBy(scaleFactor);
        return copy;
    }

    public DrawingSlope slope()
    {
        var point = to().minus(from());
        var opposite = point.y();
        var adjacent = point.x();
        return DrawingSlope.radians(Math.atan(opposite / adjacent));
    }

    public DrawingPoint to()
    {
        return withLocation().plus(offset);
    }

    @Override
    public Line withColors(Style style)
    {
        return (Line) super.withColors(style);
    }

    @Override
    public Line withDrawColor(Color color)
    {
        return (Line) super.withDrawColor(color);
    }

    @Override
    public Line withDrawStroke(Stroke stroke)
    {
        return (Line) super.withDrawStroke(stroke);
    }

    @Override
    public Line withDrawStrokeWidth(DrawingWidth width)
    {
        return (Line) super.withDrawStrokeWidth(width);
    }

    @Override
    public Line withFillColor(Color color)
    {
        return (Line) super.withFillColor(color);
    }

    @Override
    public Line withFillStroke(Stroke stroke)
    {
        return (Line) super.withFillStroke(stroke);
    }

    @Override
    public Line withFillStrokeWidth(DrawingWidth width)
    {
        return (Line) super.withFillStrokeWidth(width);
    }

    public Line withFrom(DrawingPoint from)
    {
        return withLocation(from);
    }

    public Line withFromArrowHead(Drawable arrowHead)
    {
        var copy = copy();
        copy.fromArrowHead = arrowHead;
        return copy;
    }

    @Override
    public Line withLocation(DrawingPoint at)
    {
        return (Line) super.withLocation(at);
    }

    public Line withOffset(DrawingSize offset)
    {
        var copy = copy();
        copy.offset = offset;
        return copy;
    }

    @Override
    public Line withStyle(Style style)
    {
        return (Line) super.withStyle(style);
    }

    @Override
    public Line withTextColor(Color color)
    {
        return (Line) super.withTextColor(color);
    }

    public Line withTo(DrawingPoint to)
    {
        return withOffset(to.minus(withLocation()).asSize());
    }

    public Line withToArrowHead(Drawable arrowHead)
    {
        var copy = copy();
        copy.toArrowHead = arrowHead;
        return copy;
    }
}
