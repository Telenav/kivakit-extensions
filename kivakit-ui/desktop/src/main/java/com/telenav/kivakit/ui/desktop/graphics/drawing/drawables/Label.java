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

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

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

    public static Label label(Style style, String text)
    {
        return new Label(style, text);
    }

    public static Label label(Style style)
    {
        return new Label(style, null);
    }

    private String text;

    private int margin = 10;

    protected Label(Style style, String text)
    {
        super(style);
        this.text = text;
    }

    protected Label(Label that)
    {
        super(that);
        text = that.text;
        margin = that.margin;
    }

    @Override
    public Label copy()
    {
        return new Label(this);
    }

    @Override
    public Shape draw(DrawingSurface surface)
    {
        var size = surface
                .textSize(style(), text)
                .plus(margin * 2, margin * 2);

        var shape = super.draw(surface, size);

        surface.drawText(style(), withLocation().plus(margin, margin), text);
        return shape;
    }

    @Override
    public Label scaledBy(double scaleFactor)
    {
        return unsupported();
    }

    @Override
    public Label withColors(Style style)
    {
        return (Label) super.withColors(style);
    }

    @Override
    public Label withDrawColor(Color color)
    {
        return (Label) super.withDrawColor(color);
    }

    @Override
    public Label withDrawStroke(Stroke stroke)
    {
        return (Label) super.withDrawStroke(stroke);
    }

    @Override
    public Label withDrawStrokeWidth(DrawingWidth width)
    {
        return (Label) super.withDrawStrokeWidth(width);
    }

    @Override
    public Label withFillColor(Color color)
    {
        return (Label) super.withFillColor(color);
    }

    @Override
    public Label withFillStroke(Stroke stroke)
    {
        return (Label) super.withFillStroke(stroke);
    }

    @Override
    public Label withFillStrokeWidth(DrawingWidth width)
    {
        return (Label) super.withFillStrokeWidth(width);
    }

    @Override
    public Label withLocation(DrawingPoint at)
    {
        return (Label) super.withLocation(at);
    }

    public Label withMargin(int margin)
    {
        var copy = copy();
        copy.margin = margin;
        return copy;
    }

    @Override
    public Label withRoundedCorners(DrawingLength corner)
    {
        return (Label) super.withRoundedCorners(corner);
    }

    @Override
    public Label withRoundedCorners(DrawingWidth cornerWidth, DrawingHeight cornerHeight)
    {
        return (Label) super.withRoundedCorners(cornerWidth, cornerHeight);
    }

    @Override
    public Label withStyle(Style style)
    {
        return (Label) super.withStyle(style);
    }

    public Label withText(String text)
    {
        var copy = copy();
        copy.text = text;
        return copy;
    }

    @Override
    public Label withTextColor(Color color)
    {
        return (Label) super.withTextColor(color);
    }
}
