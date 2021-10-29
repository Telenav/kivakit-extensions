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

import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

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

    public static Dot dot(Style style)
    {
        return new Dot(style);
    }

    private DrawingLength radius;

    protected Dot(Style style)
    {
        super(style);
    }

    private Dot(Dot that)
    {
        super(that);
        radius = that.radius;
    }

    @Override
    public Dot copy()
    {
        return new Dot(this);
    }

    @Override
    public Shape draw(DrawingSurface surface)
    {
        return shape(surface.drawDot(style(), withLocation(), radius()));
    }

    public DrawingLength radius()
    {
        return radius;
    }

    @Override
    public Dot scaledBy(double scaleFactor)
    {
        return withRadius(radius.scaledBy(scaleFactor));
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    @Override
    public Dot withColors(Style style)
    {
        return (Dot) super.withColors(style);
    }

    @Override
    public Dot withDrawColor(Color color)
    {
        return (Dot) super.withDrawColor(color);
    }

    @Override
    public Dot withDrawStroke(Stroke stroke)
    {
        return (Dot) super.withDrawStroke(stroke);
    }

    @Override
    public Dot withDrawStrokeWidth(DrawingWidth width)
    {
        return (Dot) super.withDrawStrokeWidth(width);
    }

    @Override
    public Dot withFillColor(Color color)
    {
        return (Dot) super.withFillColor(color);
    }

    @Override
    public Dot withFillStroke(Stroke stroke)
    {
        return (Dot) super.withFillStroke(stroke);
    }

    @Override
    public Dot withFillStrokeWidth(DrawingWidth width)
    {
        return (Dot) super.withFillStrokeWidth(width);
    }

    @Override
    public Dot withLocation(DrawingPoint at)
    {
        return (Dot) super.withLocation(at);
    }

    public Dot withRadius(DrawingLength radius)
    {
        var copy = copy();
        copy.radius = radius;
        return copy;
    }

    @Override
    public Dot withStyle(Style style)
    {
        return (Dot) super.withStyle(style);
    }

    @Override
    public Dot withTextColor(Color color)
    {
        return (Dot) super.withTextColor(color);
    }
}
