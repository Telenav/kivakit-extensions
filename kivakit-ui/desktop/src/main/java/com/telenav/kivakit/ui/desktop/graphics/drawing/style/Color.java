/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // https://www.apache.org/licenses/LICENSE-2.0
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

    public static Color of(java.awt.Color that)
    {
        return rgba(that.getRed(), that.getGreen(), that.getBlue(), that.getAlpha());
    }

    public static Color rgb(int rgb)
    {
        return of(new java.awt.Color(rgb));
    }

    public static Color rgb(int red, int green, int blue)
    {
        return new Color(red, green, blue, 255);
    }

    public static Color rgba(int red, int green, int blue, int alpha)
    {
        return new Color(red, green, blue, alpha);
    }

    public static class ColorConverter extends BaseStringConverter<Color>
    {
        public ColorConverter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected String onToString(Color value)
        {
            return Align.right(Integer.toHexString(value.rgba()), 8, '0');
        }

        @Override
        protected Color onToValue(String value)
        {
            if (value.length() == 8)
            {
                var alpha = Integer.parseInt(value.substring(0, 2), 16);
                var red = Integer.parseInt(value.substring(2, 4), 16);
                var green = Integer.parseInt(value.substring(4, 6), 16);
                var blue = Integer.parseInt(value.substring(6, 8), 16);
                return rgba(red, green, blue, alpha);
            }
            return null;
        }
    }

    private int red;

    private int green;

    private int blue;

    private int alpha;

    private Color(int red, int green, int blue, int alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    private Color(Color that)
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

    public <T extends Component> T applyAsBackground(T component)
    {
        component.setBackground(asAwtColor());
        return component;
    }

    public void applyAsDrawColor(Graphics graphics)
    {
        graphics.setColor(asAwtColor());
    }

    public void applyAsFillColor(Graphics2D graphics)
    {
        graphics.setPaint(asAwtColor());
    }

    public <T extends Component> T applyAsForeground(T component)
    {
        component.setForeground(asAwtColor());
        return component;
    }

    public <T extends JTextComponent> T applyAsSelectionBackground(T component)
    {
        component.setSelectionColor(asAwtColor());
        return component;
    }

    public <T extends JList<?>> T applyAsSelectionBackground(T component)
    {
        component.setSelectionBackground(asAwtColor());
        return component;
    }

    public <T extends JTable> T applyAsSelectionBackground(T component)
    {
        component.setSelectionBackground(asAwtColor());
        return component;
    }

    public <T extends JTable> T applyAsSelectionForeground(T component)
    {
        component.setSelectionForeground(asAwtColor());
        return component;
    }

    public <T extends JTextComponent> T applyAsSelectionForeground(T component)
    {
        component.setSelectedTextColor(asAwtColor());
        return component;
    }

    public <T extends JList<?>> T applyAsSelectionForeground(T component)
    {
        component.setSelectionForeground(asAwtColor());
        return component;
    }

    public void applyAsTextColor(Graphics graphics)
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

    public Color brighter(Percent percent)
    {
        var copy = new Color(this);
        var scaleFactor = 1.0 + percent.asZeroToOne();
        copy.red = Math.min((int) (red() * scaleFactor), 255);
        copy.green = Math.min((int) (green() * scaleFactor), 255);
        copy.blue = Math.min((int) (blue() * scaleFactor), 255);
        return copy;
    }

    public Color darker()
    {
        return darker(Percent.of(15));
    }

    public Color darker(Percent percent)
    {
        var copy = new Color(this);
        var scaleFactor = 1.0 - percent.asZeroToOne();
        copy.red = Math.max((int) (red() * scaleFactor), 0);
        copy.green = Math.max((int) (green() * scaleFactor), 0);
        copy.blue = Math.max((int) (blue() * scaleFactor), 0);
        return copy;
    }

    @Override
    public boolean equals(Object that)
    {
        if (that instanceof Color)
        {
            Color color = (Color) that;
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

    public Color withAlpha(int alpha)
    {
        var copy = new Color(this);
        copy.alpha = alpha;
        return copy;
    }
}
