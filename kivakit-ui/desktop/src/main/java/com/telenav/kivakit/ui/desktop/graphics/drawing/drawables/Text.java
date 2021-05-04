package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

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

    public static Text text(final Style style)
    {
        return new Text(style, null);
    }

    public static Text text(final Style style, final String text)
    {
        return new Text(style, text);
    }

    private String text;

    protected Text(final Style style, final String text)
    {
        super(style);
        this.text = text;
    }

    protected Text(final Text that)
    {
        super(that);

        text = that.text;
    }

    @Override
    public Text at(final Point at)
    {
        return (Text) super.at(at);
    }

    @Override
    public Text copy()
    {
        return new Text(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        surface.drawText(style(), at(), text);
        return null;
    }

    @Override
    public Text scaledBy(final double scaleFactor)
    {
        return unsupported();
    }

    @Override
    public Text withColors(final Style style)
    {
        return (Text) super.withColors(style);
    }

    @Override
    public Text withDrawColor(final Color color)
    {
        return (Text) super.withDrawColor(color);
    }

    @Override
    public Text withDrawStroke(final Stroke stroke)
    {
        return (Text) super.withDrawStroke(stroke);
    }

    @Override
    public Text withDrawStrokeWidth(final Width width)
    {
        return (Text) super.withDrawStrokeWidth(width);
    }

    @Override
    public Text withFillColor(final Color color)
    {
        return (Text) super.withFillColor(color);
    }

    @Override
    public Text withFillStroke(final Stroke stroke)
    {
        return (Text) super.withFillStroke(stroke);
    }

    @Override
    public Text withFillStrokeWidth(final Width width)
    {
        return (Text) super.withFillStrokeWidth(width);
    }

    @Override
    public Text withStyle(final Style style)
    {
        return (Text) super.withStyle(style);
    }

    public Text withText(final String text)
    {
        final var copy = copy();
        copy.text = text;
        return copy;
    }

    @Override
    public Text withTextColor(final Color color)
    {
        return (Text) super.withTextColor(color);
    }
}
