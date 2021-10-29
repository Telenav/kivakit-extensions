package com.telenav.kivakit.ui.desktop.component.panel.properties;

import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.component.layout.spacer.Spacer;

import javax.swing.JComponent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHEAST;
import static java.awt.GridBagConstraints.NORTHWEST;

/**
 * @author jonathanl (shibo)
 */
public class PropertyPanel extends KivaKitPanel
{
    private final GridBagConstraints constraints = new GridBagConstraints();

    public PropertyPanel()
    {
        setOpaque(false);
        setLayout(new GridBagLayout());

        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.ipadx = 5;
        constraints.ipady = 5;
        constraints.gridx = 0;
        constraints.gridy = 0;
    }

    public void property(String label, JComponent component)
    {
        var propertyName = newComponentLabel(label);

        constraints.anchor = NORTHEAST;
        constraints.gridx = 0;

        propertyName.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        add(propertyName, constraints);

        constraints.gridx++;
        constraints.anchor = NORTHWEST;

        component.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        add(component, constraints);

        constraints.gridy++;
    }

    public void property(String label, String value)
    {
        property(label, newComponentLabel(value));
    }

    public void space(int height)
    {
        constraints.anchor = NORTH;
        constraints.gridx = 0;

        add(new Spacer(10, height), constraints);

        constraints.gridx++;

        add(new Spacer(10, height), constraints);

        constraints.gridy++;
    }
}
