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

package com.telenav.kivakit.ui.swing.graphics.style;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.swing.graphics.color.Color;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

public class Style
{
    public static Style create()
    {
        return new Style();
    }

    // Filling
    private Color backgroundColor;

    private Stroke backgroundStroke;

    // Drawing
    private Color foregroundColor;

    private Stroke foregroundStroke;

    // Text
    private Color textColor;

    private Font textFont;

    protected Style()
    {
    }

    private Style(final Style that)
    {
        backgroundColor = that.backgroundColor;
        foregroundColor = that.foregroundColor;
        backgroundStroke = that.backgroundStroke;
        foregroundStroke = that.foregroundStroke;
        textColor = that.textColor;
        textFont = that.textFont;
    }

    public Style apply(final Component component)
    {
        applyColors(component);
        applyTextStyle(component);

        return this;
    }

    public Style applyAsSelectionStyle(final JTextComponent component)
    {
        backgroundColor.applyAsSelectionBackground(component);
        foregroundColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style applyAsSelectionStyle(final JList<?> component)
    {
        backgroundColor.applyAsSelectionBackground(component);
        foregroundColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style applyAsSelectionStyle(final JTable component)
    {
        backgroundColor.applyAsSelectionBackground(component);
        foregroundColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style applyColors(final Component component)
    {
        backgroundColor.applyAsBackground(component);
        foregroundColor.applyAsForeground(component);

        return this;
    }

    public Style applyColors(final Graphics2D graphics)
    {
        backgroundColor.applyAsBackground(graphics);
        foregroundColor.applyAsForeground(graphics);

        return this;
    }

    public Style applyDrawStyle(final Graphics2D graphics)
    {
        ensure(foregroundColor != null);
        ensure(foregroundStroke != null);

        foregroundColor.apply(graphics);
        graphics.setStroke(foregroundStroke);

        return this;
    }

    public Style applyFillStyle(final Graphics2D graphics)
    {
        ensure(backgroundColor != null);
        ensure(backgroundStroke != null);

        backgroundColor.apply(graphics);
        graphics.setStroke(backgroundStroke);

        return this;
    }

    public Style applyFont(final Graphics graphics)
    {
        graphics.setFont(textFont);

        return this;
    }

    public Style applyFont(final Component component)
    {
        component.setFont(textFont);

        return this;
    }

    public Style applyTextStyle(final Graphics graphics)
    {
        assert textColor != null;
        assert textFont != null;

        textColor.apply(graphics);
        applyFont(graphics);

        return this;
    }

    public Style applyTextStyle(final Component component)
    {
        assert textColor != null;
        assert textFont != null;

        component.setFont(textFont);
        textColor.applyAsForeground(component);

        return this;
    }

    public Color backgroundColor()
    {
        return backgroundColor;
    }

    @KivaKitIncludeProperty
    public Stroke backgroundStroke()
    {
        return backgroundStroke;
    }

    public Style darkened()
    {
        return darkened(Percent.of(15));
    }

    public Style darkened(final Percent percent)
    {
        return withBackgroundColor(backgroundColor.darkened(percent)).withForegroundColor(foregroundColor.darkened(percent));
    }

    public FontMetrics fontMetrics(final Graphics graphics)
    {
        applyTextStyle(graphics);
        return graphics.getFontMetrics();
    }

    public Color foregroundColor()
    {
        return foregroundColor;
    }

    @KivaKitIncludeProperty
    public Stroke foregroundStroke()
    {
        return foregroundStroke;
    }

    public Style lightened()
    {
        return lightened(Percent.of(15));
    }

    public Style lightened(final Percent percent)
    {
        return withBackgroundColor(backgroundColor.lightened(percent)).withForegroundColor(foregroundColor.lightened(percent));
    }

    public Rectangle2D textBounds(final Graphics graphics, final String text)
    {
        return fontMetrics(graphics).getStringBounds(text, graphics);
    }

    public Color textColor()
    {
        return textColor;
    }

    @KivaKitIncludeProperty
    public Font textFont()
    {
        return textFont;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Style withBackgroundColor(final Color fill)
    {
        final var copy = new Style(this);
        copy.backgroundColor = fill;
        return copy;
    }

    public Style withBackgroundStroke(final Stroke stroke)
    {
        final var copy = new Style(this);
        copy.backgroundStroke = stroke;
        return copy;
    }

    public Style withFont(final Font font)
    {
        final var copy = new Style(this);
        copy.textFont = font;
        return copy;
    }

    public Style withFontSize(final int size)
    {
        return withFont(new Font(textFont.getFontName(), textFont.getStyle(), size));
    }

    public Style withForegroundColor(final Color color)
    {
        final var copy = new Style(this);
        copy.foregroundColor = color;
        return copy;
    }

    public Style withForegroundStroke(final Stroke stroke)
    {
        final var copy = new Style(this);
        copy.foregroundStroke = stroke;
        return copy;
    }

    public Style withTextColor(final Color text)
    {
        final var copy = new Style(this);
        copy.textColor = text;
        return copy;
    }
}
