package com.telenav.kivakit.ui.desktop.component.health;

import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;

import javax.swing.JProgressBar;
import java.awt.Dimension;

/**
 * @author jonathanl (shibo)
 */
public class MemoryUse extends KivaKitPanel
{
    private final JProgressBar memoryUse;

    public MemoryUse()
    {
        setOpaque(false);

        memoryUse = newProgressBar();
        memoryUse.setPreferredSize(new Dimension(175, 20));

        add(memoryUse);
    }

    public MemoryUse update(JavaVirtualMachineHealth health)
    {
        var totalMemory = health.totalMemory();
        var usedMemory = health.usedMemory();
        var maximumMemory = health.maximumMemory();

        memoryUse.setVisible(true);
        memoryUse.setMaximum((int) maximumMemory.asKilobytes());
        memoryUse.setValue((int) usedMemory.asKilobytes());
        memoryUse.setStringPainted(true);
        memoryUse.setString(Message.format
                (
                        "$ ($%) of $",
                        usedMemory,
                        (int) health.memoryUse(),
                        maximumMemory
                ));

        return this;
    }
}
