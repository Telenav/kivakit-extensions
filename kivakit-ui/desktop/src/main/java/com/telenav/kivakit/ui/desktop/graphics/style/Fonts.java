/*
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * // © 2011-2021 Telenav, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * // http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * //
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 */

package com.telenav.kivakit.ui.desktop.graphics.style;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static java.awt.font.TextAttribute.FAMILY;
import static java.awt.font.TextAttribute.SIZE;
import static java.awt.font.TextAttribute.WEIGHT;
import static java.awt.font.TextAttribute.WEIGHT_LIGHT;

/**
 * @author jonathanl (shibo)
 */
public class Fonts
{
    public static Font component(final int size)
    {
        final Map<TextAttribute, Object> attributes = new HashMap<>();

        attributes.put(FAMILY, "Open Sans,Avenir,Nunito,Arial,Helvetica,SansSerif");
        attributes.put(WEIGHT, WEIGHT_LIGHT);
        attributes.put(SIZE, size);

        return Font.getFont(attributes);
    }

    public static Font fixedWidth(final int style, final int size)
    {
        final var fonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (final var at : new String[]
                {
                        "Monaco",
                        "Consolas",
                        "Lucida Console",
                        "Menlo",
                        "Sans-Serif",
                        "Monospaced"
                })
        {
            if (fonts.contains(at))
            {
                return new Font(at, style, size);
            }
        }
        return fail("Could not find an acceptable fixed width font");
    }
}
