package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A text string that is {@link Drawable} in a given {@link Style}
 *
 * @author jonathanl (shibo)
 */
public class Text extends BaseDrawable
{
    public static Text text()
    {
        return text((Style) null);
    }

    public static Text text(Style style)
    {
        return new Text(style, null);
    }

    public static Text text(Style style, String text)
    {
        return new Text(style, text);
    }

    private String text;

    protected Text(Style style, String text)
    {
        super(style);
        this.text = text;
    }

    protected Text(Text that)
    {
        super(that);

        text = that.text;
    }

    @Override
    public Text copy()
    {
        return new Text(this);
    }

    @Override
    public Shape draw(DrawingSurface surface)
    {
        surface.drawText(style(), withLocation(), text);
        return null;
    }

    @Override
    public Text scaledBy(double scaleFactor)
    {
        return unsupported();
    }

    @Override
    public Text withColors(Style style)
    {
        return (Text) super.withColors(style);
    }

    @Override
    public Text withDrawColor(Color color)
    {
        return (Text) super.withDrawColor(color);
    }

    @Override
    public Text withDrawStroke(Stroke stroke)
    {
        return (Text) super.withDrawStroke(stroke);
    }

    @Override
    public Text withDrawStrokeWidth(DrawingWidth width)
    {
        return (Text) super.withDrawStrokeWidth(width);
    }

    @Override
    public Text withFillColor(Color color)
    {
        return (Text) super.withFillColor(color);
    }

    @Override
    public Text withFillStroke(Stroke stroke)
    {
        return (Text) super.withFillStroke(stroke);
    }

    @Override
    public Text withFillStrokeWidth(DrawingWidth width)
    {
        return (Text) super.withFillStrokeWidth(width);
    }

    @Override
    public Text withLocation(DrawingPoint at)
    {
        return (Text) super.withLocation(at);
    }

    @Override
    public Text withStyle(Style style)
    {
        return (Text) super.withStyle(style);
    }

    public Text withText(String text)
    {
        var copy = copy();
        copy.text = text;
        return copy;
    }

    @Override
    public Text withTextColor(Color color)
    {
        return (Text) super.withTextColor(color);
    }
}
