package com.telenav.kivakit.settings.stores.zookeeper.converters;

import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import kivakit.merged.zookeeper.CreateMode;

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
