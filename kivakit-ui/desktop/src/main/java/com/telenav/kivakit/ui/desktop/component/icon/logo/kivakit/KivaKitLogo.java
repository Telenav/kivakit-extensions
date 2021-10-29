package com.telenav.kivakit.ui.desktop.component.icon.logo.kivakit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitLogo extends JLabel
{
    public enum Size
    {
        _32x32("kivakit-32.png"),
        _64x64("kivakit-64.png");

        private final String filename;

        Size(String filename)
        {
            this.filename = filename;
        }

        public String filename()
        {
            return filename;
        }
    }

    public KivaKitLogo(Size type)
    {
        setIcon(new ImageIcon(getClass().getResource(type.filename())));
    }
}
