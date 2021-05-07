package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Composite;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Path2D;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

/**
 * @author jonathanl (shibo)
 */
public interface DrawingSurface extends CoordinateSystem
{
    default Shape drawBox(final Style style, final DrawingRectangle area)
    {
        return drawBox(style, area.at(), area.size());
    }

    default Shape drawBox(final Style style,
                          final DrawingPoint at,
                          final DrawingWidth width,
                          final DrawingHeight height)
    {
        ensure(at.inSameCoordinateSystem(width));
        ensure(at.inSameCoordinateSystem(height));

        return drawBox(style, at, DrawingSize.size(at.coordinates(), width.units(), height.units()));
    }

    /**
     * Draws a rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawBox(Style style, DrawingPoint at, DrawingSize size);

    /**
     * Draws a circle at the given point with the given radius in the given style
     */
    Shape drawDot(Style style, DrawingPoint at, DrawingLength radius);

    /**
     * Draws the given image at the given point on this drawing surface
     */
    Shape drawImage(DrawingPoint at, Image image, Composite composite);

    /**
     * Draws the given image at the given point on this drawing surface
     */
    default Shape drawImage(final DrawingPoint at, final Image image)
    {
        return drawImage(at, image, null);
    }

    /**
     * Draws a line from one point to another in the given style
     */
    Shape drawLine(Style style, DrawingPoint from, DrawingPoint to);

    /**
     * Draws a path in the given style
     */
    Shape drawPath(Style style, Path2D path);

    /**
     * Draws a rounded rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawRoundedBox(Style style,
                         DrawingPoint at,
                         DrawingSize size,
                         DrawingLength cornerWidth,
                         DrawingLength cornerHeight);

    /**
     * Draws the given text, in the given style, at the given {@link DrawingPoint}. Because determining text shape is
     * expensive, this method has no return value. To get the shape of drawn text, call {@link #textShape(Style,
     * DrawingPoint, String)}
     */
    void drawText(Style style, DrawingPoint at, String text);

    default DrawingRectangle drawingArea()
    {
        return rectangle(x(), y(), width(), height());
    }

    /**
     * @return The shape of the given text drawn im the given style at the given location
     */
    Shape textShape(final Style style, final DrawingPoint at, final String text);

    /**
     * @return The size of the given text in the given style when rendered on this surface
     */
    DrawingSize textSize(final Style style, final String text);
}
