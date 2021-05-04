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

import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Length;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;

/**
 * A {@link Drawable} dot with a given style and {@link #radius()}.
 *
 * @author jonathanl (shibo)
 */
public class Dot extends BaseDrawable
{
    public static Dot dot()
    {
        return dot((Style) null);
    }

    public static Dot dot(final Style style)
    {
        return new Dot(style);
    }

    private Length radius;

    protected Dot(final Style style)
    {
        super(style);
    }

    private Dot(final Dot that)
    {
        super(that);
        radius = that.radius;
    }

    @Override
    public Dot at(final Point at)
    {
        return (Dot) super.at(at);
    }

    @Override
    public Dot copy()
    {
        return new Dot(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawCircle(style(), at(), radius()));
    }

    public Length radius()
    {
        return radius;
    }

    @Override
    public Dot scaledBy(final double scaleFactor)
    {
        return withRadius(radius.scaledBy(scaleFactor));
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @Override
    public Dot withColors(final Style style)
    {
        return (Dot) super.withColors(style);
    }

    @Override
    public Dot withDrawColor(final Color color)
    {
        return (Dot) super.withDrawColor(color);
    }

    @Override
    public Dot withDrawStroke(final Stroke stroke)
    {
        return (Dot) super.withDrawStroke(stroke);
    }

    @Override
    public Dot withDrawStrokeWidth(final Width width)
    {
        return (Dot) super.withDrawStrokeWidth(width);
    }

    @Override
    public Dot withFillColor(final Color color)
    {
        return (Dot) super.withFillColor(color);
    }

    @Override
    public Dot withFillStroke(final Stroke stroke)
    {
        return (Dot) super.withFillStroke(stroke);
    }

    @Override
    public Dot withFillStrokeWidth(final Width width)
    {
        return (Dot) super.withFillStrokeWidth(width);
    }

    public Dot withRadius(final Length radius)
    {
        final var copy = copy();
        copy.radius = radius;
        return copy;
    }

    @Override
    public Dot withStyle(final Style style)
    {
        return (Dot) super.withStyle(style);
    }

    @Override
    public Dot withTextColor(final Color color)
    {
        return (Dot) super.withTextColor(color);
    }
}
