package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Prepares {@link MicroservletResponse} when {@link #onRespond()} is called.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletRequestHandler extends Listener
{
    /**
     * Called with request statistics for each request
     */
    void onRequestHandlingStatistics(MicroservletRequestHandlingStatistics statistics);

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
        var statistics = new MicroservletRequestHandlingStatistics();
        statistics.path(path);
        statistics.start();

        try
        {
            // get the response,
            var response = listenTo(onRespond());
            try
            {
                // prepare it for transmission,
                response.prepare();
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
            onRequestHandlingStatistics(statistics);
        }
    }
}
