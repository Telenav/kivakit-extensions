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

import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Stroke;

import static com.telenav.kivakit.ui.desktop.graphics.drawing.geometry.measurements.DrawingWidth.pixels;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitStrokes
{
    public static Stroke THIN = Stroke.stroke()
            .withWidth(pixels(1));
}
