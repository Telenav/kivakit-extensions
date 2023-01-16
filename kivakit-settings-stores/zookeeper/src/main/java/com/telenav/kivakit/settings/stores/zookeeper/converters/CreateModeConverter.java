package com.telenav.kivakit.settings.stores.zookeeper.converters;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.third.party.zookeeper.CreateMode;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Converts to and from the Zookeeper {@link CreateMode} enum value
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
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
