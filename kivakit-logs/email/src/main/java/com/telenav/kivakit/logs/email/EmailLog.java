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

package com.telenav.kivakit.logs.email;

import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.collections.map.string.VariableMap;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.logs.email.project.lexakai.DiagramLogsEmail;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.email.Email;
import com.telenav.kivakit.network.email.EmailBody;
import com.telenav.kivakit.network.email.EmailSender;
import com.telenav.kivakit.network.email.senders.SmtpEmailSender;
import com.telenav.kivakit.security.authentication.UserName;
import com.telenav.kivakit.security.authentication.passwords.PlainTextPassword;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.ensure.Ensure.fail;

/**
 * A {@link Log} service provider that sends emails. Configuration occurs via the command line. See {@link
 * LogServiceLogger} for details. Further details are available in the markdown help. The options available for
 * configuration with this logger are:
 *
 * <ul>
 *     <li><i>to</i> - The set of email addresses to send to</li>
 *     <li><i>from</i> - The email address to send from</li>
 *     <li><i>subject</i> - The subject line</li>
 *     <li><i>host</i> - The SMTP host</li>
 *     <li><i>username</i> - The SMTP username</li>
 *     <li><i>password</i> - The SMTP password</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsEmail.class)
@LexakaiJavadoc(complete = true)
public class EmailLog extends BaseTextLog
{
    @UmlAggregation
    private EmailSender sender;

    @UmlAggregation(label = "to")
    private final Set<EmailAddress> to = new HashSet<>();

    @UmlAggregation(label = "from")
    private EmailAddress from;

    private String subject;

    @Override
    public void configure(VariableMap<String> properties)
    {
        super.configure(properties);

        // Subject
        subject = properties.get("subject");
        if (subject == null)
        {
            fail("EmailLog missing subject property");
        }

        // To email addresses
        var to = properties.get("to");
        if (to != null)
        {
            for (var at : to.split(","))
            {
                this.to.add(EmailAddress.parse(Listener.console(), at));
            }
        }
        else
        {
            fail("EmailLog missing to property");
        }

        // From email address
        var from = properties.get("from");
        if (from != null)
        {
            this.from = EmailAddress.parse(Listener.console(), from);
        }
        else
        {
            fail("EmailLog missing to property");
        }

        // Sender
        var host = properties.get("host");
        var username = properties.get("username");
        var password = properties.get("password");
        if (host != null && username != null && password != null)
        {
            var configuration = new SmtpEmailSender.Configuration();
            configuration.host(Host.parse(Listener.console(), host));
            configuration.username(UserName.parse(Listener.console(), username));
            configuration.password(PlainTextPassword.parse(Listener.console(), password));
            sender = new SmtpEmailSender(configuration);
        }
    }

    @Override
    public String name()
    {
        return "Email";
    }

    @Override
    protected void onLog(LogEntry entry)
    {
        var email = new Email();
        email.to(to);
        email.from(from);
        email.body(new EmailBody(formatted(entry)));
        email.subject(subject);
        if (sender == null)
        {
            sender = Registry.of(this).lookup(EmailSender.class);
        }
        sender.enqueue(email);
    }
}
