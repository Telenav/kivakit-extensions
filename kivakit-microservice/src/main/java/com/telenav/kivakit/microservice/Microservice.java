package com.telenav.kivakit.microservice;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.commandline.Switch;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.commandline.SwitchParsers;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.language.Arrays;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.microservice.internal.protocols.grpc.MicroservletGrpcSchemas;
import com.telenav.kivakit.microservice.internal.protocols.rest.plugins.jetty.MicroservletJettyPlugin;
import com.telenav.kivakit.microservice.project.lexakai.DiagramMicroservice;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.lambda.MicroserviceLambdaService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.web.MicroserviceWebApplication;
import com.telenav.kivakit.resource.Package;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.serialization.gson.DefaultGsonFactory;
import com.telenav.kivakit.serialization.gson.GsonFactory;
import com.telenav.kivakit.settings.Deployment;
import com.telenav.kivakit.settings.stores.zookeeper.ZookeeperConnection;
import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.AssetsJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebAppJettyPlugin;
import com.telenav.kivakit.web.swagger.SwaggerWebJarJettyPlugin;
import com.telenav.kivakit.web.wicket.WicketJettyPlugin;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.apache.wicket.protocol.http.WebApplication;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Collection;
import java.util.regex.Pattern;

import static com.telenav.kivakit.commandline.SwitchParsers.booleanSwitchParser;
import static com.telenav.kivakit.commandline.SwitchParsers.integerSwitchParser;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

/**
 * <p>
 * Base class for KivaKit <a href="https://martinfowler.com/articles/microservices.html">microservices</a>.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Creating a Microservice</b></p>
 *
 * <p>
 * A microservice extending this base class needs to:
 * </p>
 *
 * <ol>
 *     <li>Create the {@link Microservice} subclass in the application's main(String[]) method</li>
 *     <li>Call {@link #run(String[])}, passing in the arguments to main()</li>
 *     <li>Optionally, pass any {@link Project} class to the constructor to ensure dependent projects are initialized</li>
 *     <li>Override {@link #onNewRestService()} to provide the microservice's {@link MicroserviceRestService} subclass</li>
 *     <li>Override {@link #metadata()} to provide {@link MicroserviceMetadata} used in the REST OpenAPI specification at /open-api/swagger.json</li>
 *     <li>Optionally, override {@link #onNewWebApplication()} to provide an Apache Wicket {@link MicroserviceWebApplication} subclass</li>
 * </ol>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Example:</b></p>
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
 *     public MicroserviceMetaData metadata()
 *     {
 *         return new MicroserviceMetadata()
 *             .withName("my-microservice")
 *             .withDescription("My microservice!")
 *             .withVersion(Version.of(1, 0));
 *     }
 *
 *     &#064;Override
 *     public MyRestService onNewRestService()
 *     {
 *         return new MyRestService(this);
 *     }
 *
 *     &#064;Override
 *     public MyWebApplication onNewWebApplication()
 *     {
 *         return new MyWebApplication(this);
 *     }
 * }</pre>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Command Line Switches</b></p>
 *
 * <p>
 * KivaKit will parse the command line and start the microservice on any port passed to the command line with
 * <i>-port=[port]</i>. If no port is specified, the port in {@link MicroserviceSettings} will be used, as loaded
 * from the {@link Deployment} specified on the command line with <i>-deployment=[deployment]</i>.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Mount Paths</b></p>
 * <p>
 * Microservice resources are mounted on the following paths by default:
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
 *         <td>/docs</td><td>&nbsp;</td><td>Swagger documentation</td>
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
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Root Path</b></p>
 *
 * <p>The method {@link #rootPath()} returns <i>/</i> (slash) by default. This method can be overridden to put all resources
 * under a different root. For example, version 1.0 of the REST API for <i>my-microservice</i> would be mounted under
 * <i>/api/1.0</i>. But if {@link #rootPath()} returned <i>/[microservice-name]</i>, the API root for version
 * 1.0 would be <i>/my-microservice/api/1.0</i>.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Cluster Membership</b></p>
 *
 * <p>
 * When a microservice starts up, it automatically joins a cluster of similar microservices. The {@link MicroserviceCluster}
 * retrieved with the {@link #cluster()} method models the cluster and is organized through Apache Zookeeper. When members
 * join and leave the cluster the {@link #onJoin(MicroserviceClusterMember)} and {@link #onLeave(MicroserviceClusterMember)}
 * methods will be called. The set of members of the cluster can be retrieved with {@link MicroserviceCluster#members()},
 * and the leader with {@link MicroserviceCluster#leader()}.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Cluster Elections</b></p>
 *
 * <p>
 * Any time a member joins or leaves, an election is held by the {@link MicroserviceCluster}. If
 * this microservice is the elected leader of the cluster, the {@link #isLeader()} method will return true.
 * </p>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see Switch
 * @see ResourceFolder
 * @see MicroserviceMetadata
 * @see MicroserviceSettings
 * @see MicroserviceRestService
 * @see <a href="https://martinfowler.com/articles/microservices.html">Martin Fowler on Microservices</a>
 * @see MicroserviceCluster
 * @see MicroserviceClusterMember
 */
