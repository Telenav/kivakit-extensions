package com.telenav.kivakit.ui.desktop.component.layout.spacer;

import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;

import java.awt.Dimension;

/**
 * @author jonathanl (shibo)
 */
public class Spacer extends KivaKitPanel
{
    public Spacer(int width, int height)
    {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);
    }
}
