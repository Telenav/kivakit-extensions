package com.telenav.kivakit.settings.stores.zookeeper.converters;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import kivakit.merged.zookeeper.CreateMode;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.CodeType.CODE_INTERNAL;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Converts to and from the Zookeeper {@link CreateMode} enum value
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE,
             type = CODE_INTERNAL)
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
