package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.web.MicroserviceWicketWebApplication;
import com.telenav.kivakit.web.jersey.JettyJersey;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.JettyStaticResources;
import com.telenav.kivakit.web.swagger.JettySwaggerIndex;
import com.telenav.kivakit.web.swagger.JettySwaggerOpenApi;
import com.telenav.kivakit.web.swagger.JettySwaggerStaticResources;
import com.telenav.kivakit.web.swagger.JettySwaggerWebJar;
import com.telenav.kivakit.web.wicket.JettyWicket;

import static com.telenav.kivakit.commandline.SwitchParser.integerSwitchParser;

public abstract class Microservice extends Application
{
    private final SwitchParser<Integer> PORT =
            integerSwitchParser("port", "The port to use")
                    .defaultValue(9999)
                    .optional()
                    .build();

    public Microservice(final MicroserviceProject... project)
    {
        super(project);
    }

    public abstract MicroserviceRestApplication restApplication();

    public abstract MicroserviceWicketWebApplication webApplication();

    @Override
    protected void onRun()
    {
        // Show command line arguments,
        showCommandLine();

        // get the port to run on,
        final var port = has(PORT) ? get(PORT) : settings().port();

        // create the Jersey REST application,
        final var restApplication = listenTo(restApplication());

        // and start up Jetty with Swagger, Jersey and Wicket.
        listenTo(new JettyServer())
                .port(port)
                .add("/*", new JettyWicket(webApplication()))
                .add("/open-api/*", new JettySwaggerOpenApi(restApplication))
                .add("/docs/*", new JettySwaggerIndex(port))
                .add("/webapp/*", new JettySwaggerStaticResources())
                .add("/webjar/*", new JettySwaggerWebJar(restApplication))
                .add("/ui/*", new JettyStaticResources(getClass(), "ui"))
                .add("/*", new JettyJersey(restApplication))
                .start();
    }

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.of(PORT);
    }

    private MicroserviceSettings settings()
    {
        return require(MicroserviceSettings.class);
    }
}
