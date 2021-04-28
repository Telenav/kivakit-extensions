package com.telenav.kivakit.ui.desktop.graphics.drawing.awt;

import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingDistance;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateHeight;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateWidth;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingDistance.pixels;

/**
 * @author jonathanl (shibo)
 */
public class AwtDrawingSurface implements DrawingSurface
{
    public static AwtDrawingSurface surface(final Graphics2D graphics)
    {
        return new AwtDrawingSurface(graphics);
    }

    private final Graphics2D graphics;

    protected AwtDrawingSurface(final Graphics2D graphics)
    {
        this.graphics = graphics;

        final var hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics.setRenderingHints(new RenderingHints(hints));
    }

    public Shape drawBox(final Style style,
                         final DrawingPoint at,
                         final DrawingDistance width,
                         final DrawingDistance height)
    {
        return draw(style, new Rectangle2D.Double(at.x(), at.y(), width.units(), height.units()));
    }

    @Override
    public Shape drawBox(final Style style, final DrawingPoint at, final DrawingSize size)
    {
        return draw(style, new Rectangle2D.Double(at.x(), at.y(), size.width(), size.height()));
    }

    @Override
    public Shape drawCircle(final Style style,
                            final DrawingPoint at,
                            final DrawingDistance radius)
    {
        final var units = radius.units();
        final var x = (int) (at.x() - units / 2);
        final var y = (int) (at.y() - units / 2);

        return draw(style, new Ellipse2D.Double(x, y, units, units));
    }

    @Override
    public Shape drawLine(final Style style,
                          final DrawingPoint from,
                          final DrawingPoint to)
    {
        return draw(style, new Line2D.Double(from.x(), from.y(), to.x(), to.y()));
    }

    @Override
    public Shape drawPath(final Style style, final Path2D path)
    {
        return draw(style, path);
    }

    /**
     * Draws the given string at the x, y position relative to the top left. Unlike {@link Graphics#drawString(String,
     * int, int)}, the y position is offset by the height of the text, minus the descent of the font. This puts the top
     * left corner of the text at the given x, y position, rather than the bottom left corner.
     */
    @Override
    public Shape drawText(final Style style,
                          final DrawingPoint at,
                          final String text)
    {
        final var dy = height(style, text);
        final var x = at.x();
        final var y = at.y() + dy - fontMetrics(style).getDescent();

        final GlyphVector glyphs = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);
        final Shape shape = glyphs.getOutline((float) x, (float) y);

        return draw(style, shape);
    }

    @Override
    public DrawingSize size(final Style style, final String text)
    {
        final var bounds = textBounds(style, text);
        return DrawingSize.size(bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public Coordinate toCoordinates(final DrawingPoint point)
    {
        return Coordinate.at(this, point.x(), point.y());
    }

    @Override
    public CoordinateSize toCoordinates(final DrawingSize size)
    {
        return CoordinateSize.size(this, size.width(), size.height());
    }

    @Override
    public CoordinateDistance toCoordinates(final DrawingDistance distance)
    {
        return CoordinateDistance.units(this, distance.units());
    }

    @Override
    public DrawingDistance toDrawingUnits(final CoordinateHeight height)
    {
        return pixels(height.units());
    }

    @Override
    public DrawingDistance toDrawingUnits(final CoordinateWidth width)
    {
        return pixels(width.units());
    }

    @Override
    public DrawingDistance toDrawingUnits(final CoordinateDistance distance)
    {
        return pixels(distance.units());
    }

    @Override
    public DrawingPoint toDrawingUnits(final Coordinate coordinate)
    {
        return DrawingPoint.at(coordinate.inDrawingUnits().x(), coordinate.inDrawingUnits().y());
    }

    @Override
    public DrawingSize toDrawingUnits(final CoordinateSize coordinate)
    {
        return DrawingSize.size(coordinate.width(), coordinate.height());
    }

    private Shape draw(final Style style, final Shape shape)
    {
        // Set the paint color with Graphics2D.setPaint(),
        style.fillColor().applyAsFillColor(graphics);

        // set the drawing color,
        style.drawColor().applyAsDrawColor(graphics);

        // and the stroke to use,
        style.drawStroke().apply(graphics);

        // then draw the shape
        graphics.draw(shape);

        // and return the stroked shape outline to the caller.
        return style.shape(shape);
    }

    private FontMetrics fontMetrics(final Style style)
    {
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
