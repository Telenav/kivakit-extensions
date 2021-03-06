package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.thread.KivaKitThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.MicroserviceSettings;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcClient;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestClient;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.serialization.gson.GsonObjectSerializer;
import com.telenav.kivakit.serialization.gson.factory.CoreGsonFactory;
import com.telenav.kivakit.testing.UnitTest;
import com.telenav.kivakit.validation.BaseValidator;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MicroserviceTest extends UnitTest
{
    @SuppressWarnings("unused")
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
            return listenTo(new TestResponse(-1));
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
            return listenTo(new TestResponse(42));
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
        public Duration maximumWaitTime()
        {
            return Duration.MAXIMUM;
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
        public MicroserviceRestService onNewRestService()
        {
            return new TestRestMicroservice(this);
        }
    }

    public static class TestPostRequest extends BaseMicroservletRequest
    {
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
            return listenTo(new TestResponse(a * b));
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }

        @Expose
        int a;

        @Expose
        int b;
    }

    public static class TestResponse extends BaseMicroservletResponse
    {
        public TestResponse(int result)
        {
            this.result = result;
        }

        @Expose
        int result;
    }

    public static class TestRestMicroservice extends MicroserviceRestService
    {
        public TestRestMicroservice(Microservice<?> microservice)
        {
            super(microservice);
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
        register(new CoreGsonFactory(this));

        var serializers = new ObjectSerializers();
        serializers.add(Extension.JSON, new GsonObjectSerializer());
        register(serializers);

        Registry.of(this).register(new MicroserviceSettings()
                .port(8086)
                .grpcPort(8087)
                .server(false));

        var microservice = listenTo(new TestMicroservice());

        KivaKitThread.run(this, "Test", () -> microservice.run(new String[] { "-port=8086", "-grpc-port=8087",
                "-server=false" }));
        microservice.waitForReady();

        var client = listenTo(new MicroserviceRestClient(
                new GsonObjectSerializer(), Host.local().http(8086), microservice.version()));

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

        microservice.stop(Duration.MAXIMUM);
    }
}
