package com.telenav.kivakit.ui.swing.layout;

import com.telenav.kivakit.ui.swing.component.panel.stretch.StretchPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class Alignment
{
    public static JPanel left(final JComponent component)
    {
        final var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return stretched(panel);
    }

    public static JPanel stretched(final JComponent component)
    {
        final var panel = new StretchPanel();
        panel.add(component);
        return panel;
    }

    public static JPanel top(final JComponent component)
    {
        final var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return stretched(panel);
    }
}
