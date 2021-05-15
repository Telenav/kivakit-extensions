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

import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.language.primitives.Ints;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Listener;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Represents a color in RGBA space (red, green, blue, alpha).
 */
public class Color
{
    public static final Color BLACK = of(java.awt.Color.BLACK);

    public static final Color BLUE = of(java.awt.Color.BLUE);

    public static final Color CYAN = of(java.awt.Color.CYAN);

    public static final Color DARK_GRAY = of(java.awt.Color.DARK_GRAY);

    public static final Color GRAY = of(java.awt.Color.GRAY);

    public static final Color GREEN = of(java.awt.Color.GREEN);

    public static final Color LIGHT_GRAY = of(java.awt.Color.LIGHT_GRAY);

    public static final Color MAGENTA = of(java.awt.Color.MAGENTA);

    public static final Color ORANGE = of(java.awt.Color.ORANGE);

    public static final Color PINK = of(java.awt.Color.PINK);

    public static final Color RED = of(java.awt.Color.RED);

    public static final Color TRANSPARENT = rgba(0, 0, 0, 0);

    public static final Color WHITE = of(java.awt.Color.WHITE);

    public static final Color YELLOW = of(java.awt.Color.YELLOW);

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

    public static class ColorConverter extends BaseStringConverter<Color>
    {
        public ColorConverter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected Color onConvertToObject(final String value)
        {
            if (value.length() == 8)
            {
                final var alpha = Integer.parseInt(value.substring(0, 2), 16);
                final var red = Integer.parseInt(value.substring(2, 4), 16);
                final var green = Integer.parseInt(value.substring(4, 6), 16);
                final var blue = Integer.parseInt(value.substring(6, 8), 16);
                return rgba(red, green, blue, alpha);
            }
            return null;
        }

        @Override
        protected String onConvertToString(final Color value)
        {
            return Align.right(Integer.toHexString(value.rgba()), 8, '0');
        }
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

    public <T extends Component> T applyAsBackground(final T component)
    {
        component.setBackground(asAwtColor());
        return component;
    }

    public void applyAsDrawColor(final Graphics graphics)
    {
        graphics.setColor(asAwtColor());
    }

    public void applyAsFillColor(final Graphics2D graphics)
    {
        graphics.setPaint(asAwtColor());
    }

    public <T extends Component> T applyAsForeground(final T component)
    {
        component.setForeground(asAwtColor());
        return component;
    }

    public <T extends JTextComponent> T applyAsSelectionBackground(final T component)
    {
        component.setSelectionColor(asAwtColor());
        return component;
    }

    public <T extends JList<?>> T applyAsSelectionBackground(final T component)
    {
        component.setSelectionBackground(asAwtColor());
        return component;
    }

    public <T extends JTable> T applyAsSelectionBackground(final T component)
    {
        component.setSelectionBackground(asAwtColor());
        return component;
    }

    public <T extends JTable> T applyAsSelectionForeground(final T component)
    {
        component.setSelectionForeground(asAwtColor());
        return component;
    }

    public <T extends JTextComponent> T applyAsSelectionForeground(final T component)
    {
        component.setSelectedTextColor(asAwtColor());
        return component;
    }

    public <T extends JList<?>> T applyAsSelectionForeground(final T component)
    {
        component.setSelectionForeground(asAwtColor());
        return component;
    }

    public void applyAsTextColor(final Graphics graphics)
    {
        graphics.setColor(asAwtColor());
    }

    public java.awt.Color asAwtColor()
    {
        return new java.awt.Color(red(), green(), blue(), alpha());
    }

    public ColorUIResource asColorUiResource()
    {
        return new ColorUIResource(asAwtColor());
    }

    public String asHexString()
    {
        return "#" + Ints.toHex(rgb(), 6);
    }

    public String asHexStringWithAlpha()
    {
        return "#" + Ints.toHex(rgba(), 8);
    }

    public int blue()
    {
        return blue;
    }

    public Color brighter()
    {
        return brighter(Percent.of(15));
    }

    public Color brighter(final Percent percent)
    {
        final var factor = percent.inverse().asZeroToOne();
        final var copy = new Color(this);
        copy.red = Math.min((int) (red() * factor), 255);
        copy.green = Math.min((int) (green() * factor), 255);
        copy.blue = Math.min((int) (blue() * factor), 255);
        return copy;
    }

    public Color darker()
    {
        return darker(Percent.of(15));
    }

    public Color darker(final Percent percent)
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

    @Override
    public String toString()
    {
        return "[Color " + red() + ", " + green() + ", " + blue() + ", " + alpha() + "]";
    }

    public Color translucent()
    {
        return withAlpha(192);
    }

    public Color transparent()
    {
        return withAlpha(0);
    }

    public Color withAlpha(final int alpha)
    {
        final var copy = new Color(this);
        copy.alpha = alpha;
        return copy;
    }
}