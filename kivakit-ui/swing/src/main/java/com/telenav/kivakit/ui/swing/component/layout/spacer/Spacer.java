package com.telenav.kivakit.ui.swing.component.layout.spacer;

import com.telenav.kivakit.ui.swing.component.KivaKitPanel;

import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class Spacer extends KivaKitPanel
{
    public Spacer(final int width, final int height)
    {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));
        setSize(width, height);
    }
}
