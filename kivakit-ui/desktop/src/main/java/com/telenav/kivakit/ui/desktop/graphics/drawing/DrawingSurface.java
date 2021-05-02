package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateHeight;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateRectangle;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateWidth;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * @author jonathanl (shibo)
 */
public interface DrawingSurface extends CoordinateSystem
{
    default Shape drawBox(final Style style, final CoordinateRectangle area)
    {
        return drawBox(style, area.at(), area.size());
    }

    default Shape drawBox(final Style style,
                          final Coordinate at,
                          final CoordinateWidth width,
                          final CoordinateHeight height)
    {
        return drawBox(style, at, CoordinateSize.size(at.coordinateSystem(), width.units(), height.units()));
    }

    /**
     * Draws a rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawBox(Style style, Coordinate at, CoordinateSize size);

    /**
     * Draws a circle at the given point with the given radius in the given style
     */
    Shape drawCircle(Style style, Coordinate at, CoordinateDistance radius);

    /**
     * Draws a line from one point to another in the given style
     */
    Shape drawLine(Style style, Coordinate from, Coordinate to);

    /**
     * Draws a path in the given style
     */
    Shape drawPath(Style style, Path2D path);

    /**
     * Draws a rounded rectangle at the given point, with the given width and height, in the given style.
     */
    Shape drawRoundedBox(Style style,
                         Coordinate at,
                         CoordinateSize size,
                         CoordinateDistance cornerWidth,
                         CoordinateDistance cornerHeight);

    /**
     * Draws the given text, in the given style, at the given point (relative to the upper left)
     */
    Shape drawText(Style style, Coordinate at, String text);

    /**
     * @return The size of the given text in the given style when rendered on this surface
     */
    CoordinateSize size(final Style style, final String text);
}
