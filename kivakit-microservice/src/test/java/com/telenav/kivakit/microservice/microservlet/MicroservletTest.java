package com.telenav.kivakit.microservice.microservlet;

import com.google.gson.annotations.Expose;
import com.telenav.kivakit.kernel.data.validation.BaseValidator;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.threading.KivaKitThread;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.MicroserviceMetadata;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplicationGsonFactory;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletClient;
import com.telenav.kivakit.microservice.rest.microservlet.MicroservletResponse;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletGetRequest;
import com.telenav.kivakit.microservice.rest.microservlet.requests.MicroservletPostRequest;
import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class MicroservletTest extends UnitTest
{
    public static class TestGarbageRequest extends MicroservletPostRequest
    {
        private String trash;

        public TestGarbageRequest(String trash)
        {
        }

        public TestGarbageRequest()
        {
        }

        @Override
        public MicroservletResponse onPost()
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

    public static class TestGetRequest extends MicroservletGetRequest
    {
        public TestGetRequest()
        {
        }

        @Override
        public MicroservletResponse onGet()
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
        public MicroserviceRestApplication restApplication()
        {
            return new TestRestApplication(this);
        }
    }

    public static class TestPostRequest extends MicroservletPostRequest
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
        public MicroservletResponse onPost()
        {
            return new TestResponse(a * b);
        }

        @Override
        public Class<? extends MicroservletResponse> responseType()
        {
            return TestResponse.class;
        }
    }

    public static class TestResponse extends MicroservletResponse
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

    public static class TestRestApplication extends MicroserviceRestApplication
    {
        public TestRestApplication(final Microservice microservice)
        {
            super(microservice);
        }

        @Override
        public MicroserviceRestApplicationGsonFactory gsonFactory()
        {
            return new MicroserviceRestApplicationGsonFactory();
        }

        @Override
        public void onInitialize()
        {
            mount("test", TestPostRequest.class);
            mount("test", TestGetRequest.class);
        }
    }

    @Test
    public void test()
    {
        final var microservice = listenTo(new TestMicroservice());
        KivaKitThread.run(this, "Test", () -> microservice.run(new String[] { "-port=8086" }));
        microservice.waitForReady();
        var client = listenTo(new MicroservletClient(
                microservice.restApplication().gsonFactory(), Host.local().http(8086), microservice.version()));

        var garbageRequest = new TestGarbageRequest("This request is nonsense");
        var response4 = client.post("test", TestResponse.class, garbageRequest);
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
    }
}
