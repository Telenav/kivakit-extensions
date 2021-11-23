package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.Switch;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.settings.Deployment;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Startable;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyFilterPlugin;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservice;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
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
import org.apache.wicket.protocol.http.WebApplication;
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
 *     <li>Override {@link #onNewRestService()} to provide the microservice's {@link MicroserviceRestService} subclass</li>
 *     <li>Override {@link #description()} to provide a description of the microservice for display in administrative user interfaces</li>
 *     <li>Override {@link #metadata()} to provide {@link MicroserviceMetadata} used in the REST OpenAPI specification at /open-api/swagger.json</li>
 *     <li>Optionally, override {@link #openApiAssetsFolder()} to provide any package or folder for OpenAPI specification .yaml files</li>
 *     <li>Optionally, override {@link #staticAssetsFolder()} to provide a package or folder with static resources</li>
 *     <li>Optionally, override {@link #onCreateWebApplication()} to provide an Apache Wicket {@link MicroserviceWebApplication} subclass</li>
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
 *     public static void main( String[] arguments)
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
 * @see MicroserviceSettings
 * @see MicroserviceRestService
 * @see <a href="https://martinfowler.com/articles/microservices.html">Martin Fowler on Microservices</a>
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public abstract class Microservice<ClusterMember> extends Application implements Startable, Stoppable
{
    /**
     * Command line switch for what port to run any REST service on. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> PORT =
            integerSwitchParser(this, "port", "The port to use")
                    .optional()
                    .build();

    /**
     * Command line switch for what port to run any GRPC service on. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> GRPC_PORT =
            integerSwitchParser(this, "grpc-port", "The port to use")
                    .optional()
                    .build();

    /**
     * Command line switch to output .proto files to the given folder
     */
    private final SwitchParser<Folder> PROTO_EXPORT_FOLDER =
            Folder.folderSwitchParser(this, "proto-export-folder", "The folder to which .proto files for request and response objects should be exported")
                    .optional()
                    .build();

    /** True if this microservice is running */
    private boolean running;

    /** Jetty web server */
    private JettyServer server;

    private final Lazy<MicroserviceGrpcService> grpcService = Lazy.of(this::onNewGrpcService);

    private final Lazy<WebApplication> webApplication = Lazy.of(this::onCreateWebApplication);

    private final Lazy<MicroserviceRestService> restService = Lazy.of(this::onNewRestService);

    private MicroserviceCluster<ClusterMember> cluster;

    /**
     * Initializes this microservice and any project(s) it depends on
     */
    public Microservice(Project... project)
    {
        super(project);
    }

    /**
     * The cluster where this microservice is running
     */
    public MicroserviceCluster<ClusterMember> cluster()
    {
        return cluster;
    }

    /**
     * @return A description of this microservice
     */
    @Override
    public String description()
    {
        return metadata().description();
    }

    public MicroserviceGrpcService grpcService()
    {
        return grpcService.get();
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
     * @return The Apache Wicket web application, if any, for configuring or viewing the status of this microservice.
     */
    public MicroserviceWebApplication onCreateWebApplication()
    {
        return null;
    }

    /**
     * Called to initialize the microservice before it's running. This is a good place to register objects with {@link
     * #register(Object)} or {@link #register(Object, Enum)}. While settings can be registered in this method, for most
     * microservices, it will make more sense to use the more automatic method provided by {@link Deployment}s.
     */
    public void onInitialize()
    {

    }

    /**
     * @return Any GRPC service for this microservice
     */
    public MicroserviceGrpcService onNewGrpcService()
    {
        return null;
    }

    /**
     * @return Any REST service for this microservice
     */
    public MicroserviceRestService onNewRestService()
    {
        return null;
    }

    public MicroserviceRestService restService()
    {
        return restService.get();
    }

    @UmlRelation(label = "has")
    public MicroserviceSettings settings()
    {
        return require(MicroserviceSettings.class);
    }

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

            // get any port overrides from the command line,
            if (has(PORT))
            {
                settings().port(get(PORT));
            }
            if (has(GRPC_PORT))
            {
                settings().grpcPort(get(GRPC_PORT));
            }

            // create the Jetty server.
            server = listenTo(new JettyServer().port(settings().port()));

            // If there's an Apache Wicket web application,
            var webApplication = webApplication();
            if (webApplication != null)
            {
                // mount them on the server.
                server.mount("/*", new WicketJettyFilterPlugin(webApplication));
            }

            // If there are static resources,
            var staticAssets = staticAssetsFolder();
            if (staticAssets != null)
            {
                // mount them on the server.
                server.mount("/assets/*", new AssetsJettyResourcePlugin(staticAssets));
            }

            // If there is a microservlet REST application,
            var restService = restService();
            if (restService != null)
            {
                // and there are static OpenAPI assets,
                listenTo(restService);
                var openApiAssets = openApiAssetsFolder();
                if (openApiAssets != null)
                {
                    // mount them on the server.
                    server.mount("/open-api/assets/*", new AssetsJettyResourcePlugin(openApiAssets));
                }

                // Mount Swagger resources for the REST application.
                server.mount("/*", register(new MicroservletJettyFilterPlugin(restService)));
                server.mount("/docs/*", new SwaggerIndexJettyResourcePlugin(settings().port()));
                server.mount("/swagger/webapp/*", new SwaggerAssetsJettyResourcePlugin());
                server.mount("/swagger/webjar/*", new SwaggerWebJarJettyResourcePlugin(restService.getClass()));

                // Initialize the REST service.
                restService.initialize();
            }

            // If there is a GRPC service,
            var grpcService = grpcService();
            if (grpcService != null)
            {
                // initialize it,
                listenTo(grpcService);
                grpcService.initialize();

                // write out any .proto files,
                if (has(PROTO_EXPORT_FOLDER))
                {
                    grpcService.writeProtoFilesTo(get(PROTO_EXPORT_FOLDER));
                }

                // and start it up.
                grpcService.start();
            }

            // If we don't have either a REST or a GRPC service,
            if (restService == null && grpcService == null)
            {
                // then warn about that.
                warning("No REST or GRPC service is available for this microservice");
            }

            onMountJettyPlugins(server);

            // Start Jetty server.
            announce("Microservice Jetty server starting on port ${integer}", settings().port());
            server.start();
            running = true;

            // and wait for it to be terminated.
            server.waitForTermination();
        }
        return true;
    }

    @Override
    public void stop(Duration wait)
    {
        server.stop(wait);
        grpcService().stop();
    }

    @Override
    public Version version()
    {
        return metadata().version();
    }

    public WebApplication webApplication()
    {
        return webApplication.get();
    }

    /**
     * @return The ClusterMember object for this microservice. The ClusterMember object holds information about cluster
     * members. When a member joins the cluster, the {@link #onJoin(Object)} method is called with the ClusterMember
     * object for the member. When a member leaves the cluster, the {@link #onLeave(Object)} is called with the same
     * object.
     */
    protected abstract ClusterMember onInitializeClusterMember();

    protected void onJoin(ClusterMember instance)
    {

    }

    protected void onLeave(ClusterMember instance)
    {
    }

    protected void onMountJettyPlugins(JettyServer server)
    {
    }

    /**
     * <b>Not public API</b>
     * <p>
     * This method should not be invoked or overridden. To initialize a microservice, override the {@link
     * #onInitialize()} method.
     * </p>
     */
    @Override
    @SuppressWarnings("unchecked")
    protected final void onRun()
    {
        // Get the ClusterMember object for this microservice's cluster,
        var member = onInitializeClusterMember();

        // register it in zookeeper,
        cluster = new MicroserviceCluster<>((Class<ClusterMember>) member.getClass())
        {
            @Override
            protected void onJoin(ClusterMember instance)
            {
                Microservice.this.onJoin(instance);
            }

            @Override
            protected void onLeave(ClusterMember instance)
            {
                Microservice.this.onLeave(instance);
            }
        };

        // and join the cluster.
        cluster.join(member);

        // Next, initialize this microservice,
        onInitialize();

        // register objects,
        register(new MicroservletGrpcSchemas(this));

        // then start it running,
        start();

        // and let clients know we're ready.
        ready();
    }

    /**
     * @return The resource folder containing static assets for reference by OpenAPI .yaml files and KivaKit OpenApi
     * annotations. For example, a microservice might want to include an OAS .yaml file. If this method is not
     * overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder openApiAssetsFolder()
    {
        return Package.packageFrom(this, restService().getClass(), "assets");
    }

    /**
     * @return The resource folder containing static assets. The resources will be mounted on <i>/assets</i>. If this
     * method is not overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder staticAssetsFolder()
    {
        return Package.packageFrom(this, getClass(), "assets");
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
        return ObjectSet.objectSet(PORT, GRPC_PORT, PROTO_EXPORT_FOLDER);
    }
}
