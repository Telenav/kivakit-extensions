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

package com.telenav.kivakit.ui.swing.graphics.color;

import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import java.awt.*;
import java.util.Objects;

public class Color
{
    public static final Color LIGHT_GRAY = of(java.awt.Color.LIGHT_GRAY);

    public static final Color WHITE = of(java.awt.Color.WHITE);

    public static final Color GRAY = of(java.awt.Color.GRAY);

    public static final Color DARK_GRAY = of(java.awt.Color.DARK_GRAY);

    public static final Color BLACK = of(java.awt.Color.BLACK);

    public static final Color RED = of(java.awt.Color.RED);

    public static final Color PINK = of(java.awt.Color.PINK);

    public static final Color ORANGE = of(java.awt.Color.ORANGE);

    public static final Color YELLOW = of(java.awt.Color.YELLOW);

    public static final Color GREEN = of(java.awt.Color.GREEN);

    public static final Color MAGENTA = of(java.awt.Color.MAGENTA);

    public static final Color CYAN = of(java.awt.Color.CYAN);

    public static final Color BLUE = of(java.awt.Color.BLUE);

    public static final Color TRANSPARENT = rgba(0, 0, 0, 0);

    public static Color of(final java.awt.Color that)
    {
        return rgba(that.getRed(), that.getGreen(), that.getBlue(), that.getAlpha());
    }

    public static Color rgb(final int rgb)
    {
        return of(new java.awt.Color(rgb));
    }

    public static Color rgb(final int red, final int green, final int blue)
    {
        return new Color(red, green, blue, 255);
    }

    public static Color rgba(final int red, final int green, final int blue, final int alpha)
    {
        return new Color(red, green, blue, alpha);
    }

    private int red;

    private int green;

    private int blue;

    private int alpha;

    private Color(final int red, final int green, final int blue, final int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    private Color(final Color that)
    {
        red = that.red();
        green = that.green();
        blue = that.blue();
        alpha = that.alpha();
    }

    public int alpha()
    {
        return alpha;
    }

    public java.awt.Color asAwtColor()
    {
        return new java.awt.Color(red(), green(), blue(), alpha());
    }

    public <T extends Component> T background(final T component)
    {
        component.setBackground(asAwtColor());
        return component;
    }

    public int blue()
    {
        return blue;
    }

    public Color darkened()
    {
        return darkened(Percent.of(15));
    }

    public Color darkened(final Percent percent)
    {
        final var factor = percent.inverse().asZeroToOne();
        final var copy = new Color(this);
        copy.red = Math.max((int) (red() * factor), 0);
        copy.green = Math.max((int) (green() * factor), 0);
        copy.blue = Math.max((int) (blue() * factor), 0);
        return copy;
    }

    @Override
    public boolean equals(final Object that)
    {
        if (that instanceof Color)
        {
            final Color color = (Color) that;
            return red == color.red
                    && green == color.green
                    && blue == color.blue
                    && alpha == color.alpha;
        }
        return false;
    }

    public <T extends Component> T foreground(final T component)
    {
        component.setForeground(asAwtColor());
        return component;
    }

    public int green()
    {
        return green;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(red, green, blue, alpha);
    }

    public Color invert()
    {
        return rgb((~red()) & 255, (~green()) & 255, (~blue()) & 255);
    }

    public Color lightened()
    {
        return lightened(Percent.of(15));
    }

    public Color lightened(final Percent percent)
    {
        final var factor = percent.inverse().asZeroToOne();
        final var copy = new Color(this);
        copy.red = Math.min((int) (red() * factor), 255);
        copy.green = Math.min((int) (green() * factor), 255);
        copy.blue = Math.min((int) (blue() * factor), 255);
        return copy;
    }

    public int red()
    {
        return red;
    }

    public int rgb()
    {
        return rgba() & 0xffffff;
    }

    public int rgba()
    {
        return asAwtColor().getRGB();
    }

    public String toHex()
    {
        return "#" + Ints.toHex(rgb(), 6);
    }

    public String toHexWithAlpha()
    {
        return "#" + Ints.toHex(rgba(), 8);
    }

    @Override
    public String toString()
    {
        return "[Color " + red() + ", " + green() + ", " + blue() + ", " + alpha() + "]";
    }

    public Color withAlpha(final int alpha)
    {
        final var copy = new Color(this);
        copy.alpha = alpha;
        return copy;
    }
}
