package com.telenav.kivakit.ui.desktop.component.layout.separator;

import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.layout.Size;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

/**
 * @author jonathanl (shibo)
 */
public class HorizontalSeparator extends KivaKitPanel
{
    public HorizontalSeparator()
    {
        this(KivaKitTheme.get().colorSeparator());
    }

    public HorizontalSeparator(Color color)
    {
        color.applyAsBackground(this);
        Size.heightOf(1).preferred(this);
    }
}
