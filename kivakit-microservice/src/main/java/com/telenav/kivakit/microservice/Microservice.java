package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.project.Project;
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

/**
 * <p>
 * Defines a <a href="https://martinfowler.com/articles/microservices.html">microservice</a> application.
 * </p>
 *
 * <p>
 * A microservice application extending this base class needs to:
 *
 * <ol>
 *     <li>Create the microservice in the application's main(String[]) method</li>
 *     <li>Call {@link #run(String[])}, passing in the arguments to main()</li>
 *     <li>Pass any {@link Project} class to the constructor to ensure dependent projects are initialized</li>
 *     <li>Add a description of the service</li>
 *     <li>Return any {@link MicroserviceRestApplication} from {@link #restApplication()}</li>
 *     <li>Return any {@link MicroserviceWicketWebApplication} from {@link #webApplication()}</li>
 * </ol>
 * <p>
 * KivaKit will parse the command line and start the microservice on the port passed to the command line.
 * If no port is specified, the port in {@link MicroserviceSettings} will be used, as loaded from the
 * {@link Deployment} specified on the command line with -deployment=[deployment].
 * </p>
 *
 * <pre>
 * public class MyMicroservice extends Microservice
 * {
 *     public static void main(final String[] arguments)
 *     {
 *         new MyMicroservice().run(arguments);
 *     }
 *
 *     protected MyMicroservice()
 *     {
 *         super(new MyMicroserviceProject());
 *     }
 *
 *     @Override
 *     public String description()
 *     {
 *         return "My microservice";
 *     }
 *
 *     public MyRestApplication restApplication()
 *     {
 *         return new MyRestApplication();
 *     }
 *
 *     @Override
 *     public MyWebApplication webApplication()
 *     {
 *         return new MyWebApplication();
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see <a href="https://martinfowler.com/articles/microservices.html">Martin Fowler on Microservices</a>
 */
public abstract class Microservice extends Application
{
    /**
     * Command line switch for what port to run on. This will override any value from {@link MicroserviceSettings} that
     * is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> PORT =
            integerSwitchParser("port", "The port to use")
                    .optional()
                    .build();

    public Microservice(final MicroserviceProject... project)
    {
        super(project);
    }

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

    protected abstract MicroserviceRestApplication restApplication();

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.of(PORT);
    }

    protected abstract MicroserviceWicketWebApplication webApplication();

    private MicroserviceSettings settings()
    {
        return require(MicroserviceSettings.class);
    }
}
