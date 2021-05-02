package com.telenav.kivakit.ui.desktop.graphics.drawing;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.ui.desktop.graphics.geometry.Coordinate;
import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateDistance;
import com.telenav.kivakit.ui.desktop.graphics.style.Color;
import com.telenav.kivakit.ui.desktop.graphics.style.Stroke;
import com.telenav.kivakit.ui.desktop.graphics.style.Style;

import java.awt.Shape;

/**
 * @author jonathanl (shibo)
 */
public interface Drawable
{
    /**
     * @return The location of the drawable
     */
    @KivaKitIncludeProperty
    Coordinate at();

    /**
     * @return This drawable at the given new location
     */
    Drawable at(Coordinate at);

    /**
     * @return A copy of this drawable
     */
    Drawable copy();

    /**
     * Draws this on the given surface
     *
     * @return The {@link Shape} that was drawn
     */
    Shape draw(final DrawingSurface surface);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    Drawable scaledBy(final double scaleFactor);

    /**
     * @return This drawable scaled by the given scaling factor
     */
    default Drawable scaledBy(final Percent scaleFactor)
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

    Drawable withColors(final Style style);

    Drawable withDrawColor(final Color color);

    Drawable withDrawStroke(final Stroke stroke);

    Drawable withDrawStrokeWidth(final CoordinateDistance width);

    Drawable withFillColor(final Color color);

    Drawable withFillStroke(final Stroke stroke);

    Drawable withFillStrokeWidth(final CoordinateDistance width);

    Drawable withStyle(final Style style);

    Drawable withTextColor(final Color color);
}
