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

package com.telenav.kivakit.web.wicket.library;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.time.Frequency;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;

import java.util.function.Consumer;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.API_PRIVATE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Utility methods useful for Apache Wicket {@link Component}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE,
            type = API_PRIVATE)
public class Components
{
    /**
     * Updates the given Wicket component at the given frequency, calling the given consumer with
     * {@link AjaxRequestTarget} after updating
     *
     * @param component The component
     * @param frequency The update frequency
     * @param afterUpdate The code to call after updating
     */
    public static void autoUpdateComponent(Component component,
                                           Frequency frequency,
                                           Consumer<AjaxRequestTarget> afterUpdate)
    {
        component.setOutputMarkupId(true);
        component.setOutputMarkupPlaceholderTag(true);
        component.add(new AjaxSelfUpdatingTimerBehavior(frequency.cycleLength().asJavaDuration())
        {
            @Override
            protected void onPostProcessTarget(AjaxRequestTarget target)
            {
                super.onPostProcessTarget(target);
                afterUpdate.accept(target);
            }
        });
    }
}
