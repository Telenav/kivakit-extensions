package com.telenav.kivakit.ui.swing.component.dropdown;

import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class DropDownRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent
            (
                    final JList list,
                    final Object value,
                    final int index,
                    final boolean isSelected,
                    final boolean cellHasFocus
            )
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        final var theme = KivaKitTheme.get();
        if (isSelected)
        {
            theme.colorSelectionBackground().background(this);
            theme.colorSelectionText().foreground(this);
        }
        else
        {
            theme.colorDropdownBackground().background(this);
            theme.colorDropdownText().foreground(this);
        }

        return this;
    }
}
