/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // https://www.apache.org/licenses/LICENSE-2.0
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

package com.telenav.kivakit.ui.desktop.graphics.drawing.surfaces.java2d;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Collection;

/**
 * @author jonathanl (shibo)
 */
public class Java2dShapes
{
    /**
     * @return The given non-null shapes combined into a compound {@link Area} shape
     */
    public static Area combine(Shape... shapes)
    {
        var area = new Area();
        for (var shape : shapes)
        {
            if (shape != null)
            {
                area.add(new Area(shape));
            }
        }
        return area;
    }

    /**
     * @return The given non-null shapes combined into a compound {@link Area} shape
     */
    public static Area combine(Collection<Shape> shapes)
    {
        var area = new Area();
        for (var shape : shapes)
        {
            if (shape != null)
            {
                area.add(new Area(shape));
            }
        }
        return area;
    }
}
