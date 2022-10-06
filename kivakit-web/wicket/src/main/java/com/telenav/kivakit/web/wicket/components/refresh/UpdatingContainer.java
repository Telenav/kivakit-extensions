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

package com.telenav.kivakit.web.wicket.components.refresh;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.time.Frequency;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.function.Consumer;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.web.wicket.library.Components.autoUpdateComponent;

/**
 * A {@link WebMarkupContainer} that refreshes itself at the given {@link Frequency}.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class UpdatingContainer extends WebMarkupContainer
{
    public UpdatingContainer(String id, Frequency frequency)
    {
        this(id, frequency, target ->
        {
        });
    }

    public UpdatingContainer(String id, Frequency frequency, Consumer<AjaxRequestTarget> target)
    {
        super(id);

        autoUpdateComponent(this, frequency, target);
    }
}
