package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import java.awt.Shape;

/**
 * An object with a {@link #style()} that can be drawn on a {@link DrawingSurface} with {@link #draw(DrawingSurface)}.
 * The object has a location that can be queried with {@link #withLocation()}. A copy of the object can be created with
 * {@link #copy()}. A copy at a new location can be created with {@link #withLocation(DrawingPoint)} and a scaled copy
 * with {@link #scaledBy(double)} or {@link #scaledBy(Percent)}. Once the drawable has been drawn, its {@link Shape} can
 * be retrieved with {@link #shape()}.
 *
 * @author jonathanl (shibo)
 */
public interface Drawable
{
    /**
     * @return A copy of this drawable
     */
    Drawable copy();

    /**
     * Draws this on the given surface
     *
     * @return The {@link Shape} that was drawn
     */
    Shape draw(DrawingSurface surface);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    Drawable scaledBy(double scaleFactor);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    default Drawable scaledBy(Percent scaleFactor)
    {
        return copy().scaledBy(scaleFactor.asZeroToOne());
    }

    /**
     * @return The shape of this drawable (only once it has been drawn)
     */
    Shape shape();

    /**
     * @return The style of this drawable
     */
    @KivaKitIncludeProperty
    Style style();

    Drawable withColors(Style style);

    Drawable withDrawColor(Color color);

    Drawable withDrawStroke(Stroke stroke);

    Drawable withDrawStrokeWidth(DrawingWidth width);

    Drawable withFillColor(Color color);

    Drawable withFillStroke(Stroke stroke);

    Drawable withFillStrokeWidth(DrawingWidth width);

    /**
     * @return The location of the drawable
     */
    @KivaKitIncludeProperty
    DrawingPoint withLocation();

    /**
     * @return A copy of this drawable at the given new location
     */
    Drawable withLocation(DrawingPoint at);

    Drawable withStyle(Style style);

    Drawable withTextColor(Color color);
}
