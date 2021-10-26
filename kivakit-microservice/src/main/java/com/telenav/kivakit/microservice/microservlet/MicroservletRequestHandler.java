package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Creates a {@link MicroservletResponse} when {@link #onRequest()} is called.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public interface MicroservletRequestHandler
{
    /**
     * @return The response to this microservlet request
     */
    MicroservletResponse onRequest();

    /**
     * Called with request statistics for each request
     */
    void onRequestStatistics(MicroservletRequestStatistics statistics);

    default MicroservletResponse request(String path)
    {
        var statistics = new MicroservletRequestStatistics();
        statistics.path(path);
        try
        {
            statistics.start();
            return onRequest();
        }
        finally
        {
            statistics.end();
            onRequestStatistics(statistics);
        }
    }
}
