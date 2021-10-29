package com.telenav.kivakit.ui.desktop.layout;

import com.telenav.kivakit.ui.desktop.component.panel.stretch.StretchPanel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * @author jonathanl (shibo)
 */
public class Alignment
{
    public static JPanel left(JComponent component)
    {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return stretched(panel);
    }

    public static JPanel stretched(JComponent component)
    {
        var panel = new StretchPanel();
        panel.add(component);
        return panel;
    }

    public static JPanel top(JComponent component)
    {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(component);
        return stretched(panel);
    }
}
