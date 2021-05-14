/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */

package com.telenav.kivakit.ui.desktop.graphics.drawing.geometry;

import com.telenav.kivakit.ui.desktop.graphics.drawing.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.drawing.Coordinated;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * An object in a {@link CoordinateSystem}. The <i>normalized()</i> methods convert geometric objects from their
 * coordinate system to the coordinate system for this object.
 *
 * @author jonathanl (shibo)
 */
public abstract class DrawingObject implements Coordinated
{
    /** The coordinate system for this object */
    private CoordinateSystem coordinates;

    protected DrawingObject(final Coordinated coordinates)
    {
        ensureNotNull(coordinates);

        this.coordinates = coordinates.coordinates();
    }

    public void coordinates(final Coordinated coordinates)
    {
        this.coordinates = coordinates.coordinates();
    }

    @Override
    public CoordinateSystem coordinates()
    {
        return coordinates;
    }

    /**
     * @return True If this object and the given object are in the same coordinate system
     */
    public boolean inSameCoordinateSystem(final DrawingObject that)
    {
        return coordinates().equals(that.coordinates());
    }

    public DrawingPoint point(final double x, final double y)
    {
        return DrawingPoint.point(this, x, y);
    }

    public DrawingRectangle rectangle(final double x, final double y, final double dx, final double dy)
    {
        return DrawingRectangle.rectangle(this, x, y, dx, dy);
    }

    public DrawingRectangle toCoordinates(final DrawingRectangle that)
    {
        return that.toCoordinates(this);
    }

    public DrawingWidth toCoordinates(final DrawingWidth that)
    {
        return that.toCoordinates(this);
    }

    public DrawingHeight toCoordinates(final DrawingHeight that)
    {
        return that.toCoordinates(this);
    }

    public DrawingPoint toCoordinates(final DrawingPoint that)
    {
        return that.toCoordinates(this);
    }

    public DrawingSize toCoordinates(final DrawingSize that)
    {
        return that.toCoordinates(this);
    }

    public DrawingLength toCoordinates(final DrawingLength that)
    {
        return that.toCoordinates(this);
    }

    @Override
    public String toString()
    {
        return coordinates().name();
    }

    protected DrawingHeight height(final double units)
    {
        return DrawingHeight.height(coordinates(), units);
    }

    protected DrawingLength length(final double units)
    {
        return DrawingLength.length(coordinates(), units);
    }

    protected DrawingSize size(final double dx, final double dy)
    {
        return DrawingSize.size(coordinates(), dx, dy);
    }

    protected DrawingWidth width(final double units)
    {
        return DrawingWidth.width(coordinates(), units);
    }
}
