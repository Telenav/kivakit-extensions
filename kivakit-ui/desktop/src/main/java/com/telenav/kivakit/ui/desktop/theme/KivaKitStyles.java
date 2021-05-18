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

package com.telenav.kivakit.ui.desktop.theme;

import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Fonts;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color.TRANSPARENT;
import static com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color.WHITE;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.AQUA;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.BATTLESHIP_GRAY;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.BLACK_OLIVE;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.BLUE_RIDGE_MOUNTAINS;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.CLOVER;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.FOSSIL;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.IRON;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.LIME;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.MARASCHINO;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.OCEAN;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.SILVER;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.SMOKE;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TANGERINE;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSLUCENT_CLOVER;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSLUCENT_LIME;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSLUCENT_OCEAN;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSLUCENT_TANGERINE;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.TRANSLUCENT_YELLOW;
import static com.telenav.kivakit.ui.desktop.theme.KivaKitColors.UNSPECIFIED;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitStyles
{
    public static final Style BASE = Style.create()
            .withFillColor(UNSPECIFIED)
            .withDrawColor(UNSPECIFIED)
            .withTextColor(UNSPECIFIED)
            .withTextFont(Fonts.component(12));

    public static final Style ARUBA = BASE
            .withFillColor(AQUA.translucent())
            .withDrawColor(AQUA)
            .withTextColor(BLACK_OLIVE);

    public static final Style ABSINTHE = BASE
            .withFillColor(LIME)
            .withDrawColor(FOSSIL)
            .withTextColor(FOSSIL);

    public static final Style BLUE_RING = BASE
            .withFillColor(TRANSPARENT)
            .withDrawColor(AQUA)
            .withTextColor(BLACK_OLIVE);

    public static final Style GOLF = BASE
            .withFillColor(TRANSLUCENT_CLOVER)
            .withDrawColor(AQUA.brighter())
            .withTextColor(BLACK_OLIVE);

    public static final Style HEADACHE = BASE
            .withFillColor(TRANSLUCENT_OCEAN)
            .withDrawColor(TANGERINE)
            .withTextColor(TANGERINE);

    public static final Style INVISIBLE = BASE
            .withFillColor(TRANSPARENT)
            .withDrawColor(TRANSPARENT)
            .withTextColor(TRANSPARENT);

    public static final Style LIFEBUOY = BASE
            .withFillColor(TRANSLUCENT_OCEAN)
            .withDrawColor(TANGERINE)
            .withTextColor(IRON);

    public static final Style MANHATTAN = BASE
            .withFillColor(MARASCHINO.translucent())
            .withDrawColor(MARASCHINO)
            .withTextColor(SILVER);

    public static final Style MOJITO = BASE
            .withFillColor(TRANSLUCENT_LIME)
            .withDrawColor(LIME)
            .withTextColor(BLACK_OLIVE);

    public static final Style NIGHT_SHIFT = BASE
            .withFillColor(TRANSLUCENT_TANGERINE)
            .withDrawColor(OCEAN)
            .withTextColor(IRON);

    public static final Style OCEAN_SURF = BASE
            .withFillColor(OCEAN)
            .withDrawColor(SMOKE)
            .withTextColor(SMOKE);

    public static final Style POKER = BASE
            .withFillColor(TRANSLUCENT_CLOVER)
            .withDrawColor(CLOVER)
            .withTextColor(BLACK_OLIVE);

    public static final Style SEATTLE = BASE
            .withFillColor(IRON)
            .withDrawColor(BATTLESHIP_GRAY)
            .withTextColor(BATTLESHIP_GRAY);

    public static final Style STOP_SIGN = BASE
            .withFillColor(MARASCHINO)
            .withDrawColor(WHITE)
            .withTextColor(WHITE);

    public static final Style SUNNY = BASE
            .withFillColor(TRANSLUCENT_YELLOW)
            .withDrawColor(BLUE_RIDGE_MOUNTAINS)
            .withTextColor(BLACK_OLIVE);

    public static final Style VALENCIA = BASE
            .withFillColor(TANGERINE)
            .withDrawColor(FOSSIL)
            .withTextColor(AQUA);
}
