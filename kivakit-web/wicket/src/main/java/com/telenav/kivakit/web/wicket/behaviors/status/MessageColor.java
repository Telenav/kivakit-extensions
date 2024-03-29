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

package com.telenav.kivakit.web.wicket.behaviors.status;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Message;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A Wicket {@link AttributeModifier} that changes the class of the component it is attached to based on
 * {@link Message#status()}. This is used to highlight success, warning and failure conditions visually using the
 * *kivakit-problem*, *kivakit-warning* and *kivakit-information* CSS classes in KivaKitTheme.css.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class MessageColor extends AttributeModifier
{
    public MessageColor(IModel<Message.Status> status)
    {
        this(null, status);
    }

    public MessageColor(String prefix, IModel<Message.Status> model)
    {
        super("class", () ->
        {
            String className = null;
            var status = model.getObject();
            if (status != null)
            {
                switch (status)
                {
                    case FAILED:
                    case PROBLEM:
                        className = "kivakit-problem";
                        break;

                    case RESULT_COMPROMISED:
                    case RESULT_INCOMPLETE:
                        className = "kivakit-warning";
                        break;

                    case SUCCEEDED:
                    case COMPLETED:
                    default:
                        className = "kivakit-information";
                        break;
                }
            }
            return className == null ? prefix : prefix != null ? prefix + " " + className : className;
        });
    }
}
