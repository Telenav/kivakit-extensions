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

package com.telenav.kivakit.ui.desktop.graphics.style;

import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;

import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;

/**
 * @author jonathanl (shibo)
 */
public class Stroke
{
    public static Stroke none()
    {
        return stroke().withWidth(CoordinateDistance.units(0));
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

    private int cap = CAP_ROUND;

    private int join = JOIN_ROUND;

    private int miterLimit = 0;

    private float[] dash;

    private float dashPhase;

    private CoordinateDistance width = CoordinateDistance.units(1);

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
        if (isVisible())
        {
            graphics.setStroke(awtStroke());
        }
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
        if (isVisible())
        {
            return awtStroke().createStrokedShape(shape);
        }
        return null;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(stroke).toString();
    }

    public Stroke withCap(final int cap)
    {
        final var copy = copy();
        copy.cap = cap;
        return copy;
    }

    public Stroke withDash(final float[] dash)
    {
        final var copy = copy();
        copy.dash = dash;
        return copy;
    }

    public Stroke withDashPhase(final float dash)
    {
        final var copy = copy();
        copy.dashPhase = dashPhase;
        return copy;
    }

    public Stroke withJoin(final int join)
    {
        final var copy = copy();
        copy.join = join;
        return copy;
    }

    public Stroke withMiterLimit(final int miterLimit)
    {
        final var copy = copy();
        copy.miterLimit = miterLimit;
        return copy;
    }

    public Stroke withWidth(final CoordinateDistance width)
    {
        final var copy = copy();
        copy.width = width;
        return copy;
    }

    protected java.awt.Stroke awtStroke()
    {
        if (stroke == null && isVisible())
        {
            stroke = new BasicStroke((float) width.units(), cap, join, miterLimit, dash, dashPhase);
        }
        return stroke;
    }

    protected CoordinateDistance width()
    {
        return width;
    }

    private boolean isVisible()
    {
        return width.isNonZero();
    }
}
