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

package com.telenav.kivakit.math.statistics;

import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Records a series of attempts and which of them were successful to produce as {@link #successRate()}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class SuccessRate
{
    private final AtomicInteger successes = new AtomicInteger();

    private final AtomicInteger attempts = new AtomicInteger();

    /**
     * An attempt is being made
     */
    public void attempt()
    {
        attempts.incrementAndGet();
    }

    /**
     * An attempt was successful
     */
    public void success()
    {
        successes.incrementAndGet();
    }

    /**
     * @return The success rate of attempts
     */
    public Percent successRate()
    {
        return Percent.of(100.0 * successes.get() / attempts.get());
    }
}
