package com.telenav.kivakit.ui.desktop.component.version;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;

import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitBuild extends KivaKitPanel
{
    public KivaKitBuild()
    {
        setOpaque(false);
        setLayout(new BorderLayout());

        final var kivakit = KivaKit.get();
        final var text = "KivaKit " + kivakit.version().withoutRelease() + " " + kivakit.build().name();
        add(newSmallFadedLabel(text), BorderLayout.EAST);
    }
}
