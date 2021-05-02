package com.telenav.kivakit.ui.desktop.graphics.drawing.awt;

import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CartesianCoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
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
 * @author jonathanl (shibo)
 */
public class AwtDrawingSurface extends CartesianCoordinateSystem implements DrawingSurface
{
    public static AwtDrawingSurface surface(final Graphics2D graphics,
                                            final Coordinate origin,
                                            final CoordinateSize size)
    {
        ensureNotNull(graphics);
        ensureNotNull(origin);
        ensureNotNull(size);

        return new AwtDrawingSurface(graphics, origin, size);
    }

    private final Graphics2D graphics;

    protected AwtDrawingSurface(final Graphics2D graphics, final Coordinate origin, final CoordinateSize size)
    {
        super(origin, size);

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

    @Override
    public Shape drawBox(final Style style, final Coordinate at, final CoordinateSize size)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(size);

        return draw(style, new Rectangle2D.Double(
                at.to(this).x(),
                at.to(this).y(),
                size.to(this).widthInUnits(),
                size.to(this).heightInUnits()));
    }

    @Override
    public Shape drawCircle(final Style style,
                            final Coordinate at,
                            final CoordinateDistance radius)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(radius);

        final var units = radius.to(this).units();
        final var x = (int) (at.to(this).x() - units / 2);
        final var y = (int) (at.to(this).y() - units / 2);

        return draw(style, new Ellipse2D.Double(x, y, units, units));
    }

    @Override
    public Shape drawLine(final Style style,
                          final Coordinate from,
                          final Coordinate to)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(from);
        ensureNotNull(to);

        return draw(style, new Line2D.Double(
                from.to(this).x(),
                from.to(this).y(),
                to.to(this).x(),
                to.to(this).y()));
    }

    @Override
    public Shape drawPath(final Style style, final Path2D path)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(path);

        return draw(style, path);
    }

    @Override
    public Shape drawRoundedBox(final Style style,
                                final Coordinate at,
                                final CoordinateSize size,
                                final CoordinateDistance cornerWidth,
                                final CoordinateDistance cornerHeight)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(size);

        return draw(style, new RoundRectangle2D.Double(
                at.to(this).x(),
                at.to(this).y(),
                size.to(this).widthInUnits(),
                size.to(this).heightInUnits(),
                cornerWidth.units(),
                cornerHeight.units()));
    }

    /**
     * Draws the given string at the x, y position relative to the top left. Unlike {@link Graphics#drawString(String,
     * int, int)}, the y position is offset by the height of the text, minus the descent of the font. This puts the top
     * left corner of the text at the given x, y position, rather than the bottom left corner.
     */
    @Override
    public Shape drawText(final Style style,
                          final Coordinate at,
                          final String text)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(at);
        ensureNotNull(text);

        graphics.setFont(style.textFont());

        final var dy = height(style, text);
        final var x = at.to(this).x();
        final var y = at.to(this).y() + dy - fontMetrics(style).getDescent();

        final var glyphs = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);
        final var shape = glyphs.getOutline((float) x, (float) y);

        graphics.drawString(text, (float) x, (float) y);

        return style.shape(shape);
    }

    @Override
    public CoordinateSize size(final Style style, final String text)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(text);

        final var bounds = textBounds(style, text);
        return size(bounds.getWidth(), bounds.getHeight());
    }

    private static int scalingFactor()
    {
        final double scale = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .getDefaultTransform()
                .getScaleX();

        return (int) Math.round(1.0 / scale);
    }

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

    private FontMetrics fontMetrics(final Style style)
    {
        ensureNotNull(graphics);
        ensureNotNull(style);
        ensureNotNull(style.textFont());

        graphics.setFont(style.textFont());
        return graphics.getFontMetrics();
    }

    private double height(final Style style, final String text)
    {
        return textBounds(style, text).getHeight();
    }

    private Rectangle2D textBounds(final Style style, final String text)
    {
        return fontMetrics(style).getStringBounds(text, graphics);
    }
}
