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

package com.telenav.kivakit.ui.desktop.graphics.drawing.style;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d.Java2dDrawingSurface;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;

/**
 * Wrapper that adds additional functionality and interoperation to {@link java.awt.Stroke}. Strokes can be added to
 * {@link Style}s that are used by {@link Drawable}s to draw on a {@link DrawingSurface}.
 *
 * <p><b>Construction</b></p>
 *
 * <p>
 * A stroke can be constructed with {@link #stroke()}, and then attributes can be added using the <i>with*()</i>
 * functional methods. Once created, the stroke can be applied to a {@link Graphics2D} drawing surface with {@link
 * #apply(Graphics2D)}. {@link Java2dDrawingSurface} will apply strokes automatically based on drawing {@link Style}s.
 * </p>
 *
 * @author jonathanl (shibo)
 */
public class Stroke
{
    public static Stroke defaultStroke()
    {
        return stroke().withWidth(DrawingWidth.pixels(1));
    }

    public static Stroke stroke()
    {
        return new Stroke();
    }

    public static Stroke stroke(final java.awt.Stroke stroke)
    {
        return new Stroke(stroke);
    }

    private java.awt.Stroke stroke;

    @KivaKitIncludeProperty
    private int cap = CAP_ROUND;

    @KivaKitIncludeProperty
    private int join = JOIN_ROUND;

    @KivaKitIncludeProperty
    private int miterLimit = 0;

    private float[] dash;

    @KivaKitIncludeProperty
    private float dashPhase;

    @KivaKitIncludeProperty
    private DrawingWidth width = DrawingWidth.pixels(1);

    protected Stroke()
    {
    }

    protected Stroke(final java.awt.Stroke stroke)
    {
        this.stroke = stroke;
    }

    protected Stroke(final Stroke that)
    {
        stroke = that.stroke;
        width = that.width;
        cap = that.cap;
        join = that.join;
        miterLimit = that.miterLimit;
        dash = that.dash;
        dashPhase = that.dashPhase;
    }

    public void apply(final Graphics2D graphics)
    {
        graphics.setStroke(awtStroke());
    }

    public Stroke copy()
    {
        return new Stroke(this);
    }

    public Stroke scale(final double scaleFactor)
    {
        return withWidth(width().scaledBy(scaleFactor));
    }

    public Shape stroked(final Shape shape)
    {
        return awtStroke().createStrokedShape(shape);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Stroke withCap(final int cap)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.cap = cap;
        return copy;
    }

    public Stroke withDash(final float[] dash)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.dash = dash;
        return copy;
    }

    public Stroke withDashPhase(final float dash)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.dashPhase = dashPhase;
        return copy;
    }

    public Stroke withJoin(final int join)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.join = join;
        return copy;
    }

    public Stroke withMiterLimit(final int miterLimit)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.miterLimit = miterLimit;
        return copy;
    }

    public Stroke withWidth(final DrawingWidth width)
    {
        final var copy = copy();
        copy.stroke = null;
        copy.width = width;
        return copy;
    }

    protected java.awt.Stroke awtStroke()
    {
        if (stroke == null)
        {
            stroke = new BasicStroke((float) width.units(), cap, join, miterLimit, dash, dashPhase);
        }
        return stroke;
    }

    protected DrawingWidth width()
    {
        return width;
    }
}
