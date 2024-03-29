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

package com.telenav.kivakit.web.wicket.components.header;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.version.Version;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * A standardized KivaKit header panel with a title, logo and version information.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class HeaderPanel extends Panel
{
    /**
     * @param id Wicket identifier
     * @param version Project version
     * @param title Header title
     */
    public HeaderPanel(String id, Version version, String title)
    {
        super(id);
        add(new Label("title", title));
        var icon = new Image("icon",
                new PackageResourceReference(getClass(), "kivakit-48.png"),
                new PackageResourceReference(getClass(), "kivakit-48-2x.png"));
        icon.setXValues("2x");
        add(icon);
        add(new Label("version", version + " / KivaKit "
                + resolveProject(KivaKit.class).projectVersion() + " "
                + resolveProject(KivaKit.class).build()));
    }
}
