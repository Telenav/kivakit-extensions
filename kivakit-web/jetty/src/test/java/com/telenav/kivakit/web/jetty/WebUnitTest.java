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

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.testing.UnitTest;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Adds a web server to {@link UnitTest} that can be started with {@link #startWebServer(int, FilePath)} where
 * {@link FilePath} is the folder for WAR resources.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class WebUnitTest extends UnitTest
{
    /**
     * @param portNumber The port number to use
     * @param war The path to WAR resources
     */
    @SuppressWarnings("SameParameterValue")
    protected void startWebServer(int portNumber, FilePath war)
    {
        var http = new HttpConfiguration();
        var server = new Server();
        var connector = new ServerConnector(server, new HttpConnectionFactory(http));
        connector.setPort(portNumber);
        server.addConnector(connector);

        var webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(war.asString());
        server.setHandler(webapp);

        try
        {
            server.start();
        }
        catch (Exception e)
        {
            throw new Problem(e, "Couldn't start embedded Jetty web server").asException();
        }
    }
}
