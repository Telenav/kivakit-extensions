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

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class AverageDurationTest extends UnitTest
{
    @Test
    public void test()
    {
        var average = new AverageDuration();
        for (var i = 0; i <= 10; i++)
        {
            average.add(Duration.seconds(i));
        }
        ensureEqual(Duration.seconds(5), average.averageDuration());
        ensureEqual(11, average.samples());
    }
}
