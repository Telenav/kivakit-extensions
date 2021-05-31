package com.telenav.kivakit.logs.server.project;

import com.telenav.kivakit.logs.server.session.Session;
import com.telenav.kivakit.serialization.kryo.KryoTypes;

/**
 * @author jonathanl (shibo)
 */
public class LogsServerKryoTypes extends KryoTypes
{
    public LogsServerKryoTypes()
    {
        //----------------------------------------------------------------------------------------------
        // NOTE: To maintain backward compatibility, classes are assigned identifiers by KivaKitKryoSerializer.
        // If classes are appended to groups and no classes are removed, older data can always be read.
        //----------------------------------------------------------------------------------------------

        group("logs-server", () -> register(Session.class));
    }
}
