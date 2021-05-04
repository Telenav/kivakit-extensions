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

package com.telenav.kivakit.ui.desktop.graphics.geometry;

import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Height;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Length;
import com.telenav.kivakit.ui.desktop.graphics.geometry.measurements.Width;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Rectangle;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * An object in a {@link CoordinateSystem}. The <i>normalized()</i> methods convert geometric objects from their
 * coordinate system to the coordinate system for this object.
 *
 * @author jonathanl (shibo)
 */
public abstract class Coordinated
{
    /** The coordinate system for this object */
    private final CoordinateSystem coordinateSystem;

    protected Coordinated(final CoordinateSystem coordinateSystem)
    {
        ensureNotNull(coordinateSystem);

        this.coordinateSystem = coordinateSystem;
    }

    public CoordinateSystem coordinateSystem()
    {
        return coordinateSystem;
    }

    public Size normalized(final Size that)
    {
        return that.to(coordinateSystem());
    }

    public Length normalized(final Length that)
    {
        return that.to(coordinateSystem());
    }

    public Rectangle normalized(final Rectangle that)
    {
        return that.to(coordinateSystem());
    }

    public Width normalized(final Width that)
    {
        return that.to(coordinateSystem());
    }

    public Height normalized(final Height that)
    {
        return that.to(coordinateSystem());
    }

    public Point normalized(final Point that)
    {
        return that.to(coordinateSystem());
    }

    /**
     * @return True If this object and the given object are in the same coordinate system
     */
    public boolean sameCoordinateSystem(final Coordinated that)
    {
        return coordinateSystem().equals(that.coordinateSystem());
    }
}
