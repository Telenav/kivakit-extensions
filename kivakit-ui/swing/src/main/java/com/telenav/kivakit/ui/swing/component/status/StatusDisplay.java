package com.telenav.kivakit.ui.swing.component.status;

import com.telenav.kivakit.core.kernel.language.time.Duration;

/**
 * Displays a short feedback string for a given duration.
 *
 * @author jonathanl (shibo)
 */
public interface StatusDisplay
{
    void status(Duration displayTime, String message, Object... arguments);

    default void status(final String message, final Object... arguments)
    {
        status(Duration.seconds(5), message, arguments);
    }
}