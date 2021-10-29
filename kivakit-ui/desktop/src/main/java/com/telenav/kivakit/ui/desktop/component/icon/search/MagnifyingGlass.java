package com.telenav.kivakit.ui.desktop.component.icon.search;

import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
