package com.telenav.kivakit.microservice.microservlet;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.microservice.internal.lexakai.DiagramMicroservlet;
import com.telenav.kivakit.validation.ValidationType;
import com.telenav.kivakit.validation.Validator;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicReference;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.time.Duration.minutes;
import static com.telenav.kivakit.core.time.Frequency.every;
import static com.telenav.kivakit.validation.Validator.nullValidator;

/**
 * Base class for all microservlet requests. A request implements both {@link MicroservletRequest} and
 * {@link MicroservletRequestHandler} because request objects handle themselves.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMicroservlet.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseMicroservletRequest extends BaseComponent implements
        MicroservletRequest,
        MicroservletRequestHandler
{
    /** The performance statistics aggregator */
    private static final AtomicReference<MicroservletPerformanceStatistics> aggregator = new AtomicReference<>();

    @Override
    public void onMicroservletPerformance(MicroservletPerformance performance)
    {
        announce("Request: $", performance);

        // Initialize the aggregator if not already
        aggregator.compareAndSet(null,
                new MicroservletPerformanceStatistics(statisticsReportingFrequency()));

        // then add the performance information to the aggregator
        aggregator.get().add(this, performance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Validator validator(ValidationType type)
    {
        return nullValidator();
    }

    protected Frequency statisticsReportingFrequency()
    {
        return every(minutes(5));
    }
}
