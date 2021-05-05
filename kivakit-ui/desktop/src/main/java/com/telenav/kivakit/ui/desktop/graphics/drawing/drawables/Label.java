package com.telenav.kivakit.ui.desktop.graphics.drawing.drawables;

import com.telenav.kivakit.ui.desktop.graphics.drawing.Drawable;
import com.telenav.kivakit.ui.desktop.graphics.drawing.DrawingSurface;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Shape;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A {@link Drawable} text label with an underlying {@link Box}
 *
 * @author jonathanl (shibo)
 */
public class Label extends Box
{
    public static Label label()
    {
        return label((Style) null);
    }

    public static Label label(final Style style, final String text)
    {
        return new Label(style, text);
    }

    public static Label label(final Style style)
    {
        return new Label(style, null);
    }

    private String text;

    private int margin = 10;

    protected Label(final Style style, final String text)
    {
        super(style);
        this.text = text;
    }

    protected Label(final Label that)
    {
        super(that);
        text = that.text;
        margin = that.margin;
    }

    @Override
    public Label at(final DrawingPoint at)
    {
        return (Label) super.at(at);
    }

    @Override
    public Label copy()
    {
        return new Label(this);
    }

    @Override
    public Shape draw(final DrawingSurface surface)
    {
        final var size = surface
                .textSize(style(), text)
                .plus(margin * 2, margin * 2);

        final var shape = super.draw(surface, size);

        surface.drawText(style(), at().plus(margin, margin), text);
        return shape;
    }

    @Override
    public Label scaledBy(final double scaleFactor)
    {
        return unsupported();
    }

    @Override
    public Label withColors(final Style style)
    {
        return (Label) super.withColors(style);
    }

    @Override
    public Label withDrawColor(final Color color)
    {
        return (Label) super.withDrawColor(color);
    }

    @Override
    public Label withDrawStroke(final Stroke stroke)
    {
        return (Label) super.withDrawStroke(stroke);
    }

    @Override
    public Label withDrawStrokeWidth(final DrawingWidth width)
    {
        return (Label) super.withDrawStrokeWidth(width);
    }

    @Override
    public Label withFillColor(final Color color)
    {
        return (Label) super.withFillColor(color);
    }

    @Override
    public Label withFillStroke(final Stroke stroke)
    {
        return (Label) super.withFillStroke(stroke);
    }

    @Override
    public Label withFillStrokeWidth(final DrawingWidth width)
    {
        return (Label) super.withFillStrokeWidth(width);
    }

    public Label withMargin(final int margin)
    {
        final var copy = copy();
        copy.margin = margin;
        return copy;
    }

    @Override
    public Label withRoundedCorners(final DrawingLength corner)
    {
        return (Label) super.withRoundedCorners(corner);
    }

    @Override
    public Label withRoundedCorners(final DrawingWidth cornerWidth, final DrawingHeight cornerHeight)
    {
        return (Label) super.withRoundedCorners(cornerWidth, cornerHeight);
    }

    @Override
    public Label withStyle(final Style style)
    {
        return (Label) super.withStyle(style);
    }

    public Label withText(final String text)
    {
        final var copy = copy();
        copy.text = text;
        return copy;
    }

    @Override
    public Label withTextColor(final Color color)
    {
        return (Label) super.withTextColor(color);
    }
}
