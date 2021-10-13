package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.kernel.data.validation.BaseValidator;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestClient;
import com.telenav.kivakit.microservice.protocols.rest.MicroserviceRestService;
import com.telenav.kivakit.microservice.protocols.rest.gson.MicroserviceGsonFactory;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

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
        public Validator validator(final ValidationType type)
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
        public MicroserviceRestService restService()
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

        public TestPostRequest(final int a, final int b)
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

        public TestResponse(final int result)
        {
            this.result = result;
        }
    }

    public static class TestRestService extends MicroserviceRestService
    {
        public TestRestService(final Microservice microservice)
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
        final var microservice = listenTo(new TestMicroservice());
        KivaKitThread.run(this, "Test", () -> microservice.run(new String[] { "-port=8086" }));
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

        microservice.stop();
    }
}
