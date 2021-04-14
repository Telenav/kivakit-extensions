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

package com.telenav.kivakit.ui.swing.graphics.color;

import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.strings.Align;
import com.telenav.kivakit.core.kernel.messaging.Listener;

public class ColorConverter extends BaseStringConverter<Color>
{
    public ColorConverter(final Listener listener)
    {
        super(listener);
    }

    @Override
    protected Color onConvertToObject(final String value)
    {
        if (value.length() == 8)
        {
            final var alpha = Integer.parseInt(value.substring(0, 2), 16);
            final var red = Integer.parseInt(value.substring(2, 4), 16);
            final var green = Integer.parseInt(value.substring(4, 6), 16);
            final var blue = Integer.parseInt(value.substring(6, 8), 16);
            return Color.rgba(red, green, blue, alpha);
        }
        return null;
    }

    @Override
    protected String onConvertToString(final Color value)
    {
        return Align.right(Integer.toHexString(value.rgba()), 8, '0');
    }
}
