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

package com.telenav.kivakit.web.wicket.components.feedback;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.web.wicket.theme.KivaKitTheme;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITHOUT_EXCEPTION;

/**
 * A KivaKit feedback panel in the KivaKit style.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class FeedbackPanel extends org.apache.wicket.markup.html.panel.FeedbackPanel implements ComponentMixin
{
    public FeedbackPanel(String id)
    {
        super(id);
    }

    @Override
    public void onTransmitting(Transmittable message)
    {
        if (message instanceof Problem)
        {
            var problem = (Problem) message;
            error(problem.formatted(WITHOUT_EXCEPTION));
        }

        if (message instanceof Warning)
        {
            var problem = (Warning) message;
            warning(problem.formatted(WITHOUT_EXCEPTION));
        }
    }

    @Override
    public void renderHead(IHeaderResponse response)
    {
        response.render(CssHeaderItem.forReference(KivaKitTheme.kivakitColors()));
        response.render(CssHeaderItem.forReference(KivaKitTheme.kivakitTheme()));
    }
}
