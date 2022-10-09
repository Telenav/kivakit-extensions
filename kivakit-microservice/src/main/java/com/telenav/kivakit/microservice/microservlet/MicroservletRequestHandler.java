package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Prepares {@link MicroservletResponse} when {@link #onRespond()} is called.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public interface MicroservletRequestHandler extends Listener
{
    /**
     * Called with request statistics for each request
     */
    void onMicroservletPerformance(MicroservletPerformance statistics);

    /**
     * Handles this request, producing a {@link MicroservletResponse}
     *
     * @return The response to this microservlet request
     */
    MicroservletResponse onRespond();

    /**
     * Produces a response to the current request
     *
     * @param path The request path
     * @return A response to a request at the given path
     */
    default MicroservletResponse respond(String path)
    {
        // Create request handling statistics object for our request,
        var statistics = new MicroservletPerformance();
        statistics.path(path);
        statistics.start();

        try
        {
            // get the response,
            var response = listenTo(onRespond());
            try
            {
                // prepare it for transmission,
                response.onPrepareResponse();

                // complete the response,
                response.onEndResponse();
            }
            catch (Exception e)
            {
                response.problem(e, "Error preparing response");
            }

            // and return it.
            return response;
        }
        finally
        {
            // Finish collecting request handling statistics.
            statistics.end();
            onMicroservletPerformance(statistics);
        }
    }
}
