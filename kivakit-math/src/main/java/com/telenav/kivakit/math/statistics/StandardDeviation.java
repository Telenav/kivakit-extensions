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

import java.util.ArrayList;
import java.util.List;

/**
 * Standard deviation of a series of samples.
 *
 * @author matthieun
 */
public class StandardDeviation extends Average
{
    private final List<Double> values;

    public StandardDeviation()
    {
        values = new ArrayList<>();
    }

    @Override
    public void add(double value)
    {
        super.add(value);
        values.add(value);
    }

    public double standardDeviation()
    {
        var squareDifferencesSum = 0.0;
        var average = average();
        for (var value : values)
        {
            squareDifferencesSum += Math.pow(average - value, 2);
        }
        return Math.sqrt(squareDifferencesSum / values.size());
    }
}
