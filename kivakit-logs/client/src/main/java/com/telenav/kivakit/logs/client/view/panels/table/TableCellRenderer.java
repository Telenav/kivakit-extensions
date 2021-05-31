package com.telenav.kivakit.logs.client.view.panels.table;

import com.telenav.kivakit.kernel.language.strings.StringTo;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.USER_LABEL;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.COLUMNS;

/**
 * @author jonathanl (shibo)
 */
class TableCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(final JTable table,
                                                   final Object object,
                                                   final boolean isSelected,
                                                   final boolean hasFocus,
                                                   final int row,
                                                   final int column)
    {
        final var renderer = super.getTableCellRendererComponent(table, object, isSelected, hasFocus, row, column);

        if (isSelected)
        {
            KivaKitTheme.get().styleSelection().apply(renderer);
        }
        else
        {
            Class<? extends Message> highlight = Information.class;
            final var modelRow = table.convertRowIndexToModel(row);
            for (var at = 0; at < COLUMNS; at++)
            {
                final var value = table.getModel().getValueAt(modelRow, at);
                final String text = StringTo.string(value).trim();
                if (text.startsWith("!") || text.endsWith("!"))
                {
                    highlight = Problem.class;
                }
                if ("Warning".equals(text))
                {
                    highlight = Warning.class;
                }
            }
            KivaKitTheme.get().styleMessage(highlight).apply(renderer);
        }

        if (object != null)
        {
            ((JLabel) renderer).setText(object instanceof Duration
                    ? ((Duration) object).asString(USER_LABEL)
                    : object.toString());
        }

        return renderer;
    }
}
