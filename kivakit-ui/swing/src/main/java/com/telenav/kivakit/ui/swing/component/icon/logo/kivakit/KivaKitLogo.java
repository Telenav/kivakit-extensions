package com.telenav.kivakit.ui.swing.component.icon.logo.kivakit;

import javax.swing.*;

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

        Size(final String filename)
        {
            this.filename = filename;
        }

        public String filename()
        {
            return filename;
        }
    }

    public KivaKitLogo(final Size type)
    {
        setIcon(new ImageIcon(getClass().getResource(type.filename())));
    }
}
