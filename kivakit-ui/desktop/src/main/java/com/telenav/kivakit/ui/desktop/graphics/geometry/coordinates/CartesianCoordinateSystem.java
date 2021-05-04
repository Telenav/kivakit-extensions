////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.ui.desktop.graphics.geometry.coordinates;

import com.telenav.kivakit.ui.desktop.graphics.geometry.CoordinateSystem;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Point;
import com.telenav.kivakit.ui.desktop.graphics.geometry.objects.Size;

import java.util.Objects;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * Maps to and from a given bounded coordinate system using simple rectangular interpolation.
 *
 * @author jonathanl (shibo)
 */
public class CartesianCoordinateSystem implements CoordinateSystem
{
    /**
     * @return A {@link CartesianCoordinateSystem} with the given origin and size
     */
    public static CartesianCoordinateSystem createCoordinateSystem(final double x, final double y, final Size size)
    {
        return new CartesianCoordinateSystem(x, y, size);
    }

    /**
     * @return A coordinate system with the given origin, but with no size and therefore no scaling
     */
    public static CartesianCoordinateSystem createCoordinateSystem(final double x, final double y)
    {
        return new CartesianCoordinateSystem(x, y);
    }

    /**
     * @return A {@link CartesianCoordinateSystem} with an origin of 0, 0 and no bounds
     */
    public static CartesianCoordinateSystem pixelCoordinateSystem()
    {
        return createCoordinateSystem(0, 0);
    }

    /** The origin of this coordinate system */
    private final Point origin;

    /** The size of the coordinate system, or null if it is unbounded */
    private Size size;

    protected CartesianCoordinateSystem(final double x, final double y, final Size size)
    {
        this(x, y);
        this.size = size;
    }

    protected CartesianCoordinateSystem(final double x, final double y)
    {
        origin = Point.at(this, x, y);
    }

    protected CartesianCoordinateSystem(final Point origin)
    {
        this.origin = origin;
    }

    protected CartesianCoordinateSystem(final Point origin, final Size size)
    {
        this.origin = origin;
        this.size = size;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof CartesianCoordinateSystem)
        {
            final CartesianCoordinateSystem that = (CartesianCoordinateSystem) object;
            return origin.equals(that.origin) && Objects.equals(size, that.size);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(origin, size);
    }

    /**
     * @return True if this coordinate system is bounded
     */
    @Override
    public boolean isBounded()
    {
        return size() != null;
    }

    @Override
    public Point origin()
    {
        return origin;
    }

    @Override
    public Size size()
    {
        return size;
    }

    @Override
    public Point to(final CoordinateSystem that, final Point coordinate)
    {
        ensureNotNull(that);

        // If we are already in the same coordinate system,
        if (this == that)
        {
            // the coordinate does not need to be converted.
            return coordinate;
        }
        else
        {
            // If this coordinate system has a finite size,
            if (size != null)
            {
                // normalize the given coordinate to the unit interval from 0 to 1 in the this coordinate system,
                final var xUnit = (coordinate.x() - origin.x()) / size.widthInUnits();
                final var yUnit = (coordinate.y() - origin.y()) / size.heightInUnits();

                // then return the x, y coordinate in the given coordinate system scaled to the same relative position.
                final var x = that.origin().x() + xUnit * that.size().widthInUnits();
                final var y = that.origin().y() + yUnit * that.size().heightInUnits();

                return at(x, y);
            }
            else
            {
                // otherwise, get the offset of the given coordinate in this coordinate system,
                final var dx = coordinate.x() - origin.x();
                final var dy = coordinate.y() - origin.y();

                // and return the origin of the target coordinate system plus the offset.
                final var x = that.origin().x() + dx;
                final var y = that.origin().y() + dy;

                return at(x, y);
            }
        }
    }

    public CartesianCoordinateSystem withOrigin(final double x, final double y)
    {
        return new CartesianCoordinateSystem(Point.at(this, x, y), size);
    }

    public CartesianCoordinateSystem withSize(final double dx, final double dy)
    {
        return new CartesianCoordinateSystem(origin, Size.size(this, dx, dy));
    }
}
