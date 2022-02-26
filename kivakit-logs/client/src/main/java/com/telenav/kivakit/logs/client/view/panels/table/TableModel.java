package com.telenav.kivakit.logs.client.view.panels.table;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.logs.client.view.ClientLogPanel;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.telenav.kivakit.interfaces.string.Stringable.Format.USER_LABEL;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * @author jonathanl (shibo)
 */
public class TableModel extends AbstractTableModel
{
    static final int COLUMNS = 6;

    static final int SEQUENCE_NUMBER = 0;

    static final int ELAPSED = 1;

    static final int THREAD = 2;

    static final int CONTEXT = 3;

    static final int MESSAGE_TYPE = 4;

    static final int MESSAGE = 5;

    private static final String[] COLUMN_NAMES = new String[COLUMNS];

    private static final Class<?>[] COLUMN_TYPES = new Class<?>[COLUMNS];

    static
    {
        COLUMN_NAMES[SEQUENCE_NUMBER] = "#";
        COLUMN_NAMES[ELAPSED] = "Elapsed";
        COLUMN_NAMES[THREAD] = "Thread";
        COLUMN_NAMES[CONTEXT] = "Context";
        COLUMN_NAMES[MESSAGE_TYPE] = "TimeFormat";
        COLUMN_NAMES[MESSAGE] = "Message";
    }

    static
    {
        COLUMN_TYPES[SEQUENCE_NUMBER] = Count.class;
        COLUMN_TYPES[ELAPSED] = Duration.class;
        COLUMN_TYPES[THREAD] = Thread.class;
        COLUMN_TYPES[CONTEXT] = LoggerCodeContext.class;
        COLUMN_TYPES[MESSAGE_TYPE] = Message.class;
        COLUMN_TYPES[MESSAGE] = Message.class;
    }

    private final int maximumRows;

    private final ClientLogPanel parent;

    public TableModel(ClientLogPanel parent, int maximumRows)
    {
        this.parent = parent;
        this.maximumRows = maximumRows;
    }

    public synchronized void addRows(List<LogEntry> toAdd)
    {
        var copy = new ArrayList<>(toAdd);
        SwingUtilities.invokeLater(() ->
        {
            while (rows.size() + copy.size() > maximumRows)
            {
                rows.removeFirst();
            }
            rows.addAll(copy);

            parent.searchPanel().updateSearchDropDowns(copy);
            parent.tablePanel().scroll();
            parent.updateTitle();
        });
    }

    public synchronized void clear()
    {
        if (!rows.isEmpty())
        {
            rows.clear();
            parent.updateTitle();
        }
        fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int column)
    {
        return COLUMN_TYPES[column];
    }

    @Override
    public int getColumnCount()
    {
        return COLUMNS;
    }

    @Override
    public String getColumnName(int column)
    {
        return "  " + COLUMN_NAMES[column];
    }

    @Override
    public int getRowCount()
    {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        var entry = row(row);

        switch (column)
        {
            case SEQUENCE_NUMBER:
                return entry.sequenceNumber();

            case ELAPSED:
                return entry.created().elapsedSince().asString(USER_LABEL);

            case THREAD:
                return entry.threadName();

            case CONTEXT:
                return entry.context();

            case MESSAGE_TYPE:
                return entry.messageType();

            case MESSAGE:
                return entry.formattedMessage().replaceAll("\\s+", " ");

            default:
                ensure(false);
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public LogEntry row(int which)
    {
        return rows.get(which);
    }

    final LinkedList<LogEntry> rows = new LinkedList<>();
}
