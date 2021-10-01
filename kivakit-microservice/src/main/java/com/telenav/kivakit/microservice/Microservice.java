package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.Switch;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.microservlet.internal.plugins.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.MetricReporter;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.console.ConsoleMetricReporter;
import com.telenav.kivakit.microservice.rest.microservlet.metrics.reporters.none.NullMetricReporter;
import com.telenav.kivakit.microservice.web.MicroserviceWebApplication;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.AssetsJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerAssetsJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerIndexJettyResourcePlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebJarJettyResourcePlugin;
import com.telenav.kivakit.web.wicket.WicketJettyFilterPlugin;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;

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
 *     <li>Create the {@link Microservice} subclass in the application's main(String[]) method</li>
 *     <li>Call {@link #run(String[])}, passing in the arguments to main()</li>
 *     <li>Optionally, pass any {@link Project} class to the constructor to ensure dependent projects are initialized</li>
 *     <li>Override {@link #restApplication()} to provide the microservice's {@link MicroserviceRestApplication} subclass</li>
 *     <li>Override {@link #description()} to provide a description of the microservice for display in administrative user interfaces</li>
 *     <li>Override {@link #metadata()} to provide {@link MicroserviceMetadata} used in the REST OpenAPI specification at /open-api/swagger.json</li>
 *     <li>Optionally, override {@link #openApiAssetsFolder()} to provide any package or folder for OpenAPI specification .yaml files</li>
 *     <li>Optionally, override {@link #staticAssetsFolder()} to provide a package or folder with static resources</li>
 *     <li>Optionally, override {@link #webApplication()} to provide an Apache Wicket {@link MicroserviceWebApplication} subclass</li>
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
 *         <td>/</td><td>&nbsp;</td><td>Microservlet REST application</td>
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
 *         <td>/open-api/swagger.json</td><td>&nbsp;</td><td>Open API definition</td>
 *     </tr>
 *     <tr>
 *         <td>/swagger/webapp</td><td>&nbsp;</td><td>Swagger application</td>
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
 * {@link Deployment} specified on the command line with -deployment=[deployment].
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
 *     public MyRestApplication restApplication()
 *     {
 *         return new MyRestApplication(this);
 *     }
 *
 *     &#064;Override
 *     public MyWebApplication webApplication()
 *     {
 *         return new MyWebApplication(this);
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see Switch
 * @see ResourceFolder
 * @see MicroserviceMetadata
 * @see MetricReporter
 * @see ConsoleMetricReporter
 * @see MicroserviceSettings
 * @see MicroserviceRestApplication
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

    /** True if this microservice is running */
    private boolean running;

    /**
     * Initializes this microservice and any project(s) it depends on
     */
    public Microservice(final Project... project)
    {
        super(project);

        register(this);
        register(metricReporter());
    }

    /**
     * @return A description of this microservice
     */
    @Override
    public String description()
    {
        return metadata().description();
    }

    /**
     * This method implements the {@link Startable#isRunning()} method. It returns true if the service is running and
     * false otherwise. The {@link #start()} method should not be called, as this microservice will start running
     * automatically when the {@link Application#run(String[])} method is called from the Java <i>main(String[])</i>
     * method entrypoint.
     *
     * @return True if this microservice is running
     */
    @Override
    public boolean isRunning()
    {
        return running;
    }

    /**
     * @return Metadata about this microservice
     */
    @UmlRelation(label = "has")
    public abstract MicroserviceMetadata metadata();

    /**
     * Called to initialize the microservice before it's running. This is a good place to register objects with {@link
     * #register(Object)} or {@link #register(Object, Enum)}. While settings can be registered in this method, for most
     * microservices, it will make more sense to use the more automatic method provided by {@link Deployment}s.
     */
    public void onInitialize()
    {
    }

    /**
     * @return The REST application for this microservice
     */
    public abstract MicroserviceRestApplication restApplication();

    /**
     * <b>Not public API</b>
     * <p>
     * Implements the method {@link Startable#start()} and starts the microservice running. This method is called by
     * {@link #onRun()} and should not be called by framework users.
     * </p>
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
            final var staticAssets = staticAssetsFolder();
            if (staticAssets != null)
            {
                // mount them on the server.
                server.mount("/assets/*", new AssetsJettyResourcePlugin(staticAssets));
            }

            // If there is a microservlet REST application,
            final var restApplication = listenTo(restApplication());
            if (restApplication != null)
            {
                // and there are static OpenAPI assets,
                final var openApiAssets = openApiAssetsFolder();
                if (openApiAssets != null)
                {
                    // mount them on the server.
                    server.mount("/open-api/assets/*", new AssetsJettyResourcePlugin(openApiAssets));
                }

                // Mount Swagger resources for the REST application.
                server.mount("/*", register(new MicroservletJettyFilterPlugin(restApplication)));
                server.mount("/docs/*", new SwaggerIndexJettyResourcePlugin(port));
                server.mount("/swagger/webapp/*", new SwaggerAssetsJettyResourcePlugin());
                server.mount("/swagger/webjar/*", new SwaggerWebJarJettyResourcePlugin(restApplication.getClass()));

                // and initialize it.
                restApplication.initialize();
            }

            // Start the server.
            announce("Microservice server starting on port ${integer}", port);
            server.start();
            running = true;
        }
        return true;
    }

    /**
     * @return The {@link MetricReporter} implementation to use. If no metric reporter is provided, {@link
     * NullMetricReporter} will be used.
     */
    protected MetricReporter metricReporter()
    {
        return new NullMetricReporter();
    }

    /**
     * <b>Not public API</b>
     * <p>
     * This method should not be invoked or overridden. To initialize a microservice, override the {@link
     * #onInitialize()} method.
     * </p>
     */
    @Override
    protected final void onRun()
    {
        // Initialize this microservice,
        onInitialize();

        // then start it running,
        start();

        // and let clients know we're ready.
        ready();
    }

    /**
     * @return The resource folder containing static assets for reference by OpenAPI .yaml files and KivaKit OpenApi
     * annotations. For example, a microservice might want to include an OAS .yaml file. If this method is not
     * overridden, the default folder will be the "assets" sub-package of tshe rest application's package.
     */
    protected ResourceFolder openApiAssetsFolder()
    {
        return Package.of(restApplication().getClass(), "assets");
    }

    /**
     * @return The resource folder containing static assets. The resources will be mounted on <i>/assets</i>. If this
     * method is not overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder staticAssetsFolder()
    {
        return Package.of(getClass(), "assets");
    }

    /**
     * Provides the built-in -port=[number] switch to allow users of the microservice to run the REST and web
     * applications on a given port. If no port is specified explicitly, then the {@link MicroserviceSettings} class
     * provides a default through the {@link Deployment} command line switch. Subclasses that override this method can
     * include the switches provided by this method by using the {@link ObjectSet#with(Collection)} method. For
     * example:
     * <pre>
     * {@literal @}Override
     *  protected ObjectSet&lt;SwitchParser&lt;?&gt;&gt; switchParsers()
     *  {
     *      return ObjectSet.of(...).with(super.switchParsers);
     *  }
     * </pre>
     */
    @Override
    @MustBeInvokedByOverriders
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return ObjectSet.of(PORT);
    }

    /**
     * @return The Apache Wicket web application, if any, for configuring or viewing the status of this microservice.
     */
    protected MicroserviceWebApplication webApplication()
    {
        return null;
    }

    @UmlRelation(label = "has")
    private MicroserviceSettings settings()
    {
        return require(MicroserviceSettings.class);
    }
}
