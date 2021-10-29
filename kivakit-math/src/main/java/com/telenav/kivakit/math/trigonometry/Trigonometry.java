////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.math.trigonometry;

import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Faster, although less accurate, versions of expensive trigonometric functions.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class Trigonometry
{
    private static final int COUNT = 100_000;

    private static final double[] arcTangents = new double[COUNT + 1];

    static
    {
        for (var i = 0; i < arcTangents.length; i++)
        {
            arcTangents[i] = Math.atan((1.0 / COUNT) * i);
        }
    }

    public static double arcTangent(double value)
    {
        if (value < 0.0)
        {
            return -arcTangent(-value);
        }
        else if (value > 1.0)
        {
            return Math.PI * 0.5 - arcTangents[(int) (COUNT / value)];
        }
        else
        {
            return arcTangents[(int) (COUNT * value)];
        }
    }

    public static double arcTangent2(double y, double x)
    {
        if (x > 0)
        {
            return arcTangent(y / x);
        }
        else if (x < 0)
        {
            return y >= 0 ? Math.PI + arcTangent(y / x) : arcTangent(y / x) - Math.PI;
        }
        else
        {
            if (y > 0)
            {
                return Math.PI / 2;
            }
            else if (y < 0)
            {
                return -Math.PI / 2;
            }
            else
            {
                return 0;
            }
        }
    }
}
