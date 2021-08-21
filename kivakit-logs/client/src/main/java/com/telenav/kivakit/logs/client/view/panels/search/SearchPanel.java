package com.telenav.kivakit.logs.client.view.panels.search;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.logs.client.view.ClientLogPanel;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.component.icon.search.MagnifyingGlass;
import com.telenav.kivakit.ui.desktop.event.EventCoalescer;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.desktop.layout.Size;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.ui.desktop.layout.Spacing.MANUAL_SPACING;

/**
 * @author jonathanl (shibo)
 */
public class SearchPanel extends KivaKitPanel
{
    public static final String ALL_THREADS = "All Threads";

    public static final String ALL_CONTEXTS = "All Contexts";

    public static final String ALL_TYPES = "All Message Types";

    private JComboBox<String> threadDropDown;

    private JComboBox<String> contextDropDown;

    private JComboBox<String> messageTypeDropDown;

    private final Set<String> threadNames = new HashSet<>();

    private final Set<String> contexts = new HashSet<>();

    private JTextField searchField;

    private final ClientLogPanel parent;

    public SearchPanel(final ClientLogPanel parent)
    {
        this.parent = parent;

        Size.maximumWidth().maximum(this);

        new HorizontalBoxLayout(this, MANUAL_SPACING, 24)
                .verticalGlue()
                .add(verticalCenter(threadDropDown()))
                .add(verticalCenter(contextDropDown()))
                .add(verticalCenter(messageTypeDropDown()))
                .space(8)
                .add(new MagnifyingGlass())
                .add(searchField());
    }

    public void clear()
    {
        threadDropDown().setModel(new DefaultComboBoxModel<>(new String[] { ALL_THREADS }));
        contextDropDown().setModel(new DefaultComboBoxModel<>(new String[] { ALL_CONTEXTS }));
        searchField.setText("");
        threadNames.clear();
        contexts.clear();
    }

    public synchronized void updateSearchDropDowns(final List<LogEntry> entries)
    {
        var newContexts = false;
        var newThreadNames = false;

        for (final var entry : entries)
        {
            newContexts = contexts.add(entry.context().typeName()) || newContexts;
            newThreadNames = threadNames.add(entry.threadName()) || newThreadNames;
        }

        if (newThreadNames)
        {
            SwingUtilities.invokeLater(this::updateThreadsDropDown);
        }

        if (newContexts)
        {
            SwingUtilities.invokeLater(this::updateContextDropDown);
        }
    }

    private JComboBox<String> contextDropDown()
    {
        if (contextDropDown == null)
        {
            contextDropDown = KivaKitTheme.get().applyTo(new JComboBox<>(new String[] { ALL_CONTEXTS }));
            contextDropDown.addItemListener(event ->
            {
                if (event.getStateChange() == ItemEvent.SELECTED)
                {
                    search();
                }
            });
        }
        return contextDropDown;
    }

    private JComboBox<String> messageTypeDropDown()
    {
        if (messageTypeDropDown == null)
        {
            messageTypeDropDown = KivaKitTheme.get().applyTo(new JComboBox<>(new String[]
                    {
                            ALL_TYPES,
                            "Problem, Glitch or Warning",
                            "Problem or Warning",
                            "Problem",
                            "Warning or Glitch",
                            "Warning",
                            "Glitch",
                            "Announcement",
                            "Narration",
                            "Information",
                            "Trace"
                    }));
            messageTypeDropDown.addItemListener(event ->
            {
                if (event.getStateChange() == ItemEvent.SELECTED)
                {
                    search();
                }
            });
        }
        return messageTypeDropDown;
    }

    private void search()
    {
        final var searchText = searchField.getText();
        final var thread = threadDropDown.getSelectedItem();
        final var context = contextDropDown.getSelectedItem();
        final var messageType = messageTypeDropDown.getSelectedItem();
        if (thread != null && context != null && messageType != null)
        {
            parent.tablePanel().filter(searchText, thread.toString(), context.toString(), messageType.toString());
        }
    }

    private JTextField searchField()
    {
        if (searchField == null)
        {
            final var coalescer = new EventCoalescer(Frequency.every(Duration.milliseconds(150)),
                    () -> SwingUtilities.invokeLater(this::search));

            searchField = KivaKitTheme.get().newTextField();
            Size.maximumWidth().maximum(searchField);
            searchField.requestFocus();
            searchField.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(final DocumentEvent e)
                {
                    coalescer.startTimer();
                }

                @Override
                public void insertUpdate(final DocumentEvent e)
                {
                    coalescer.startTimer();
                }

                @Override
                public void removeUpdate(final DocumentEvent e)
                {
                    coalescer.startTimer();
                }
            });
        }

        return searchField;
    }

    private JComboBox<String> threadDropDown()
    {
        if (threadDropDown == null)
        {
            threadDropDown = KivaKitTheme.get().applyTo(new JComboBox<>(new String[] { ALL_THREADS }));
            threadDropDown.addItemListener(event ->
            {
                if (event.getStateChange() == ItemEvent.SELECTED)
                {
                    search();
                }
            });
            threadDropDown.setAlignmentY(CENTER_ALIGNMENT);
        }
        return threadDropDown;
    }

    private void updateContextDropDown()
    {
        final var sorted = new ArrayList<>(contexts);
        Collections.sort(sorted);
        final var dropdown = contextDropDown();
        final var selected = dropdown.getSelectedItem();
        dropdown.removeAllItems();
        dropdown.addItem(ALL_CONTEXTS);
        for (final var name : sorted)
        {
            dropdown.addItem(name);
        }
        if (selected != null)
        {
            dropdown.setSelectedItem(selected);
        }
        dropdown.setMaximumRowCount(contexts.size() + 1);
        dropdown.repaint();
    }

    private void updateThreadsDropDown()
    {
        final var sorted = new ArrayList<>(threadNames);
        Collections.sort(sorted);
        final var dropdown = threadDropDown();
        final var selected = dropdown.getSelectedItem();
        dropdown.removeAllItems();
        dropdown.addItem(ALL_THREADS);
        for (final var name : sorted)
        {
            dropdown.addItem(name);
        }
        if (selected != null)
        {
            dropdown.setSelectedItem(selected);
        }
        dropdown.setMaximumRowCount(threadNames.size() + 1);
        dropdown.repaint();
    }
}
