package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.MetricReporter;
import com.telenav.kivakit.microservice.rest.microservlet.model.metrics.reporters.console.ConsoleMetricReporter;
import com.telenav.kivakit.microservice.web.MicroserviceWebApplication;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.web.jersey.JerseyJettyServletPlugin;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.AssetsJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerAssetsJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerIndexJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebJarJettyResourcePlugin;
import com.telenav.kivakit.web.wicket.WicketJettyFilterPlugin;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.commandline.SwitchParser.integerSwitchParser;

/**
 * <p>
 * Defines a <a href="https://martinfowler.com/articles/microservices.html">microservice</a> application.
 * </p>
 *
 * <p><b>Creating a Microservice</b></p>
 *
 * <p>
 * A microservice application extending this base class needs to:
 * </p>
 *
 * <ol>
 *     <li>Create the microservice in the application's main(String[]) method</li>
 *     <li>Call {@link #run(String[])}, passing in the arguments to main()</li>
 *     <li>Optionally, pass any {@link Project} class to the constructor to ensure dependent projects are initialized</li>
 *     <li>Optionally, add a description of the service for display in administrative user interfaces</li>
 *     <li>Return {@link MicroserviceMetadata} from {@link #metadata()}</li>
 *     <li>Return any {@link ResourceFolder} from {@link #openApiAssets()}</li>
 *     <li>Return any {@link MicroserviceRestApplication} from {@link #restApplication()}</li>
 *     <li>Return any {@link ResourceFolder} from {@link #staticAssets()}</li>
 *     <li>Return any {@link MicroserviceWebApplication} from {@link #webApplication()}</li>
 * </ol>
 *
 * <p><b>Mount Paths</b></p>
 * <p>
 * Microservice resources are mounted on the following paths:
 *
 * <p>
 * <table>
 *     <tr>
 *         <td>/</td><td>&nbsp;</td></td><td>Apache Wicket web application</td>
 *     </tr>
 *     <tr>
 *         <td>/</td><td>&nbsp;</td><td>Jersey REST application</td>
 *     </tr>
 *     <tr>
 *         <td>/assets</td><td>&nbsp;</td><td>Static resources</td>
 *     </tr>
 *     <tr>
 *         <td>/docs/</td><td>&nbsp;</td><td>Swagger documentation</td>
 *     </tr>
 *     <tr>
 *         <td>/open-api/assets</td><td>&nbsp;</td><td>Static resources for use in OpenAPI definitions</td>
 *     </tr>
 *     <tr>
 *         <td>/open-api/</td><td>&nbsp;</td><td>Open API definition</td>
 *     </tr>
 *     <tr>
 *         <td>/swagger/webapp</td><td>&nbsp;</td><td>Swagger static resources</td>
 *     </tr>
 *     <tr>
 *         <td>/swagger/webjar</td><td>&nbsp;</td><td>Swagger design webjar</td>
 *     </tr>
 * </table>
 * </p>
 *
 * <p>
 * KivaKit will parse the command line and start the microservice on any port passed to the command line with
 * -port=[port]. If no port is specified, the port in {@link MicroserviceSettings} will be used, as loaded from the
 * {@link Deployment} specified on the command line with -deployment=[deployment]. See
 * </p>
 *
 * <p><b>Example</b></p>
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
 *     &#064;Override
 *     public String description()
 *     {
 *         return "My microservice";
 *     }
 *
 *     &#064;Override
 *     public MyRestApplication restApplication()
 *     {
 *         return new MyRestApplication();
 *     }
 *
 *     &#064;Override
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
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public abstract class Microservice extends Application implements Startable
{
    /**
     * Command line switch for what port to run on. This will override any value from {@link MicroserviceSettings} that
     * is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> PORT =
            integerSwitchParser("port", "The port to use")
                    .optional()
                    .build();

    private boolean running;

    public Microservice(final MicroserviceProject... project)
    {
        super(project);

        registerObject(reporter());
    }

    @Override
    public String description()
    {
        return metadata().description();
    }

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @UmlRelation(label = "has")
    public abstract MicroserviceMetadata metadata();

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean start()
    {
        if (!running)
        {
            // Show command line arguments,
            showCommandLine();

            // get the port to run on,
            final var port = has(PORT) ? get(PORT) : settings().port();

            // create the Jetty server.
            final var server = listenTo(new JettyServer().port(port));

            // If there's an Apache Wicket web application,
            final var webApplication = webApplication();
            if (webApplication != null)
            {
                // mount them on the server.
                server.mount("/*", new WicketJettyFilterPlugin(webApplication));
            }

            // If there are static resources,
            final var staticAssets = staticAssets();
            if (staticAssets != null)
            {
                // mount them on the server.
                server.mount("/assets/*", new AssetsJettyResourcePlugin(staticAssets));
            }

            // If there is a Jersey REST application,
            final var restApplication = listenTo(restApplication());
            if (restApplication != null)
            {
                // and there are static OpenAPI assets,
                final var openApiAssets = openApiAssets();
                if (openApiAssets != null)
                {
                    // mount them on the server.
                    server.mount("/open-api/assets/*", new AssetsJettyResourcePlugin(openApiAssets));
                }

                // Mount Swagger resources for the REST application.
                server.mount("/*", registerObject(new MicroservletJettyFilterPlugin(restApplication)));
                server.mount("/docs/*", new SwaggerIndexJettyResourcePlugin(port));
                server.mount("/swagger/webapp/*", new SwaggerAssetsJettyResourcePlugin());
                server.mount("/swagger/webjar/*", new SwaggerWebJarJettyResourcePlugin(restApplication));

                // Mount the REST application.
                server.mount("/*", new JerseyJettyServletPlugin(restApplication));
            }

            // Start the server.
            announce("Microservice server starting on port ${integer}", port);
            server.start();
            running = true;
        }
        return true;
    }

    /**
     * Called to initialize the microservice before it's running
     */
    protected void onInitialize()
    {
    }

    @Override
    protected final void onRun()
    {
        // Initialize this microservice,
        onInitialize();

        // then start it running.
        start();
    }

    /**
     * @return The resource folder containing static assets for reference by OpenAPI .yaml files and annotations. For
     * example, a microservice might want to include an OAS .yaml file.
     */
    protected ResourceFolder openApiAssets()
    {
        return Package.of(restApplication().getClass(), "assets");
    }

    /**
     * @return The REST application
     */
    protected abstract MicroserviceRestApplication restApplication();

    /**
     * @return The resource folder containing static assets. The resources will be mounted on <i>/assets</i>.
     */
    protected ResourceFolder staticAssets()
    {
        return Package.of(getClass(), "assets");
    }

    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.of(PORT);
    }

    /**
     * @return The Apache Wicket web application for this microservice, if any.
     */
    protected MicroserviceWebApplication webApplication()
    {
        return null;
    }

    MetricReporter reporter()
    {
        return new ConsoleMetricReporter();
    }

    @UmlRelation(label = "has")
    private MicroserviceSettings settings()
    {
        return require(MicroserviceSettings.class);
    }
}
