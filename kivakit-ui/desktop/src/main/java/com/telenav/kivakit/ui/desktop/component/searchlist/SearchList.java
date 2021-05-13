package com.telenav.kivakit.ui.desktop.component.searchlist;

import com.telenav.kivakit.kernel.data.conversion.string.StringConverter;
import com.telenav.kivakit.kernel.interfaces.code.Callback;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.component.icon.search.MagnifyingGlass;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBox;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.layout.Spacing;
import com.telenav.kivakit.ui.desktop.layout.VerticalBoxLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * @author jonathanl (shibo)
 */
public class SearchList<T> extends KivaKitPanel
{
    private final JList<T> list;

    private final JTextField searchField;

    private List<T> elements;

    private final StringConverter<T> converter;

    public SearchList(final StringConverter<T> converter)
    {
        this.converter = converter;

        list = configure(new JList<>());
        list.setModel(new DefaultListModel<>());

        final var scroll = new JScrollPane(list, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(1000, 1000));
        scroll.setMinimumSize(new Dimension(150, 150));

        searchField = newTextField();
        searchField.setPreferredSize(new Dimension(1000, 24));

        Margins.of(10).apply(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void changedUpdate(final DocumentEvent e)
            {
                filter();
            }

            @Override
            public void insertUpdate(final DocumentEvent e)
            {
                filter();
            }

            @Override
            public void removeUpdate(final DocumentEvent e)
            {
                filter();
            }
        });

        final var searchTools = new HorizontalBox(Spacing.MANUAL_SPACING, 24)
                .add(new MagnifyingGlass())
                .add(searchField);

        new VerticalBoxLayout(this)
                .add(searchTools)
                .add(scroll);
    }

    public void addSelectionListener(final Callback<T> selectionListener)
    {
        list.addListSelectionListener((event) ->
        {
            final var selected = list.getSelectedValue();
            if (selected != null)
            {
                selectionListener.callback(selected);
            }
        });
    }

    public void elements(final List<T> elements)
    {
        this.elements = elements;

        final var model = (DefaultListModel<T>) list.getModel();
        model.removeAllElements();
        model.addAll(elements);
    }

    public void select(final T value)
    {
        list.setSelectedValue(value, true);
    }

    public T selected()
    {
        return list.getSelectedValue();
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        list.setEnabled(enabled);
        searchField.setEnabled(enabled);
    }

    private void filter()
    {
        final var model = (DefaultListModel<T>) list.getModel();
        if (model != null && elements != null)
        {
            model.removeAllElements();
            final String filter = searchField.getText();
            if (Strings.isEmpty(filter))
            {
                model.addAll(elements);
            }
            else
            {
                final var match = filter.toLowerCase();

                model.addAll(elements.stream()
                        .filter(item -> converter.toString(item).toLowerCase().startsWith(match))
                        .collect(Collectors.toList()));

                model.addAll(elements.stream()
                        .filter(item ->
                        {
                            final var string = converter.toString(item).toLowerCase();
                            return string.contains(match) && !string.startsWith(filter);
                        })
                        .collect(Collectors.toList()));
            }
        }
    }
}
