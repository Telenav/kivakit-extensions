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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.logging.Log;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.logs.email.internal.lexakai.DiagramLogsEmail;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.email.Email;
import com.telenav.kivakit.network.email.EmailBody;
import com.telenav.kivakit.network.email.EmailSender;
import com.telenav.kivakit.network.email.senders.SmtpEmailSender;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.consoleListener;
import static com.telenav.kivakit.core.registry.Registry.registryFor;
import static com.telenav.kivakit.network.core.EmailAddress.parseEmailAddress;
import static com.telenav.kivakit.network.core.Host.parseHost;
import static com.telenav.kivakit.network.core.authentication.UserName.parseUserName;
import static com.telenav.kivakit.network.core.authentication.passwords.PlainTextPassword.parsePlainTextPassword;

/**
 * A {@link Log} service provider that sends emails. Configuration occurs via {@link #configure(VariableMap)}, which
 * receives the key value pairs parsed from the KIVAKIT_LOG environment variable.
 *
 * <p><b>Options</b></p>
 *
 * <p>
 * The options available for configuration with this logger are:
 * </p>
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
 * <p><b>Example</b></p>
 *
 * <pre>
 * -DKIVAKIT_LOG="from=noreply@telenav.com to=jonathanl@telenav.com subject=\"Hello\" host=\"smtp.telenav.com\" username=jonathanl@telenav.com password=snarky-purple-ducky-4127"
 * </pre>
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsEmail.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class EmailLog extends BaseTextLog
{
    /** The from email address */
    @UmlAggregation(label = "from")
    private EmailAddress from;

    /** The email sender */
    @UmlAggregation
    private EmailSender sender;

    /** The subject for the email */
    private String subject;

    @UmlAggregation(label = "to")
    private final Set<EmailAddress> to = new HashSet<>();

    /**
     * {@inheritDoc}
     */
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
                this.to.add(parseEmailAddress(consoleListener(), at));
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
            this.from = parseEmailAddress(consoleListener(), from);
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
            configuration.host(parseHost(consoleListener(), host));
            configuration.username(parseUserName(consoleListener(), username));
            configuration.password(parsePlainTextPassword(consoleListener(), password));
            sender = new SmtpEmailSender(configuration);
        }
    }

    /**
     * {@inheritDoc}
     */
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
            sender = registryFor(this).lookup(EmailSender.class);
        }
        sender.enqueue(email);
    }
}
