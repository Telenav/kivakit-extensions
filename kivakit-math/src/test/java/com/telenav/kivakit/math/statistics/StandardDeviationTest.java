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

package com.telenav.kivakit.math.statistics;

import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

/**
 * Test the standard deviation class
 *
 * @author matthieun
 */
public class StandardDeviationTest extends UnitTest
{
    @Test
    public void testStandardDeviation()
    {
        var stddev = new StandardDeviation();
        var values = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (double value : values)
        {
            stddev.add(value);
        }
        ensureEqual(5.0, stddev.average());
        ensureClose(2.58, stddev.standardDeviation(), 2);
    }
}
