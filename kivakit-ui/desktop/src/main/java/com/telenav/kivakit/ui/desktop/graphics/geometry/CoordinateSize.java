package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;

import static com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem.drawingSurface;

/**
 * @author jonathanl (shibo)
 */
public class CoordinateSize extends Coordinated
{
    public static CoordinateSize size(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        return new CoordinateSize(coordinateSystem, width, height);
    }

    public static CoordinateSize size(final double width, final double height)
    {
        return size(drawingSurface(), width, height);
    }

    private final double width;

    private final double height;

    public CoordinateSize(final CoordinateSystem coordinateSystem, final double width, final double height)
    {
        super(coordinateSystem);
        this.width = width;
        this.height = height;
    }

    public Coordinate asCoordinate()
    {
        return Coordinate.at(coordinateSystem(), width, height);
    }

    public CoordinateRectangle asRectangle()
    {
        return CoordinateRectangle.rectangle(coordinateSystem(), 0, 0, width, height);
    }

    public CoordinateRectangle centeredIn(final CoordinateRectangle rectangle)
    {
        return rectangle
                .topLeft()
                .plus(rectangle.size().minus(this).times(0.5))
                .rectangle(this);
    }

    public CoordinateHeight height()
    {
        return CoordinateHeight.height(coordinateSystem(), height);
    }

    public double heightInUnits()
    {
        return height;
    }

    public CoordinateSize minus(final CoordinateSize that)
    {
        return withSize(widthInUnits() - that.widthInUnits(), heightInUnits() - that.heightInUnits());
    }

    public CoordinateSize plus(final double width, final double height)
    {
        return withSize(this.width + width, this.height + height);
    }

    public CoordinateSize plus(final CoordinateSize that)
    {
        return withSize(width + that.width, height + that.height);
    }

    public CoordinateSize scaledBy(final double scaleFactor)
    {
        return withSize(width * scaleFactor, height * scaleFactor);
    }

    public CoordinateSize scaledBy(final Percent percent)
    {
        return withSize(percent.scale(width), percent.scale(height));
    }

    public CoordinateSize times(final double scaleFactor)
    {
        return withSize(widthInUnits() * scaleFactor, heightInUnits() * scaleFactor);
    }

    public CoordinateSize to(final CoordinateSystem system)
    {
        return system.to(system, this);
    }

    @Override
    public String toString()
    {
        return width + ", " + height;
    }

    public CoordinateWidth width()
    {
        return CoordinateWidth.width(coordinateSystem(), width);
    }

    public double widthInUnits()
    {
        return width;
    }

    public CoordinateSize withSize(final double width, final double height)
    {
        return size(coordinateSystem(), width, height);
    }
}
