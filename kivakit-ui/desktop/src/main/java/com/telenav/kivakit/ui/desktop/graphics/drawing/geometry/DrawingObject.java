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
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingHeight;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingLength;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingPoint;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingRectangle;
import com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.objects.DrawingSize;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * An object in a {@link CoordinateSystem}. The <i>normalized()</i> methods convert geometric objects from their
 * coordinate system to the coordinate system for this object.
 *
 * @author jonathanl (shibo)
 */
public abstract class DrawingObject
{
    /** The coordinate system for this object */
    private CoordinateSystem coordinateSystem;

    protected DrawingObject(final CoordinateSystem coordinateSystem)
    {
        ensureNotNull(coordinateSystem);

        this.coordinateSystem = coordinateSystem;
    }

    public CoordinateSystem coordinateSystem()
    {
        return coordinateSystem;
    }

    public void coordinateSystem(final CoordinateSystem coordinateSystem)
    {
        this.coordinateSystem = coordinateSystem;
    }

    public DrawingSize normalized(final DrawingSize that)
    {
        return that.to(coordinateSystem());
    }

    public DrawingLength normalized(final DrawingLength that)
    {
        return that.to(coordinateSystem());
    }

    public DrawingRectangle normalized(final DrawingRectangle that)
    {
        return that.to(coordinateSystem());
    }

    public DrawingWidth normalized(final DrawingWidth that)
    {
        return that.to(coordinateSystem());
    }

    public DrawingHeight normalized(final DrawingHeight that)
    {
        return that.to(coordinateSystem());
    }

    public DrawingPoint normalized(final DrawingPoint that)
    {
        return that.to(coordinateSystem());
    }

    /**
     * @return True If this object and the given object are in the same coordinate system
     */
    public boolean sameCoordinateSystem(final DrawingObject that)
    {
        return coordinateSystem().equals(that.coordinateSystem());
    }
}
