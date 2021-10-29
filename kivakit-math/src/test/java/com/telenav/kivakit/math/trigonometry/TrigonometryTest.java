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

import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class TrigonometryTest extends UnitTest
{
    @Test
    public void testArcTangent()
    {
        var error = 0D;
        var delta = 1.0;
        for (double y = -100; y < 100; y += delta)
        {
            for (double x = -100; x < 100; x += delta)
            {
                error += Math.abs(Math.atan2(y, x) - Trigonometry.arcTangent2(y, x));
            }
        }
        assert error / (2_000 * 2_000) < 0.01;
    }
}
