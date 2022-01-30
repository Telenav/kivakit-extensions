package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.data.validation.ValidationType;
import com.telenav.kivakit.kernel.data.validation.Validator;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.microservice.project.lexakai.diagrams.DiagramMicroservlet;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Base class for all microservlet requests. A request implements both {@link MicroservletRequest} and {@link
 * MicroservletRequestHandler} because request objects handle themselves.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
public abstract class BaseMicroservletRequest extends BaseComponent implements
        MicroservletRequest,
        MicroservletRequestHandler
{
    private static final AtomicReference<MicroservletRequestStatisticsAggregator> aggregator = new AtomicReference<>();

    @Override
    public void onRequestHandlingStatistics(MicroservletRequestHandlingStatistics statistics)
    {
        announce("Request: $", statistics);
        aggregator.compareAndSet(null, new MicroservletRequestStatisticsAggregator(statisticsReportingFrequency()));
        aggregator.get().add(this, statistics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validator validator(ValidationType type)
    {
        return Validator.NULL;
    }

    protected Frequency statisticsReportingFrequency()
    {
        return Frequency.every(Duration.minutes(5));
    }
}
