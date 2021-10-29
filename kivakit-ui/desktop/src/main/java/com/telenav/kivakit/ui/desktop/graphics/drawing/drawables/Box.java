package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

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

    public static Box box(Style style)
    {
        return new Box(style);
    }

    private DrawingSize size;

    private DrawingWidth cornerWidth;

    private DrawingHeight cornerHeight;

    protected Box(Box that)
    {
        super(that);
        size = that.size;
        cornerWidth = that.cornerWidth;
        cornerHeight = that.cornerHeight;
    }

    protected Box(Style style)
    {
        super(style);
    }

    @Override
    public Box copy()
    {
        return new Box(this);
    }

    @Override
    public Shape draw(DrawingSurface surface)
    {
        return draw(surface, size());
    }

    @Override
    public Drawable scaledBy(double scaleFactor)
    {
        var copy = copy();
        copy.size = size.scaledBy(scaleFactor);
        return copy;
    }

    public DrawingSize size()
    {
        return size;
    }

    public Box withArea(DrawingRectangle rectangle)
    {
        return withSize(rectangle.size()).withLocation(rectangle.at());
    }

    @Override
    public Box withColors(Style style)
    {
        return (Box) super.withColors(style);
    }

    @Override
    public Box withDrawColor(Color color)
    {
        return (Box) super.withDrawColor(color);
    }

    @Override
    public Box withDrawStroke(Stroke stroke)
    {
        return (Box) super.withDrawStroke(stroke);
    }

    @Override
    public Box withDrawStrokeWidth(DrawingWidth width)
    {
        return (Box) super.withDrawStrokeWidth(width);
    }

    @Override
    public Box withFillColor(Color color)
    {
        return (Box) super.withFillColor(color);
    }

    @Override
    public Box withFillStroke(Stroke stroke)
    {
        return (Box) super.withFillStroke(stroke);
    }

    @Override
    public Box withFillStrokeWidth(DrawingWidth width)
    {
        return (Box) super.withFillStrokeWidth(width);
    }

    @Override
    public Box withLocation(DrawingPoint at)
    {
        return (Box) super.withLocation(at);
    }

    public Box withRoundedCorners(DrawingLength corner)
    {
        return withRoundedCorners(corner.asWidth(), corner.asHeight());
    }

    public Box withRoundedCorners(DrawingWidth cornerWidth, DrawingHeight cornerHeight)
    {
        var copy = copy();
        copy.cornerWidth = cornerWidth;
        copy.cornerHeight = cornerHeight;
        return copy;
    }

    public Box withSize(DrawingSize size)
    {
        var copy = copy();
        copy.size = size;
        return copy;
    }

    @Override
    public Box withStyle(Style style)
    {
        return (Box) super.withStyle(style);
    }

    @Override
    public Box withTextColor(Color color)
    {
        return (Box) super.withTextColor(color);
    }

    protected Shape draw(DrawingSurface surface, DrawingSize size)
    {
        if (cornerWidth != null && cornerHeight != null)
        {
            return shape(surface.drawRoundedBox(style(), withLocation(), size, cornerWidth, cornerHeight));
        }
        else
        {
            return shape(surface.drawBox(style(), withLocation(), size));
        }
    }
}
