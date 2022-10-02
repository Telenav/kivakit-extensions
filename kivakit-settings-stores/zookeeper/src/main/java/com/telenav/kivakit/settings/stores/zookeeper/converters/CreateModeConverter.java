package com.telenav.kivakit.settings.stores.zookeeper.converters;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import kivakit.merged.zookeeper.CreateMode;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE_API;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Converts to and from the Zookeeper {@link CreateMode} enum value
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED,
            type = PRIVATE_API)
public class CreateModeConverter extends EnumConverter<CreateMode>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    public CreateModeConverter(Listener listener)
    {
        super(listener, CreateMode.class);
    }
}
