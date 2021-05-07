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

package com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d;

import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.DrawingCoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * A Java2D {@link DrawingSurface} with x, y coordinates in a {@link DrawingCoordinateSystem}. The drawing area on the
 * {@link Graphics2D} surface is bounded by a {@link DrawingRectangle}, with an origin in the top left, but not
 * necessarily at 0, 0. A  {@link Java2dDrawingSurface} can be created for a {@link Graphics2D} surface and drawing area
 * on that surface with {@link #surface(String, Graphics2D, DrawingRectangle)}.
 *
 * @author jonathanl (shibo)
 */
public class Java2dDrawingSurface extends DrawingCoordinateSystem implements DrawingSurface
{
    /**
     * @return A Java 2D drawing surface for the given {@link Graphics2D} context with the given origin and size
     */
    public static Java2dDrawingSurface surface(final String name,
                                               final Graphics2D graphics,
                                               final DrawingRectangle area)
    {
        ensureNotNull(graphics);
        ensureNotNull(area);

        return new Java2dDrawingSurface(name, graphics, area);
    }

    /** The graphics surface to draw on */
    private final Graphics2D graphics;

    protected Java2dDrawingSurface(final String name, final Graphics2D graphics, final DrawingRectangle area)
    {
        super(name);

        origin(area.x(), area.y());
        extent(area.width(), area.height());

        this.graphics = graphics;

        graphics.scale(scalingFactor(), scalingFactor());

        final var hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        graphics.setRenderingHints(new RenderingHints(hints));

        System.setProperty("awt.useSystemAAFontSettings", "off");
        System.setProperty("swing.aatext", "false");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape drawBox(final Style style, final DrawingPoint at, final DrawingSize size)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(size);

        final var atLocal = at.toCoordinates(this);
        final var sizeLocal = size.toCoordinates(this);

        return draw(style, new Rectangle2D.Double(
                atLocal.x(),
                atLocal.y(),
                sizeLocal.widthInUnits(),
                sizeLocal.heightInUnits()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape drawDot(final Style style,
                         final DrawingPoint at,
                         final DrawingLength radius)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(radius);

        final var units = radius.toCoordinates(this).units();
        final var x = (int) (at.toCoordinates(this).x() - units / 2);
        final var y = (int) (at.toCoordinates(this).y() - units / 2);

        return draw(style, new Ellipse2D.Double(x, y, units, units));
    }

    @Override
    public Shape drawImage(final DrawingPoint at, final Image image, final Composite composite)
    {
        final var x = (int) at.toCoordinates(this).rounded().x();
        final var y = (int) at.toCoordinates(this).rounded().y();

        final var original = graphics.getComposite();
        graphics.setComposite(composite);
        graphics.drawImage(image, x, y, null);
        graphics.setComposite(original);

        return new Rectangle2D.Double(x, y, image.getWidth(null), image.getHeight(null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape drawLine(final Style style,
                          final DrawingPoint from,
                          final DrawingPoint to)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(from);
        ensureNotNull(to);

        return draw(style, new Line2D.Double(
                from.toCoordinates(this).x(),
                from.toCoordinates(this).y(),
                to.toCoordinates(this).x(),
                to.toCoordinates(this).y()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape drawPath(final Style style, final Path2D path)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(path);

        return draw(style, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape drawRoundedBox(final Style style,
                                final DrawingPoint at,
                                final DrawingSize size,
                                final DrawingLength cornerWidth,
                                final DrawingLength cornerHeight)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(size);

        return draw(style, new RoundRectangle2D.Double(
                at.toCoordinates(this).x(),
                at.toCoordinates(this).y(),
                size.toCoordinates(this).widthInUnits(),
                size.toCoordinates(this).heightInUnits(),
                cornerWidth.units(),
                cornerHeight.units()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawText(final Style style,
                         final DrawingPoint at,
                         final String text)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(text);

        final var dy = height(style, text);
        final var x = at.toCoordinates(this).x();
        final var y = at.toCoordinates(this).y() + dy - fontMetrics(style).getDescent();

        style.textColor().applyAsTextColor(graphics);
        graphics.setFont(style.textFont());
        graphics.drawString(text, (float) x, (float) y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape textShape(final Style style,
                           final DrawingPoint at,
                           final String text)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(text);

        graphics.setFont(style.textFont());

        final var dy = height(style, text);
        final var x = at.toCoordinates(this).x();
        final var y = at.toCoordinates(this).y() + dy - fontMetrics(style).getDescent();

        final var glyphs = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);

        return style.shape(glyphs.getOutline((float) x, (float) y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DrawingSize textSize(final Style style, final String text)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(text);

        final var bounds = textBounds(style, text);
        return size(bounds.getWidth(), bounds.getHeight());
    }

    /**
     * @return The scaling factor to apply to adjust for HiDPI (Retina) displays vs ordinary displays
     */
    private static int scalingFactor()
    {
        final double scale = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getDefaultTransform()
                .getScaleX();

        return (int) Math.round(1.0 / scale);
    }

    /**
     * Draws the given shape in the given style using the style's fill color, fill stroke, draw color and draw stroke.
     *
     * @param style The style to draw in
     * @param shape The shape to draw
     * @return The stroked shape outline of the drawing
     */
    private Shape draw(final Style style, final Shape shape)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(shape);
        ensureNotNull(style.fillColor());
        ensureNotNull(style.drawColor());
        ensureNotNull(style.drawStroke());

        // Set the paint color and stroke,
        style.fillColor().applyAsFillColor(graphics);
        style.fillStroke().apply(graphics);

        // fill the shape,
        graphics.fill(shape);

        // set the draw color and stroke,
        style.drawColor().applyAsDrawColor(graphics);
        style.drawStroke().apply(graphics);

        // draw the shape,
        graphics.draw(shape);

        // and return the stroked shape outline to the caller.
        return style.shape(shape);
    }

    /**
     * @return The {@link FontMetrics} for the style's {@link Font}
     */
    private FontMetrics fontMetrics(final Style style)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(style.textFont());

        graphics.setFont(style.textFont());
        return graphics.getFontMetrics();
    }

    /**
     * @return The height of the given text in the given style
     */
    private double height(final Style style, final String text)
    {
        return textBounds(style, text).getHeight();
    }

    /**
     * @return The bounds of the given text in the given style
     */
    private Rectangle2D textBounds(final Style style, final String text)
    {
        return fontMetrics(style).getStringBounds(text, graphics);
    }
}
