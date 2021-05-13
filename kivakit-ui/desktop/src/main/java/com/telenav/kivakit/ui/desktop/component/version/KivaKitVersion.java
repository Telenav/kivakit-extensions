package com.telenav.kivakit.ui.desktop.component.version;

import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

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

        KivaKitTheme.get().applyToComponentLabel(this);
    }
}
