package com.telenav.kivakit.ui.swing.component.icon.search;

import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class MagnifyingGlass extends JLabel
{
    public MagnifyingGlass()
    {
        super(new ImageIcon(MagnifyingGlass.class.getResource(KivaKitTheme.get().isDark() ? "light-magnifying-glass.png"
                : "dark-magnifying-glass.png")), SwingConstants.LEFT);

        setOpaque(false);
    }
}
