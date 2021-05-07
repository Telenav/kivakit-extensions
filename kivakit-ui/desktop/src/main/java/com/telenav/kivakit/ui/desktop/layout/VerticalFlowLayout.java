package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class VerticalFlowLayout
{
    private final GridBagConstraints constraints;

    private final JComponent parent;

    public VerticalFlowLayout(final JComponent parent)
    {
        this.parent = parent;

        parent.setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
    }

    public VerticalFlowLayout add(final JComponent child)
    {
        parent.add(child, constraints);
        constraints.gridy++;
        return this;
    }
}
