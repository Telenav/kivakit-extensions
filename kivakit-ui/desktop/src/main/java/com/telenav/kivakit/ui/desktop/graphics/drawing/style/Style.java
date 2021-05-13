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

import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d.Java2dShapes;
import org.jetbrains.annotations.NotNull;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Font;
import java.awt.Shape;

import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSPARENT;

/**
 * A style for configuring Swing components and drawing Java 2D shapes and text. A style has a fill color and stroke, a
 * draw color and stroke, and a text color and font. Styles are designed to be reusable and new styles can be derived
 * from existing styles using a variety of functional methods:
 *
 * <ul>
 *     <li>{@link #darkened()} - A copy of this style where all colors are 10% darker</li>
 *     <li>{@link #darkened(Percent)} - A copy of this style where all colors are darkened by the given {@link Percent}</li>
 *     <li>{@link #lightened()} - A copy of this style where all colors are 10% darkened</li>
 *     <li>{@link #lightened(Percent)} - A copy of this style where all colors are lightened by the given {@link Percent}</li>
 *     <li>{@link #translucent()} - A copy of this style where the fill color has an alpha of 192</li>
 *     <li>{@link #transparent()} - A copy of this style where the fill color has an alpha of 0</li>
 *     <li>{@link #withAlpha(int)} - A copy of this style where the fill color has the given alpha value</li>
 *     <li>{@link #withDrawColor(Color)} - A copy of this style with the given drawing color</li>
 *     <li>{@link #withDrawStroke(Stroke)} - A copy of this style with the given drawing {@link Stroke}</li>
 *     <li>{@link #withFillColor(Color)} - A copy of this style with the given fill color</li>
 *     <li>{@link #withFillStroke(Stroke)} - A copy of this style with the given fill {@link Stroke}</li>
 *     <li>{@link #withTextColor(Color)} - A copy of this style with the given text color</li>
 *     <li>{@link #withTextFont(Font)} - A copy of this style with the given text font</li>
 *     <li>{@link #withTextFontSize(int)} - A copy of this style with the given font size</li>
 * </ul>
 */
public class Style
{
    /**
     * @return A style where all colors are transparent
     */
    public static Style create()
    {
        return new Style();
    }

    // Filling
    private Color fillColor = TRANSPARENT;

    private Stroke fillStroke = Stroke.defaultStroke();

    // Drawing
    private Color drawColor = TRANSPARENT;

    private Stroke drawStroke = Stroke.defaultStroke();

    // Text
    private Color textColor = TRANSPARENT;

    private Font textFont = Fonts.component(12);

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
        applyText(component);

        return this;
    }

    public Style applyColors(final Component component)
    {
        fillColor.applyAsBackground(component);
        drawColor.applyAsForeground(component);

        return this;
    }

    public Style applyText(final Component component)
    {
        assert textColor != null;
        assert textFont != null;

        applyTextFont(component);
        applyTextColor(component);

        return this;
    }

    public Style applyTextColor(final Component component)
    {
        textColor.applyAsForeground(component);
        return this;
    }

    public Style applyTextFont(final Component component)
    {
        component.setFont(textFont);
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
        return withFillColor(fillColor.darker(percent))
                .withDrawColor(drawColor.darker(percent))
                .withTextColor(textColor.darker(percent));
    }

    @KivaKitIncludeProperty
    public Color drawColor()
    {
        return drawColor;
    }

    @KivaKitIncludeProperty
    public Stroke drawStroke()
    {
        return drawStroke;
    }

    @KivaKitIncludeProperty
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
        return withFillColor(fillColor.brighter(percent))
                .withDrawColor(drawColor.brighter(percent));
    }

    public Shape shape(final Shape shape)
    {
        return Java2dShapes.combine(
                fillStroke().stroked(shape),
                drawStroke().stroked(shape));
    }

    @KivaKitIncludeProperty
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

    public Style translucent()
    {
        return copy().withFillColor(fillColor().translucent());
    }

    public Style transparent()
    {
        return copy().withFillColor(fillColor().transparent());
    }

    public Style withAlpha(final int alpha)
    {
        return copy().withFillColor(fillColor().withAlpha(alpha));
    }

    public Style withDrawColor(final Color color)
    {
        final var copy = copy();
        copy.drawColor = color;
        return copy;
    }

    public Style withDrawStroke(final Stroke stroke)
    {
        final var copy = copy();
        copy.drawStroke = stroke;
        return copy;
    }

    public Style withFillColor(final Color fill)
    {
        final var copy = copy();
        copy.fillColor = fill;
        return copy;
    }

    public Style withFillStroke(final Stroke stroke)
    {
        final var copy = copy();
        copy.fillStroke = stroke;
        return copy;
    }

    public Style withTextColor(final Color text)
    {
        final var copy = copy();
        copy.textColor = text;
        return copy;
    }

    public Style withTextFont(final Font font)
    {
        final var copy = copy();
        copy.textFont = font;
        return copy;
    }

    public Style withTextFontSize(final int size)
    {
        return withTextFont(new Font(textFont.getFontName(), textFont.getStyle(), size));
    }

    @NotNull
    private Style copy()
    {
        return new Style(this);
    }
}
