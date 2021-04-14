package com.telenav.kivakit.ui.swing.component.layout.separator;

import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.layout.Size;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

/**
 * @author jonathanl (shibo)
 */
public class HorizontalSeparator extends KivaKitPanel
{
    public HorizontalSeparator()
    {
        this(KivaKitTheme.get().colorSeparator());
    }

    public HorizontalSeparator(final Color color)
    {
        setBackground(color.asAwtColor());
        Size.heightOf(1).preferred(this);
    }
}