@UmlClassDiagram(diagram = DiagramMicroservice.class)
public abstract class Microservice<Member> extends Application implements
        Startable,
        Stoppable
{
    /**
     * Command line switch for what port to run any REST service on. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<String> API_FORWARDING =
            SwitchParsers.stringSwitchParser(this, "api-forwarding", "A semicolon-separated list of APIs to forward to, each of the form:\n                                      version=[version],jar=[resource],port=[port-number],(command-line=[command-line])?\n\n    For example:\n\n        -api-forwarding=version=0.9,jar=classpath:/apis/my-microservice-0.9.jar,port=8082,command-line=-deployment=development\n")
                    .optional()
                    .build();

    /**
     * Command line switch for what port to run any GRPC service on. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> GRPC_PORT =
            integerSwitchParser(this, "grpc-port", "The port to use for gRPC")
                    .optional()
                    .build();

    /**
     * Command line switch for what port to run any REST service on. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Integer> PORT =
            integerSwitchParser(this, "port", "The port to use for REST")
                    .optional()
                    .build();

    /**
     * Command line switch to output .proto files to the given folder
     */
    private final SwitchParser<Folder> PROTO_EXPORT_FOLDER =
            Folder.folderSwitchParser(this, "proto-export-folder", "The folder to which .proto files for request and response objects should be exported")
                    .optional()
                    .build();

    /**
     * Command line switch to run this microservice as a blocking server. This will override any value from {@link
     * MicroserviceSettings} that is loaded from a {@link Deployment}.
     */
    private final SwitchParser<Boolean> SERVER =
            booleanSwitchParser(this, "server", "True to run this microservice as a server")
                    .optional()
                    .build();

    private MicroserviceCluster<Member> cluster;

    private final Lazy<MicroserviceGrpcService> grpcService = Lazy.of(this::onNewGrpcService);

    private final Lazy<MicroserviceLambdaService> lambdaService = Lazy.of(this::onNewLambdaService);

    private final Lazy<MicroserviceRestService> restService = Lazy.of(this::onNewRestService);

    /** True if this microservice is running */
    private boolean running;

    /** Jetty web server */
    private JettyServer server;

    private final Lazy<WebApplication> webApplication = Lazy.of(this::onNewWebApplication);

    /**
     * Initializes this microservice and any project(s) it depends on
     */
    public Microservice(Project... project)
    {
        super(Arrays.concatenate(project, new Project[] { new MicroserviceProject() }));

        register(gsonFactory());
    }

    /**
     * The cluster where this microservice is running
     */
    public MicroserviceCluster<Member> cluster()
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
     * @return The {@link GsonFactory} factory for this microservice
     */
    public GsonFactory gsonFactory()
    {
        return new DefaultGsonFactory(this);
    }

    /**
     * @return True if this microservice is clustered. To be clustered, two things are required:
     * <ol>
     *     <li>{@link #onNewMember()} must be overridden to return a value</li>
     *     <li>A configuration for {@link ZookeeperConnection} must be available (normally in the <i>deployments</i>
     *     package) next to the application</li>
     * </ol>
     */
    public boolean isClustered()
    {
        return cluster != null;
    }

    /**
     * @return True if this microservice is the leader of the cluster
     */
    public boolean isLeader()
    {
        ensure(isClustered());

        return cluster.thisMember().isLeader();
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

    public MicroserviceLambdaService lambdaService()
    {
        return lambdaService.get();
    }

    /**
     * @return The leader of this cluster
     */
    public MicroserviceClusterMember<Member> leader()
    {
        ensure(isClustered());

        return cluster.leader();
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
     * @return Any GRPC service for this microservice
     */
    public MicroserviceGrpcService onNewGrpcService()
    {
        return null;
    }

    /**
     * @return Any AWS Lambda service for this microservice
     */
    public MicroserviceLambdaService onNewLambdaService()
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

    /**
     * @return The Apache Wicket web application, if any, for configuring or viewing the status of this microservice.
     */
    public MicroserviceWebApplication onNewWebApplication()
    {
        return null;
    }

    /**
     * Returns the full path to the given mount path
     *
     * @param mountPath The relative path on which something is mounted
     */
    public String resolvePath(String mountPath)
    {
        return Paths.concatenate(rootPath(), mountPath);
    }

    /**
     * Returns the rest service for this microservice
     */
    public MicroserviceRestService restService()
    {
        return restService.get();
    }

    /**
     * The root of this microservice. By default, this is <i>/[microservice-name]</i>, like <i>/my-microservice</i>
     */
    public String rootPath()
    {
        return "/";
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
            if (has(SERVER))
            {
                settings().server(get(SERVER));
            }

            // create the Jetty server.
            server = listenTo(new JettyServer(rootPath()).port(settings().port()));

            // If there's an Apache Wicket web application,
            var webApplication = webApplication();
            if (webApplication != null)
            {
                // mount them on the server.
                server.mount("/*", new WicketJettyPlugin(webApplication));
            }

            // If there are static resources,
            var staticAssets = staticAssetsFolder();
            if (staticAssets != null)
            {
                // mount them on the server.
                server.mount("/assets/*", new AssetsJettyPlugin(staticAssets));
            }

            // If there is a microservlet REST application,
            var restService = restService();
            if (restService != null)
            {
                // mount the (filter) plugin for it,
                server.mount("/*", register(new MicroservletJettyPlugin(restService)));

                // and if there are any OpenAPI assets,
                listenTo(restService);
                var openApiAssets = openApiAssetsFolder();
                if (openApiAssets != null)
                {
                    // mount them.
                    mountOpenApiAssets("/docs", openApiAssets);
                    mountOpenApiAssets("/api/" + version() + "/docs", openApiAssets);
                }

                // If there are any previous APIs specified by the -api-forwarding switch,
                if (has(API_FORWARDING))
                {
                    // mount the specified API JAR files.
                    mountApis(restService);
                }

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

            // If there is a Lambda service,
            var lambdaService = lambdaService();
            if (lambdaService != null)
            {
                // initialize it.
                lambdaService.initialize();
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
            ready();

            // and wait for it to be terminated.
            if (settings().isServer())
            {
                server.waitForTermination();
            }
        }
        return true;
    }

    @Override
    public void stop(LengthOfTime wait)
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

    protected void onJoin(MicroserviceClusterMember<Member> member)
    {
    }

    protected void onLeave(MicroserviceClusterMember<Member> member)
    {
    }

    protected void onMountJettyPlugins(JettyServer server)
    {
    }

    /**
     * @return The ClusterMember object for this microservice. The ClusterMember object holds information about cluster
     * members. When a member joins the cluster, the {@link #onJoin(MicroserviceClusterMember)} method is called with
     * the ClusterMember object for the member. When a member leaves the cluster, the {@link
     * #onLeave(MicroserviceClusterMember)} is called with the same object.
     */
    protected MicroserviceClusterMember<Member> onNewMember()
    {
        // By default, microservices are not clustered, so this returns null
        return null;
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
        // Get the Member object for this microservice's cluster,
        var member = onNewMember();

        // create cluster instance,
        if (member != null && lookup(ZookeeperConnection.Settings.class) != null)
        {
            try
            {
                var outer = this;
                cluster = listenTo(new MicroserviceCluster<>()
                {
                    @Override
                    protected void onJoin(MicroserviceClusterMember<Member> member)
                    {
                        outer.onJoin(member);
                    }

                    @Override
                    protected void onLeave(MicroserviceClusterMember<Member> instance)
                    {
                        outer.onLeave(instance);
                    }
                });

                // and join the cluster.
                cluster.join(member);
            }
            catch (Exception e)
            {
                warning("Unable to join microservice cluster");
            }
        }

        // Next, initialize this microservice,
        tryCatch(this::onInitialize, "Initialization failed");

        // register objects,
        register(new MicroservletGrpcSchemas(this));

        // then start our microservice running.
        tryCatch(this::start, "Microservice startup failed");
    }

    @Override
    @MustBeInvokedByOverriders
    protected void onRunning()
    {
        register(gsonFactory());
    }

    /**
     * @return The resource folder containing static assets for reference by OpenAPI .yaml files and KivaKit OpenApi
     * annotations. For example, a microservice might want to include an OAS .yaml file. If this method is not
     * overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder openApiAssetsFolder()
    {
        var type = ensureNotNull(Type.forName("com.telenav.kivakit.web.swagger.SwaggerJettyPlugin"));
        return Package.packageFrom(this, type.type(), "assets/openapi");
    }

    /**
     * @return The resource folder containing static assets. The resources will be mounted on <i>/assets</i>. If this
     * method is not overridden, the default folder will be the "assets" sub-package of the rest application's package.
     */
    protected ResourceFolder staticAssetsFolder()
    {
        return Package.packageFrom(this, restService().getClass(), "assets");
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
        return ObjectSet.objectSet(PORT, GRPC_PORT, PROTO_EXPORT_FOLDER, SERVER, API_FORWARDING);
    }

    /**
     * Mounts the API versions specified by the <i>-api-forwarding</i> switch
     *
     * @param restService The REST service to mount each API version JAR on
     */
    private void mountApis(final MicroserviceRestService restService)
    {
        var apis = get(API_FORWARDING).split(";");
        var apiSpecifier = Pattern.compile("version=(<?version>\\d+\\.\\d+),jar=(<?jar>[^,]+),port=(<?port>\\d+)(,command-line=(<?commandLine>.*))?");

        var okay = true;

        for (var api : apis)
        {
            var matcher = apiSpecifier.matcher(api);
            if (matcher.matches())
            {
                var version = Version.parseVersion(this, matcher.group("version"));
                var resource = Resource.resolve(this, matcher.group("jar"));
                var commandLine = matcher.group("commandLine");
                var port = Ints.parseInt(this, matcher.group("port"));

                restService.mountApi(version, resource, commandLine, port);
            }
            else
            {
                okay = false;
            }
        }

        if (!okay)
        {
            exit("Invalid -api-forwarding switch value: $", get(API_FORWARDING));
        }
    }

    private void mountOpenApiAssets(String path, final ResourceFolder openApiAssets)
    {
        server.mount(path, new SwaggerJettyPlugin(openApiAssets, settings().port()));
        server.mount(path + "/assets/openapi/*", new AssetsJettyPlugin(openApiAssets));
        server.mount(path + "/assets/swagger/webapp/*", new SwaggerWebAppJettyPlugin());
        server.mount(path + "/assets/swagger/webjar/*", new SwaggerWebJarJettyPlugin());
    }
}
