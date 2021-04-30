package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;
import java.awt.geom.Path2D;

/**
 * @author jonathanl (shibo)
 */
public interface DrawingSurface extends CoordinateSystem
{
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
     * Draws the given text, in the given style, at the given point (relative to the upper left)
     */
    Shape drawText(Style style, Coordinate at, String text);

    /**
     * @return The size of the given text in the given style when rendered on this surface
     */
    CoordinateSize size(final Style style, final String text);
}
