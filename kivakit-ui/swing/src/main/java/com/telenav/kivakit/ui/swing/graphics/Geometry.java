package com.telenav.kivakit.ui.swing.graphics;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author jonathanl (shibo)
 */
public class Geometry
{
    public static Point2D.Double to2d(final Point point)
    {
        return new Point2D.Double(point.getX(), point.getY());
    }

    public static Point2D.Double to2d(final Point2D point)
    {
        return new Point2D.Double(point.getX(), point.getY());
    }
}
