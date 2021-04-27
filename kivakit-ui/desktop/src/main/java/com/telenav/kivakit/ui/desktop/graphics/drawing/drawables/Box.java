package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.BaseDrawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingDistance;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSize;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;

/**
 * @author jonathanl (shibo)
 */
public class Box extends BaseDrawable
{
    public static Box box()
    {
        return box((Style) null);
    }

    public static Box box(final Style style)
    {
        return new Box(style);
    }

    private CoordinateSize size;

    protected Box(final Box that)
    {
        super(that);
        size = that.size;
    }

    protected Box(final Style style)
    {
        super(style);
    }

    @Override
    public Box at(final Coordinate at)
    {
        return (Box) super.at(at);
    }

    @Override
    public Box copy()
    {
        return new Box(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        return shape(surface.drawBox(style(), at().inDrawingUnits(), size.onDrawingSurface()));
    }

    @Override
    public Drawable scaled(final double scaleFactor)
    {
        final var copy = copy();
        copy.size = size.scaled(scaleFactor);
        return copy;
    }

    @Override
    public Box withColors(final Style style)
    {
        return (Box) super.withColors(style);
    }

    @Override
    public Box withDrawColor(final Color color)
    {
        return (Box) super.withDrawColor(color);
    }

    @Override
    public Box withDrawStroke(final Stroke stroke)
    {
        return (Box) super.withDrawStroke(stroke);
    }

    @Override
    public Box withDrawStrokeWidth(final DrawingDistance width)
    {
        return (Box) super.withDrawStrokeWidth(width);
    }

    @Override
    public Box withFillColor(final Color color)
    {
        return (Box) super.withFillColor(color);
    }

    @Override
    public Box withFillStroke(final Stroke stroke)
    {
        return (Box) super.withFillStroke(stroke);
    }

    @Override
    public Box withFillStrokeWidth(final DrawingDistance width)
    {
        return (Box) super.withFillStrokeWidth(width);
    }

    public Box withSize(final CoordinateSize size)
    {
        final var copy = copy();
        copy.size = size;
        return copy;
    }

    @Override
    public Box withStyle(final Style style)
    {
        return (Box) super.withStyle(style);
    }

    @Override
    public Box withTextColor(final Color color)
    {
        return (Box) super.withTextColor(color);
    }
}
