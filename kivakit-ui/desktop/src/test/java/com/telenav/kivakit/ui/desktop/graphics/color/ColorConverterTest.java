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

package com.telenav.kivakit.ui.desktop.graphics.color;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import org.junit.Test;

public class ColorConverterTest extends UnitTest
{
    @Test
    public void test()
    {
        final var converter = new Color.ColorConverter(Listener.none());
        ensureEqual(Color.RED, converter.convert("ffff0000"));
        ensureEqual(Color.GREEN, converter.convert("ff00ff00"));
        ensureEqual(Color.BLUE, converter.convert("ff0000ff"));
        ensureEqual("ffff0000", converter.unconvert(Color.RED));
        ensureEqual("ff00ff00", converter.unconvert(Color.GREEN));
        ensureEqual("ff0000ff", converter.unconvert(Color.BLUE));
    }
}
