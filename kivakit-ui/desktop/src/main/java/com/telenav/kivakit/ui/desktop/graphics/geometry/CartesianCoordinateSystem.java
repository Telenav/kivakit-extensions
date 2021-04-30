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

/**
 * Maps to and from a given bounded coordinate system using simple rectangular interpolation.
 *
 * @author jonathanl (shibo)
 */
public class CartesianCoordinateSystem implements CoordinateSystem
{
    private Coordinate origin;

    private CoordinateSize size;

    public CartesianCoordinateSystem()
    {
    }

    protected CartesianCoordinateSystem(final Coordinate origin, final CoordinateSize size)
    {
        this.origin = origin;
        this.size = size;
    }

    @Override
    public Coordinate origin()
    {
        return origin;
    }

    @Override
    public CoordinateSize size()
    {
        return size;
    }

    @Override
    public Coordinate to(final CoordinateSystem that, final Coordinate coordinate)
    {
        if (this == that)
        {
            return coordinate;
        }
        else
        {
            // Normalize the given coordinate to the unit interval from 0 to 1 in the this coordinate system,
            final var xUnit = (coordinate.x() - origin.x()) / size.widthInUnits();
            final var yUnit = (coordinate.y() - origin.y()) / size.heightInUnits();

            // then find the x, y coordinate in the given coordinate system at the same relative position.
            final var x = that.origin().x() + xUnit * that.size().widthInUnits();
            final var y = that.origin().y() + yUnit * that.size().heightInUnits();

            return at(x, y);
        }
    }

    public CartesianCoordinateSystem withOrigin(final double x, final double y)
    {
        return new CartesianCoordinateSystem(Coordinate.at(this, x, y), size);
    }

    public CartesianCoordinateSystem withSize(final double dx, final double dy)
    {
        return new CartesianCoordinateSystem(origin, CoordinateSize.size(this, dx, dy));
    }
}
