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

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A base {@link Drawable} implementation, with a {@link Style}, retrieved with {@link #style()}, and a {@link
 * DrawingPoint} location, retrieved with {@link #at()}. When the drawable is drawn with {@link #draw(DrawingSurface)},
 * it then has a shape that can be retrieved with {@link #shape()}. A copy of a drawable can be retrieved with {@link
 * #copy()} and the <i>with*()</i> functional methods can be used to create copies with new attributes.
 *
 * @author jonathanl (shibo)
 */
public abstract class BaseDrawable implements Drawable
{
    private Style style;

    private DrawingPoint at;

    private Shape shape;

    public BaseDrawable(final Style style, final DrawingPoint at)
    {
        this(style);
        this.at = at;
    }

    protected BaseDrawable(final Style style)
    {
        this.style = style;
    }

    @SuppressWarnings("ConstantConditions")
    protected BaseDrawable(final BaseDrawable that)
    {
        ensure(that != null);

        style = that.style;
        shape = that.shape;
        at = that.at;
    }

    @Override
    public Drawable at(final DrawingPoint at)
    {
        final var copy = (BaseDrawable) copy();
        copy.at = at;
        return copy;
    }

    @Override
    public DrawingPoint at()
    {
        return at;
    }

    @Override
    public abstract BaseDrawable copy();

    @Override
    public Shape shape()
    {
        return shape;
    }

    @Override
    @KivaKitIncludeProperty
    public Style style()
    {
        return style;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @Override
    public BaseDrawable withColors(final Style style)
    {
        return withFillColor(style.fillColor())
                .withDrawColor(style.drawColor())
                .withFillColor(style.textColor());
    }

    @Override
    public BaseDrawable withDrawColor(final Color color)
    {
        return withStyle(style.withDrawColor(color));
    }

    @Override
    public BaseDrawable withDrawStroke(final Stroke stroke)
    {
        return withStyle(style.withDrawStroke(stroke));
    }

    @Override
    public BaseDrawable withDrawStrokeWidth(final DrawingWidth width)
    {
        return withStyle(style.withDrawStroke(style.drawStroke().withWidth(width)));
    }

    @Override
    public BaseDrawable withFillColor(final Color color)
    {
        return withStyle(style.withFillColor(color));
    }

    @Override
    public BaseDrawable withFillStroke(final Stroke stroke)
    {
        return withStyle(style.withFillStroke(stroke));
    }

    @Override
    public BaseDrawable withFillStrokeWidth(final DrawingWidth width)
    {
        return withStyle(style.withFillStroke(style.fillStroke().withWidth(width)));
    }

    @Override
    public BaseDrawable withStyle(final Style style)
    {
        final var copy = (BaseDrawable) copy();
        copy.style = style;
        return copy;
    }

    @Override
    public BaseDrawable withTextColor(final Color color)
    {
        return withStyle(style.withTextColor(color));
    }

    protected Box box(final DrawingSize size)
    {
        return Box.box(style)
                .at(at)
                .withSize(size);
    }

    protected Dot dot(final DrawingLength radius)
    {
        return Dot.dot(style)
                .at(at)
                .withRadius(radius);
    }

    protected Label label(final String text)
    {
        return Label.label(style, text).at(at);
    }

    protected Line line(final DrawingPoint from, final DrawingPoint to)
    {
        return Line.line(style, from, to);
    }

    protected Shape shape(final Shape shape)
    {
        this.shape = shape;
        return shape;
    }

    protected Text text(final String text)
    {
        return Text.text(style, text).at(at);
    }
}
