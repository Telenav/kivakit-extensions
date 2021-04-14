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

package com.telenav.kivakit.ui.swing.event;

import com.telenav.kivakit.core.kernel.language.time.Frequency;

import javax.swing.*;

public class EventCoalescer
{
    private Timer timer;

    public EventCoalescer(final Frequency frequency, final Runnable callback)
    {
        timer = new Timer((int) frequency.cycleLength().asMilliseconds(), event ->
        {
            if (timer != null)
            {
                timer.stop();
                callback.run();
            }
        });
    }

    public void startTimer()
    {
        timer.restart();
    }
}
