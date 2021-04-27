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

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

public class Style
{
    public static Style create()
    {
        return new Style();
    }

    // Filling
    private Color fillColor;

    private Stroke fillStroke;

    // Drawing
    private Color drawColor;

    private Stroke drawStroke;

    // Text
    private Color textColor;

    private Font textFont;

    protected Style()
    {
    }

    private Style(final Style that)
    {
        fillColor = that.fillColor;
        drawColor = that.drawColor;
        fillStroke = that.fillStroke;
        drawStroke = that.drawStroke;
        textColor = that.textColor;
        textFont = that.textFont;
    }

    public Style apply(final Component component)
    {
        applyColors(component);
        applyTextStyle(component);

        return this;
    }

    public Style apply(final Graphics2D graphics)
    {
        applyColors(graphics);
        applyStrokes(graphics);

        return this;
    }

    public Style applyColors(final Component component)
    {
        fillColor.applyAsBackground(component);
        drawColor.applyAsForeground(component);

        return this;
    }

    public Style applyColors(final Graphics2D graphics)
    {
        fillColor.applyAsBackground(graphics);
        drawColor.applyAsForeground(graphics);

        return this;
    }

    public Style applyDrawColor(final Graphics graphics)
    {
        drawColor.applyAsForeground(graphics);
        return this;
    }

    public Style applyDrawStyle(final Graphics2D graphics)
    {
        ensure(drawColor != null);
        ensure(drawStroke != null);

        applyDrawColor(graphics);
        drawStroke.apply(graphics);

        return this;
    }

    public Style applyFillColor(final Graphics2D graphics)
    {
        fillColor.applyAsBackground(graphics);
        return this;
    }

    public Style applyFillStyle(final Graphics2D graphics)
    {
        ensure(fillColor != null);
        ensure(fillStroke != null);

        applyFillColor(graphics);
        fillStroke.apply(graphics);

        return this;
    }

    public Style applyTextColor(final Component component)
    {
        textColor.applyAsForeground(component);
        return this;
    }

    public Style applyTextColor(final Graphics graphics)
    {
        textColor.applyAsForeground(graphics);
        return this;
    }

    public Style applyTextFont(final Graphics graphics)
    {
        graphics.setFont(textFont);
        return this;
    }

    public Style applyTextFont(final Component component)
    {
        component.setFont(textFont);
        return this;
    }

    public Style applyTextStyle(final Graphics graphics)
    {
        assert textColor != null;
        assert textFont != null;

        applyTextColor(graphics);
        applyTextFont(graphics);

        return this;
    }

    public Style applyTextStyle(final Component component)
    {
        assert textColor != null;
        assert textFont != null;

        applyTextFont(component);
        applyTextColor(component);

        return this;
    }

    public Style applyToSelectionStyle(final JTextComponent component)
    {
        fillColor.applyAsSelectionBackground(component);
        drawColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style applyToSelectionStyle(final JList<?> component)
    {
        fillColor.applyAsSelectionBackground(component);
        drawColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style applyToSelectionStyle(final JTable component)
    {
        fillColor.applyAsSelectionBackground(component);
        drawColor.applyAsSelectionForeground(component);

        return this;
    }

    public Style darkened()
    {
        return darkened(Percent.of(10));
    }

    public Style darkened(final Percent percent)
    {
        return withFillColor(fillColor.darkened(percent))
                .withDrawColor(drawColor.darkened(percent));
    }

    public Color drawColor()
    {
        return drawColor;
    }

    @KivaKitIncludeProperty
    public Stroke drawStroke()
    {
        return drawStroke;
    }

    public Color fillColor()
    {
        return fillColor;
    }

    @KivaKitIncludeProperty
    public Stroke fillStroke()
    {
        return fillStroke;
    }

    public Style lightened()
    {
        return lightened(Percent.of(10));
    }

    public Style lightened(final Percent percent)
    {
        return withFillColor(fillColor.lightened(percent))
                .withDrawColor(drawColor.lightened(percent));
    }

    public Shape shape(final Shape shape)
    {
        return Shapes.combine(
                fillStroke().stroked(shape),
                drawStroke().stroked(shape));
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

    public Style withDrawColor(final Color color)
    {
        final var copy = new Style(this);
        copy.drawColor = color;
        return copy;
    }

    public Style withDrawStroke(final Stroke stroke)
    {
        final var copy = new Style(this);
        copy.drawStroke = stroke;
        return copy;
    }

    public Style withFillColor(final Color fill)
    {
        final var copy = new Style(this);
        copy.fillColor = fill;
        return copy;
    }

    public Style withFillStroke(final Stroke stroke)
    {
        final var copy = new Style(this);
        copy.fillStroke = stroke;
        return copy;
    }

    public Style withTextColor(final Color text)
    {
        final var copy = new Style(this);
        copy.textColor = text;
        return copy;
    }

    public Style withTextFont(final Font font)
    {
        final var copy = new Style(this);
        copy.textFont = font;
        return copy;
    }

    public Style withTextFontSize(final int size)
    {
        return withTextFont(new Font(textFont.getFontName(), textFont.getStyle(), size));
    }

    private void applyStrokes(final Graphics2D graphics)
    {
        fillStroke.apply(graphics);
        drawStroke.apply(graphics);
    }
}
