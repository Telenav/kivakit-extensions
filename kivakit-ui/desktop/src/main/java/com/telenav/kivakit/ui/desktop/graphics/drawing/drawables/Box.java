package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Height;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Length;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;

/**
 * A {@link Drawable} rectangle that can have rounded corners
 *
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

    private Size size;

    private Width cornerWidth;

    private Height cornerHeight;

    protected Box(final Box that)
    {
        super(that);
        size = that.size;
        cornerWidth = that.cornerWidth;
        cornerHeight = that.cornerHeight;
    }

    protected Box(final Style style)
    {
        super(style);
    }

    @Override
    public Box at(final Point at)
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
        return draw(surface, size());
    }

    @Override
    public Drawable scaledBy(final double scaleFactor)
    {
        final var copy = copy();
        copy.size = size.scaledBy(scaleFactor);
        return copy;
    }

    public Size size()
    {
        return size;
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
    public Box withDrawStrokeWidth(final Width width)
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
    public Box withFillStrokeWidth(final Width width)
    {
        return (Box) super.withFillStrokeWidth(width);
    }

    public Box withRoundedCorners(final Length corner)
    {
        return withRoundedCorners(corner.asWidth(), corner.asHeight());
    }

    public Box withRoundedCorners(final Width cornerWidth, final Height cornerHeight)
    {
        final var copy = copy();
        copy.cornerWidth = cornerWidth;
        copy.cornerHeight = cornerHeight;
        return copy;
    }

    public Box withSize(final Size size)
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

    protected Shape draw(final DrawingSurface surface, final Size size)
    {
        if (cornerWidth != null && cornerHeight != null)
        {
            return shape(surface.drawRoundedBox(style(), at(), size, cornerWidth, cornerHeight));
        }
        else
        {
            return shape(surface.drawBox(style(), at(), size));
        }
    }
}
