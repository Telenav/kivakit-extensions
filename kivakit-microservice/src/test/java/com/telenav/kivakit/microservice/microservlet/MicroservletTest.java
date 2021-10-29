package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.kernel.data.validation.BaseValidator;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.MicroserviceSettings;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcClient;
import com.telenav.kivakit.microservice.protocols.grpc.MicroserviceGrpcService;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestClient;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonFactory;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

// TODO shibo
// This test is not exiting cleanly due to some aspect of shutdown
// If the shutdown can't be fixed, some workaround should be found with JUnit
@Ignore
public class MicroservletTest extends UnitTest
{
    public static class TestGarbageRequest extends BaseMicroservletRequest
    {
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
        public MicroservletResponse onRequest()
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
        public MicroservletResponse onRequest()
        {
            return new TestResponse(42);
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }
    }

    public static class TestMicroservice extends Microservice
    {
        @Override
        public MicroserviceMetadata metadata()
        {
            return new MicroserviceMetadata()
                    .withName("Test")
                    .withDescription("This is a test REST application")
                    .withVersion(Version.parse("0.9"));
        }

        @Override
        public MicroserviceGrpcService onNewGrpcService()
        {
            return new MicroserviceGrpcService(this);
        }

        @Override
        public MicroserviceRestService onNewRestService()
        {
            return new TestRestService(this);
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
        public MicroservletResponse onRequest()
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

    public static class TestRestService extends MicroserviceRestService
    {
        public TestRestService(Microservice microservice)
        {
            super(microservice);
        }

        @Override
        public MicroserviceGsonFactory gsonFactory()
        {
            return new MicroserviceGsonFactory();
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
        Registry.of(this).register(new MicroserviceSettings().port(8086).grpcPort(8087));

        var microservice = listenTo(new TestMicroservice());

        KivaKitThread.run(this, "Test", () -> microservice.run(new String[] { "-port=8086", "-grpc-port=8087" }));
        microservice.waitForReady();

        var client = listenTo(new MicroserviceRestClient(
                microservice.restService().gsonFactory(), Host.local().http(8086), microservice.version()));

        var garbageRequest = new TestGarbageRequest("This request is nonsense");
        var response4 = client.post("garbage", TestResponse.class, garbageRequest);
        ensureNull(response4);

        // Test POST
        var request = new TestPostRequest(7, 8);
        var response = client.post("test", TestResponse.class, request);
        ensureEqual(56, response.result);

        // Test GET
        var response2 = client.get("test", TestResponse.class);
        ensureEqual(42, response2.result);

        // Test POST with path parameters but no request object
        var response3 = client.post("test/a/9/b/3", TestResponse.class);
        ensureEqual(27, response3.result);

        var grpcClient = listenTo(new MicroserviceGrpcClient(Host.local().port(8087), microservice.version()));
        var response5 = grpcClient.request("test", request, TestResponse.class);
        ensureEqual(56, response5.result);

        microservice.stop();
    }
}
