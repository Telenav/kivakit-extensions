package com.telenav.kivakit.aws.lambda;

import com.google.gson.Gson;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.strings.CaseFormat;
import com.telenav.kivakit.kernel.logging.logs.text.ConsoleLog;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;

import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static software.amazon.awssdk.services.lambda.model.InvocationType.REQUEST_RESPONSE;

/**
 * Base class describing a lambda function, allowing it to be invoked through the Java {@link Function} interface with
 * {@link Function#apply(Object)}. A subclass of this class providing a lambda function description can be passed by a
 * subclass of {@link LambdaCode} to the constructor {@link LambdaCode#LambdaCode(Lambda)} when implementing the code
 * for the lambda function. See the example below.
 *
 * <p><b>Example</b></p>
 * <p><i>Base class for all lambda functions in my project:</i></p>
 * <br/>
 * <pre>
 * abstract class MyBaseLambda&lt;Request, Response&gt;
 *     extends Lambda&lt;Request, Response&gt;
 * {
 *     protected MyBaseLambda(Class&lt;Request&gt; request, Class&lt;Response&gt; response)
 *     {
 *         super(service(), request, response);
 *     }
 *
 *     private LambdaService service()
 *     {
 *         return LambdaService.create()
 *             .withMachineImage(imageUri)
 *             .withCodeBucket("my-lambda-bucket")
 *             .withCodeSigningArn(arn)
 *             .withGsonFactory(new MyApplicationGsonFactory());
 *     }
 * }
 * </pre>
 * <p><i>For each lambda in my project:</i></p>
 * <br/>
 * <pre>
 * class MyLambda extends MyBaseLambda&lt;MyRequest, MyResponse&gt;
 * {
 *     public MyLambda()
 *     {
 *         super(MyRequest.class, MyResponse.class);
 *     }
 * }
 *
 * class MyLambdaCode extends LambdaCode&lt;MyRequest, MyResponse&gt;
 * {
 *     public MyLambdaCode()
 *     {
 *         super(new MyLambda());
 *     }
 *
 *     public void onRun()
 *     {
 *         [...]
 *     }
 *
 *     public static void main(String[] arguments)
 *     {
 *         // Publish lambda function when invoked from build script
 *         new MyLambdaFunction().publish(new File(arguments[0]));
 *     }
 * }
 *
 * // Apply lambda function
 * MyRequest request;
 * var result = new MyLambda()
 *     .withRole("my-role")
 *     .apply(request);
 * </pre>
 *
 * @param <Request> The request object type
 * @param <Response> The response object type
 * @author jonathanl (shibo)
 */
public class Lambda<Request, Response> extends BaseRepeater implements Named, Function<Request, Response>
{
    /** The service that created this lambda function */
    private final LambdaService service;

    /** The name of this lambda function */
    private final String name;

    /** The type of the request object */
    private final Class<Request> requestType;

    /** The type of the response object */
    private final Class<Response> responseType;

    /** The Gson JSON codec */
    private final Gson gson;

    protected Lambda(final LambdaService service,
                     final Class<Request> requestType,
                     final Class<Response> responseType)
    {
        this.service = service;
        this.name = CaseFormat.hyphenatedName(getClass());
        this.requestType = requestType;
        this.responseType = responseType;
        this.gson = service.gson();

        ConsoleLog.asynchronous(false);
    }

    protected Lambda(final Lambda<Request, Response> that)
    {
        this.service = that.service;
        this.name = that.name;
        this.requestType = that.requestType;
        this.responseType = that.responseType;
        this.gson = that.gson;
    }

    @Override
    public Response apply(final Request request)
    {
        final InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(name)
                .invocationType(REQUEST_RESPONSE)
                .payload(SdkBytes.fromString(toRequestJson(request), UTF_8))
                .build();

        final var response = service.client().invoke(invokeRequest);
        if (response.sdkHttpResponse().isSuccessful())
        {
            final var json = response.payload().asUtf8String();
            return toResponse(json);
        }
        warning("Unable to invoke lambda $", this);
        return null;
    }

    @Override
    public String name()
    {
        return name;
    }

    public Class<Request> requestType()
    {
        return requestType;
    }

    public LambdaService service()
    {
        return service;
    }

    @Override
    public String toString()
    {
        return Message.format("${class} $(${class})", name, requestType, responseType);
    }

    Request toRequest(final String json)
    {
        return gson.fromJson(json, requestType);
    }

    String toRequestJson(final Request request)
    {
        return gson.toJson(request);
    }

    Response toResponse(final String json)
    {
        return gson.fromJson(json, responseType);
    }

    String toResponseJson(final Response response)
    {
        return gson.toJson(response);
    }
}
