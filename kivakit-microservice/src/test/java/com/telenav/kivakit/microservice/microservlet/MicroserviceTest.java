package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.MicroserviceSettings;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcClient;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.http.RestClient;
import com.telenav.kivakit.microservice.protocols.rest.http.RestService;
import com.telenav.kivakit.microservice.protocols.rest.http.serializers.GsonRestClientSerializer;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import com.telenav.kivakit.testing.UnitTest;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.core.registry.Registry.registryFor;
import static com.telenav.kivakit.core.time.Duration.FOREVER;
import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.network.core.LocalHost.localhost;
import static com.telenav.kivakit.network.http.HttpMethod.GET;
import static com.telenav.kivakit.network.http.HttpMethod.POST;

@Ignore
public class MicroserviceTest extends UnitTest
{
    public static class TestGarbageRequest extends TestRequest
    {
        @SuppressWarnings("unused")
        @Expose
        private String trash;

        public TestGarbageRequest(String trash)
        {
            this.trash = trash;
        }

        public TestGarbageRequest()
        {
        }

        @Override
        public MicroservletResponse onRespond()
        {
            return null;
        }

        @Override
        public Validator validator(ValidationType type)
        {
            return new BaseValidator()
            {
                @Override
                protected void onValidate()
                {
                    problem("This request is garbage!");
                }
            };
        }
    }

    public static class TestGetRequest extends TestRequest
    {
        @Override
        public MicroservletResponse onRespond()
        {
            return listenTo(new TestResponse(42));
        }
    }

    public static class TestMicroservice extends Microservice<String>
    {
        @Override
        public Duration maximumStopTime()
        {
            return FOREVER;
        }

        @Override
        public MicroserviceMetadata metadata()
        {
            return new MicroserviceMetadata()
                .withName("Test")
                .withDescription("This is a test REST application")
                .withVersion(Version.parseVersion(this, "0.9"));
        }

        @Override
        public MicroserviceGrpcService onNewGrpcService()
        {
            return new MicroserviceGrpcService(this);
        }

        @Override
        public RestService onNewRestService()
        {
            return new TestRest(this);
        }
    }

    public static class TestPostRequest extends TestRequest
    {
        @Expose
        int a;

        @Expose
        int b;

        public TestPostRequest(int a, int b)
        {
            this.a = a;
            this.b = b;
        }

        @SuppressWarnings("unused")
        public TestPostRequest()
        {
        }

        @Override
        public MicroservletResponse onRespond()
        {
            return listenTo(new TestResponse(a * b));
        }
    }

    public static class TestResponse extends BaseMicroservletResponse
    {
        @Expose
        int result;

        public TestResponse(int result)
        {
            this.result = result;
        }
    }

    public static class TestRest extends RestService
    {
        public TestRest(Microservice<?> microservice)
        {
            super(microservice);
        }

        @Override
        public Version apiVersion()
        {
            return version("1.0");
        }

        @Override
        public void onInitialize()
        {
            mount("test", POST, TestPostRequest.class);
            mount("test", GET, TestGetRequest.class);
            mount("garbage", POST, TestGarbageRequest.class);
        }
    }

    static abstract class TestRequest extends BaseMicroservletRequest
    {
        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }
    }

    @Test
    public void test()
    {
        register(new KivaKitCoreGsonFactory());

        registryFor(this).register(new MicroserviceSettings()
            .port(8086)
            .grpcPort(8087)
            .server(false));

        var microservice = listenTo(new TestMicroservice());

        KivaKitThread.run(this, "Test", () ->
            microservice.run(new String[] { "-port=8086", "-grpc-port=8087", "-server=false" }));

        microservice.waitForReady();

        var client = listenTo(new RestClient<TestRequest, TestResponse>(
            new GsonRestClientSerializer<>(), localhost().http(8086), microservice.version()));

        // Test POST with path parameters but no request object
        var response3 = client.post("test/a/9/b/3", TestResponse.class);
        ensureEqual(response3.result, 27);

        var garbageRequest = new TestGarbageRequest("This request is nonsense");

        var succeeded = false;
        try
        {
            client.post("garbage", garbageRequest, TestResponse.class);
            succeeded = true;
        }
        catch (Exception ignored)
        {
        }

        ensureFalse(succeeded);

        // Test POST
        var request = new TestPostRequest(7, 8);
        var response = client.post("test", request, TestResponse.class);
        ensureEqual(56, response.result);

        // Test GET
        var response2 = client.get("test", TestResponse.class);
        ensureEqual(42, response2.result);

        var grpcClient = listenTo(new MicroserviceGrpcClient(localhost().port(8087), microservice.version()));
        var response5 = grpcClient.request("test", request, TestResponse.class);
        ensureEqual(56, response5.result);

        microservice.stop(FOREVER);
    }
}
