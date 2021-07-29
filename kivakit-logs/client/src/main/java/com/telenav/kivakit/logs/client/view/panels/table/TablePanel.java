package com.telenav.kivakit.logs.client.view.panels.table;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.strings.Align;
import com.telenav.kivakit.kernel.language.strings.StringTo;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.logs.client.view.ClientLogPanel;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.event.EventCoalescer;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.kernel.language.strings.conversion.StringFormat.USER_LABEL;
import static com.telenav.kivakit.kernel.logging.logs.text.formatters.ColumnarLogFormatter.DEFAULT;
import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.CONTEXT;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.ELAPSED;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.MESSAGE;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.MESSAGE_TYPE;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.SEQUENCE_NUMBER;
import static com.telenav.kivakit.logs.client.view.panels.table.TableModel.THREAD;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * @author jonathanl (shibo)
 */
public class TablePanel extends KivaKitPanel
{
    private JTable table;

    private volatile int firstSelectedIndex = -1;

    private volatile int lastSelectedIndex = -1;

    private boolean scrolledToBottom;

    private final TableModel model;

    private final List<LogEntry> toAdd = new ArrayList<>();

    private final ClientLogPanel parent;

    private final EventCoalescer addCoalescer;

    private JScrollPane scrollPane;

    public TablePanel(final ClientLogPanel parent)
    {
        assert parent != null;
        this.parent = parent;

        KivaKitTheme.get().styleTextField().apply(this);
        setMinimumSize(new Dimension(0, 200));

        // Create the table model,
        model = new TableModel(parent, parent.log().maximumEntries().asInt());
        model.addTableModelListener(event -> reselect());

        // add the header and the table
        setLayout(new BorderLayout());
        add(KivaKitTheme.get().applyTo(table().getTableHeader()));
        add(tableScrollPane());

        // add table listeners
        addListeners();

        // and create an event coalescer that adds to the session no more frequently than every 100ms.
        final var frequency = Frequency.every(Duration.milliseconds(100));
        addCoalescer = new EventCoalescer(frequency, () ->
                SwingUtilities.invokeLater(() ->
                {
                    synchronized (toAdd)
                    {
                        parent.addAll(new ArrayList<>(toAdd));
                        toAdd.clear();
                    }
                }));
    }

    public void add(final LogEntry entry)
    {
        synchronized (toAdd)
        {
            toAdd.add(entry);
        }

        addCoalescer.startTimer();
    }

    public void addAll(final List<LogEntry> entries)
    {
        model.addRows(entries);
    }

    public void clear()
    {
        table().removeAll();
        model.clear();
        synchronized (toAdd)
        {
            toAdd.clear();
        }
        scrolledToBottom = false;
        firstSelectedIndex = -1;
        lastSelectedIndex = -1;
    }

    public void filter(final String searchText, final String thread, final String context, final String messageType)
    {
        final var sorter = new TableRowSorter<javax.swing.table.TableModel>(model);
        sorter.setRowFilter(new TableFilter(searchText, thread, context, messageType));
        table().setRowSorter(sorter);
    }

    public Count rows()
    {
        return Count.count(model.getRowCount());
    }

    public void scroll()
    {
        final var scrollbar = scrollPane.getVerticalScrollBar();
        final var maximum = scrollbar.getMaximum() - scrollbar.getVisibleAmount();
        scrolledToBottom = scrollbar.getValue() >= maximum - 2;
        model.fireTableDataChanged();
        parent.tablePanel().reselect();
    }

    public void update()
    {
        addCoalescer.startTimer();
    }

