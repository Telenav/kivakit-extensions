package com.telenav.kivakit.settings.stores.zookeeper.converters;

import com.telenav.kivakit.conversion.string.enumeration.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import org.apache.zookeeper.CreateMode;

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
