package com.telenav.kivakit.microservice.protocols.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.microservice.Microservice;
import com.telenav.kivakit.microservice.microservlet.MicroservletRequest;
import com.telenav.kivakit.serialization.gson.GsonFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.io.IO.buffer;
import static com.telenav.kivakit.core.io.IO.readString;
import static com.telenav.kivakit.network.http.HttpStatus.OK;

/**
 * <p>
 * The KivaKit generic Amazon Web Service (AWS) Lambda request handler.
 * </p>
 *
 * <p><b>Configuration</b></p>
 *
 * <p>
 * This request handler should be specified as follows when installing a {@link Microservice} Lambda jar:
 * </p>
 *
 * <pre>com.telenav.kivakit.microservice.protocols.lambda.LambdaRequestHandler::handleRequest</pre>
 *
 * <p><b>Security</b></p>
 *
 * <p>
 * This request handler can handle any {@link MicroservletRequest} once it has been exposed by a call to
 * {@link MicroserviceLambdaService#mountLambdaRequestHandler(String, Version, Class)}. Only explicitly exposed Lambda methods can be
 * executed Lambda {@link RequestStreamHandler}.
 * </p>
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * Note that the KivaKit logging methods are redirected to the AWS logger provided by {@link Context#getLogger()}. This
 * allows existing error reporting in request handlers to be seen on AWS.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see LambdaFunction
 * @see MicroserviceLambdaService
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class LambdaRequestHandler implements RequestStreamHandler, ComponentMixin
{
    /**
     * Implementation of Lambda request handler
     *
     * @param in The input
     * @param out The output
     * @param context Information about the request context
     */
    @Override
    public void handleRequest(InputStream in, OutputStream out, Context context)
    {
        // Create a print writer to write the response to,
        try (var print = new PrintWriter(buffer(out)))
        {
            // then get the requested Lambda function,
            var lambda = new LambdaFunction(context);

            // and its request handler type (if it has been exposed),
            var requestType = require(MicroserviceLambdaService.class).requestType(lambda);
            if (requestType != null)
            {
                // read the JSON for the request,
                var json = readString(this, in);

                // get a Gson and deserialize the JSON into a request object,
                var gson = require(GsonFactory.class).gson();
                var request = gson.fromJson(json, requestType);

                // add a listener to the request so that any status messages go to the AWS logger,
                request.addListener(message -> context.getLogger().log(message.asString()));

                // then process the request,
                var response = request.onRespond();

                // and return the JSON response.
                json = gson.toJson(request.responseType());
                response.restResponse().httpStatus(OK);
                print.println(json);
            }
            else
            {
                // There is no handler for this Lambda
                print.println("No request handler found for: " + lambda);
            }
        }
    }
}