    private void addListeners()
    {
        final var consolePanel = parent.consolePanel();
        table().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table().getSelectionModel().addListSelectionListener((event) ->
        {
            final var from = table().getSelectionModel().getMinSelectionIndex();
            final var to = table().getSelectionModel().getMaxSelectionIndex();
            if (from >= 0 && to >= 0 && !(from == firstSelectedIndex && to == lastSelectedIndex))
            {
                if (from == to)
                {
                    final var modelIndex = table().convertRowIndexToModel(from);
                    final var row = model.row(modelIndex);
                    final var lines = new StringList();
                    final int align = 11;

                    lines.add(Align.right("#: ", align, ' ') + row.sequenceNumber());
                    lines.add(Align.right("elapsed: ", align, ' ') + row.created().elapsedSince().asString(USER_LABEL));
                    lines.add(Align.right("time: ", align, ' ') + row.created());
                    lines.add(Align.right("host: ", align, ' ') + row.context().host());
                    lines.add(Align.right("thread: ", align, ' ') + row.threadName());
                    lines.add(Align.right("context: ", align, ' ') + row.context().fullTypeName());
                    lines.add(Align.right("type: ", align, ' ') + row.messageType());
                    lines.add(Align.right("severity: ", align, ' ') + row.severity().toString().toUpperCase());

                    final var stackTrace = row.stackTrace();
                    final int fontSize = 14;
                    consolePanel.text(
                            "<html><font style='font-size: " + fontSize + "'>"
                                    + "<p><font color='#0096ff'>" + StringTo.html(lines.join('\n')) + "</font></p>"
                                    + "<p><font color='#FF9300'>" + StringTo.html(row.formattedMessage()) + "</font></p>"
                                    + (stackTrace == null ? "" : ("<p><font color='#8EFA00'>" + StringTo.html(stackTrace.toString()) + "</font></p>"))
                                    + "</font></html>");
                }
                else
                {
                    final var lines = new StringList();
                    for (var rowIndex = from; rowIndex <= to; rowIndex++)
                    {
                        final var modelIndex = table().convertRowIndexToModel(rowIndex);
                        final var row = model.row(modelIndex);
                        lines.add(row.format(DEFAULT, WITHOUT_EXCEPTION));
                        final var stackTrace = row.stackTrace();
                        if (stackTrace != null)
                        {
                            lines.add(stackTrace.toString());
                        }
                    }
                    consolePanel.text("<html><p><font color='#FF9300'>"
                            + StringTo.html(lines.join('\n'))
                            + "</font></p></html>");
                }
                firstSelectedIndex = from;
                lastSelectedIndex = to;
            }
        });
    }

    private void reselect()
    {
        if (firstSelectedIndex >= 0 && lastSelectedIndex >= 0)
        {
            try
            {
                table().setRowSelectionInterval(firstSelectedIndex, lastSelectedIndex);
            }
            catch (final Exception ignored)
            {
            }
        }
    }

    private JTable table()
    {
        if (table == null)
        {
            table = KivaKitTheme.get().applyTo(new JTable());
            table.setModel(model);
            table.setAutoCreateRowSorter(true);
            table.setShowGrid(false);
            table.setIntercellSpacing(new Dimension(0, 0));
            table.setDefaultRenderer(Object.class, new TableCellRenderer());
            table.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentResized(final ComponentEvent e)
                {
                    if (scrolledToBottom)
                    {
                        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
                        reselect();
                    }
                }
            });

            width(table.getColumnModel().getColumn(SEQUENCE_NUMBER), 70);
            width(table.getColumnModel().getColumn(ELAPSED), 70);
            width(table.getColumnModel().getColumn(THREAD), 200);
            width(table.getColumnModel().getColumn(CONTEXT), 250);
            width(table.getColumnModel().getColumn(MESSAGE_TYPE), 90);
            width(table.getColumnModel().getColumn(MESSAGE), 1000);
        }

        return table;
    }

    private JScrollPane tableScrollPane()
    {
        scrollPane = new JScrollPane(table(), VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(KivaKitTheme.get().styleTable().fillColor().asAwtColor());
        final var scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setPreferredSize(new Dimension(15, scrollbar.getWidth()));
        scrollbar.addAdjustmentListener(event -> reselect());
        return scrollPane;
    }

    private void width(final TableColumn column, final int width)
    {
        column.setMaxWidth((int) (width * 1.5));
        column.setPreferredWidth(width);
    }
}
