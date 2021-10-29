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

public class AverageTest extends UnitTest
{
    @Test
    public void testAverage_addAverages()
    {
        var totalAverage = new Average();
        var total = 0D;
        var totalSampleCount = 0D;

        for (var i = 0; i < 100; i++)
        {
            var average = new Average();
            var sampleCount = randomInt(0, 1000);
            var tempTotal = 0D;
            for (var j = 0; j < sampleCount; j++)
            {
                var currentValue = randomValueFactory().newDouble(-10000, 10000);
                tempTotal += currentValue;
                average.add(currentValue);
            }
            total += tempTotal;
            totalSampleCount += sampleCount;
            totalAverage.add(average);
        }
        ensureEqual(total / totalSampleCount, totalAverage.average());
    }

    @Test
    public void testAverage_addIntegers()
    {
        for (var i = 0; i < 100; i++)
        {
            var average = new Average();
            var sampleCount = randomInt(1, 1000);
            var total = 0;
            for (var j = 0; j < sampleCount; j++)
            {
                var currentValue = randomInt(-10000, 10000);
                total += currentValue;
                average.add(currentValue);
            }
            ensureEqual((double) total / (double) sampleCount, average.average());
        }
    }
}
