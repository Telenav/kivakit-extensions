package com.telenav.kivakit.ui.desktop.component.dropdown;

import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

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

        var theme = KivaKitTheme.get();
        if (isSelected)
        {
            theme.styleSelection().applyColors(this);
        }
        else
        {
            theme.styleDropdown().apply(this);
        }

        return this;
    }
}
