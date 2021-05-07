package com.telenav.kivakit.ui.desktop.component.health;

import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class CpuUse extends KivaKitPanel
{
    private final JProgressBar cpuUse;

    public CpuUse()
    {
        setOpaque(false);

        cpuUse = newProgressBar();
        cpuUse.setPreferredSize(new Dimension(175, 20));

        add(cpuUse);
    }

    public CpuUse update(final JavaVirtualMachineHealth health)
    {
        cpuUse.setString(Message.format("$% cpu", (int) health.cpuUse()));
        cpuUse.setStringPainted(true);
        cpuUse.setMaximum((int) health.elapsed().asSeconds());
        cpuUse.setValue((int) health.elapsedCpuTime().asSeconds());

        return this;
    }
}
