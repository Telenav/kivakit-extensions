package com.telenav.kivakit.ui.swing.component.version;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.language.values.version.Version;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitVersion extends JLabel
{
    public KivaKitVersion()
    {
        this(KivaKit.get().version());
    }

    public KivaKitVersion(final Version version)
    {
        super("KivaKit " + version);

        KivaKitTheme.get().configureComponentLabel(this);
    }
}
