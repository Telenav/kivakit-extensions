package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Height;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Length;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Rectangle;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * @author jonathanl (shibo)
 */
public interface DrawingSurface extends CoordinateSystem
{
    default Shape drawBox(final Style style, final Rectangle area)
    {
        return drawBox(style, area.at(), area.size());
    }

    default Shape drawBox(final Style style,
                          final Point at,
                          final Width width,
                          final Height height)
    {
        return drawBox(style, at, Size.size(at.coordinateSystem(), width.units(), height.units()));
    }

    /**
     * Draws a rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawBox(Style style, Point at, Size size);

    /**
     * Draws a circle at the given point with the given radius in the given style
     */
    Shape drawCircle(Style style, Point at, Length radius);

    /**
     * Draws a line from one point to another in the given style
     */
    Shape drawLine(Style style, Point from, Point to);

    /**
     * Draws a path in the given style
     */
    Shape drawPath(Style style, Path2D path);

    /**
     * Draws a rounded rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawRoundedBox(Style style,
                         Point at,
                         Size size,
                         Length cornerWidth,
                         Length cornerHeight);

    /**
     * Draws the given text, in the given style, at the given {@link Point}. Because determining text shape is
     * expensive, this method has no return value. To get the shape of drawn text, call {@link #textShape(Style, Point,
     * String)}
     */
    void drawText(Style style, Point at, String text);

    /**
     * @return The shape of the given text drawn im the given style at the given location
     */
    Shape textShape(final Style style, final Point at, final String text);

    /**
     * @return The size of the given text in the given style when rendered on this surface
     */
    Size textSize(final Style style, final String text);
}
