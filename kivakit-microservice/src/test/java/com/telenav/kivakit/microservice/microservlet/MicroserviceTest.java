package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.MicroserviceSettings;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcClient;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestClient;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.serialization.json.DefaultGsonFactory;
import com.telenav.kivakit.serialization.json.GsonFactory;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MicroserviceTest extends UnitTest
{
    public static class TestGarbageRequest extends BaseMicroservletRequest
    {
        @SuppressWarnings("FieldCanBeLocal")
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
            return new TestResponse(-1);
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
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

    public static class TestGetRequest extends BaseMicroservletRequest
    {
        public TestGetRequest()
        {
        }

        @Override
        public MicroservletResponse onRespond()
        {
            return new TestResponse(42);
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }
    }

    public static class TestMicroservice extends Microservice<String>
    {
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
        public MicroserviceRestService onNewRestService()
        {
            return new TestRestMicroservice(this);
        }
    }

    public static class TestPostRequest extends BaseMicroservletRequest
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

        public TestPostRequest()
        {
        }

        @Override
        public MicroservletResponse onRespond()
        {
            return new TestResponse(a * b);
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }
    }

    public static class TestResponse extends BaseMicroservletResponse
    {
        @Expose
        int result;

        public TestResponse()
        {
        }

        public TestResponse(int result)
        {
            this.result = result;
        }
    }

    public static class TestRestMicroservice extends MicroserviceRestService
    {
        public TestRestMicroservice(Microservice<?> microservice)
        {
            super(microservice);
        }

        @Override
        public GsonFactory gsonFactory()
        {
            return new DefaultGsonFactory(this);
        }

        @Override
        public void onInitialize()
        {
            mount("test", HttpMethod.POST, TestPostRequest.class);
            mount("test", HttpMethod.GET, TestGetRequest.class);
            mount("garbage", HttpMethod.POST, TestGarbageRequest.class);
        }
    }

    @Test
    public void test()
    {
        Registry.of(this).register(new MicroserviceSettings()
                .port(8086)
                .grpcPort(8087)
                .server(false));

        var microservice = listenTo(new TestMicroservice());

        KivaKitThread.run(this, "Test", () -> microservice.run(new String[] { "-port=8086", "-grpc-port=8087",
                "-server=false" }));
        microservice.waitForReady();

        var client = listenTo(new MicroserviceRestClient(
                microservice.restService().gsonFactory(), Host.local().http(8086), microservice.version()));

        // Test POST with path parameters but no request object
        var response3 = client.post("test/a/9/b/3", TestResponse.class);
        ensureEqual(response3.result, 27);

        var garbageRequest = new TestGarbageRequest("This request is nonsense");

        var succeeded = false;
        try
        {
            client.post("garbage", TestResponse.class, garbageRequest);
            succeeded = true;
        }
        catch (Exception ignored)
        {
        }

        ensureFalse(succeeded);

        // Test POST
        var request = new TestPostRequest(7, 8);
        var response = client.post("test", TestResponse.class, request);
        ensureEqual(56, response.result);

        // Test GET
        var response2 = client.get("test", TestResponse.class);
        ensureEqual(42, response2.result);

        var grpcClient = listenTo(new MicroserviceGrpcClient(Host.local().port(8087), microservice.version()));
        var response5 = grpcClient.request("test", request, TestResponse.class);
        ensureEqual(56, response5.result);

        microservice.stop();
    }
}
