package com.telenav.kivakit.ui.swing.theme;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.component.dropdown.DropDownRenderer;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors;
import com.telenav.kivakit.ui.swing.graphics.font.Fonts;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.layout.Size;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * @author jonathanl (shibo)
 */
public abstract class KivaKitTheme
{
    private static KivaKitTheme theme;

    public static KivaKitTheme get()
    {
        return theme;
    }

    public static void set(final KivaKitTheme theme)
    {
        KivaKitTheme.theme = theme;
    }

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorBorder();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorCaret();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorComponentLabel();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorDropdownBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorDropdownText();

    public com.telenav.kivakit.ui.swing.graphics.color.Color colorError()
    {
        return KivaKitColors.CHERRY;
    }

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorFadedText();

    public com.telenav.kivakit.ui.swing.graphics.color.Color colorInformation()
    {
        return KivaKitColors.LIGHT_GRAY;
    }

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorInformationLabel();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorNote();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorProgressBarBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorProgressBarForeground();

    public com.telenav.kivakit.ui.swing.graphics.color.Color colorQuibble()
    {
        return KivaKitColors.DARK_KIVAKIT_YELLOW;
    }

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorSelectionBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorSelectionText();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorSeparator();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorShadedPanel();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorShadedSubPanel();

    public com.telenav.kivakit.ui.swing.graphics.color.Color colorSuccess()
    {
        return KivaKitColors.CLOVER;
    }

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTableBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTableHeaderBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTableHeaderText();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTableText();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorText();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTextAreaBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTextFieldBackground();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTextFieldText();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTitle();

    public abstract com.telenav.kivakit.ui.swing.graphics.color.Color colorTitleBackground();

    public Color colorWarning()
    {
        return KivaKitColors.TANGERINE;
    }

    public final JButton configure(final JButton button)
    {
        colorText().foreground(button);
        button.setFont(fontComponent());
        return button;
    }

    public final JTableHeader configure(final JTableHeader header)
    {
        colorTableHeaderText().foreground(header);
        header.setFont(fontComponent());
        return header;
    }

    public final JEditorPane configure(final JEditorPane editor)
    {
        editor.setEditable(false);
        editor.setSelectionColor(colorSelectionBackground().asAwtColor());
        editor.setSelectedTextColor(colorSelectionText().asAwtColor());
        return editor;
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown)
    {
        return configure(dropdown, -1);
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown, final int preferredWidth)
    {
        dropdown.setMaximumRowCount(50);
        if (preferredWidth > 0)
        {
            Size.widthOf(preferredWidth).preferred(dropdown);
        }
        colorText().foreground(dropdown);
        dropdown.setFont(fontComponent());
        dropdown.setRenderer(new DropDownRenderer());
        return dropdown;
    }

    public <T> JList<T> configure(final JList<T> list)
    {
        list.setFont(fontComponent());
        colorTableBackground().background(list);
        colorTableText().foreground(list);
        list.setSelectionBackground(colorSelectionBackground().asAwtColor());
        list.setSelectionForeground(colorSelectionText().asAwtColor());
        Margins.of(10).apply(list);
        return list;
    }

    public JTable configure(final JTable table)
    {
        colorTableHeaderText().foreground(table.getTableHeader());
        colorTableHeaderBackground().background(table.getTableHeader());
        colorTableBackground().background(table);
        colorTableText().foreground(table);
        table.setFont(tableFont());
        table.setSelectionBackground(colorSelectionBackground().asAwtColor());
        table.setSelectionForeground(colorSelectionText().asAwtColor());
        return table;
    }

    public JCheckBox configure(final JCheckBox checkbox)
    {
        colorText().foreground(checkbox);
        checkbox.setFont(fontComponent());
        return checkbox;
    }

    public JTextField configure(final JTextField field)
    {
        colorTextFieldText().foreground(field);
        field.setFont(fontComponent());
        field.setCaretColor(colorCaret().asAwtColor());
        return field;
    }

    public final JLabel configureComponentLabel(final JLabel label)
    {
        colorComponentLabel().foreground(label);
        label.setFont(fontComponent());
        return label;
    }

    public JPanel configureContainerPanel(final JPanel panel)
    {
        return panel;
    }

    public JTextField configureSearchField(final JTextField field)
    {
        colorTextFieldText().foreground(field);
        field.setFont(fontComponent());
        field.setCaretColor(colorCaret().asAwtColor());
        return field;
    }

    public KivaKitPanel configureShadedPanel(final KivaKitPanel panel)
    {
        colorShadedPanel().background(panel);
        return panel;
    }

    public KivaKitPanel configureShadedSubPanel(final KivaKitPanel panel)
    {
        colorShadedSubPanel().background(panel);
        return panel;
    }

    public Font fontComponent()
    {
        return Fonts.component(12);
    }

    public Font fontFixedWidth()
    {
        return Fonts.fixedWidth(Font.PLAIN, 12);
    }

    public Font fontTitle()
    {
        return fontComponent().deriveFont(16.0f);
    }

    public abstract boolean isDark();

    public Margins margins()
    {
        return Margins.of(10);
    }

    public final JButton newButton(final String text, final ActionListener listener)
    {
        final var button = newButton(text);
        button.addActionListener(listener);
        return button;
    }

    public final JButton newButton(final String text, final Source<Boolean> enabled, final ActionListener listener)
    {
        final var button = new JButton(text)
        {
            @Override
            public boolean isEnabled()
            {
                return enabled.get();
            }
        };
        configure(button);
        button.addActionListener(listener);
        return button;
    }

    public final JButton newButton(final String text)
    {
        return configure(new JButton(text));
    }

    public final JCheckBox newCheckBox(final String text)
    {
        return configure(new JCheckBox(text));
    }

    public final JLabel newComponentLabel(final String text)
    {
        return configureComponentLabel(new JLabel(text));
    }

    public KivaKitPanel newContainerPanel()
    {
        return (KivaKitPanel) configureContainerPanel(new KivaKitPanel());
    }

    public KivaKitPanel newContainerPanel(final LayoutManager layout)
    {
        return (KivaKitPanel) configureContainerPanel(new KivaKitPanel(layout));
    }

    public <T> JComboBox<T> newDropDown(final ComboBoxModel<T> model)
    {
        return new JComboBox<>(model);
    }

    public <T> JComboBox<T> newDropDown(final T[] values, final Source<Boolean> enabled)
    {
        return new JComboBox<>(values)
        {
            @Override
            public boolean isEnabled()
            {
                return enabled.get();
            }
        };
    }

    public <T> JComboBox<T> newDropDown(final ComboBoxModel<T> model, final Source<Boolean> enabled)
    {
        return new JComboBox<>(model)
        {
            @Override
            public boolean isEnabled()
            {
                return enabled.get();
            }
        };
    }

    public <T> JComboBox<T> newDropDown(final T[] values)
    {
        return new JComboBox<>(values);
    }

    public final JSeparator newHorizontalSeparator()
    {
        final var separator = new JSeparator(JSeparator.HORIZONTAL);
        colorSeparator().foreground(separator);
        return separator;
    }

    public JLabel newInformationLabel(final String text)
    {
        final var label = newComponentLabel(text);
        colorInformationLabel().foreground(label);
        return label;
    }

    public JLabel newListCellRenderer(final String text, final boolean isSelected)
    {
        final var renderer = newComponentLabel(text);
        renderer.setOpaque(true);

        if (isSelected)
        {
            colorSelectionBackground().background(renderer);
            colorSelectionText().foreground(renderer);
        }
        else
        {
            colorDropdownBackground().background(renderer);
            colorDropdownText().foreground(renderer);
        }
        return renderer;
    }

    public final JLabel newNote(final String text)
    {
        final var label = newComponentLabel(text);
        label.setFont(smallComponentFont());
        colorFadedText().foreground(label);
        return label;
    }

    public final JProgressBar newProgressBar()
    {
        final var bar = new JProgressBar(JProgressBar.HORIZONTAL);
        bar.setOpaque(false);
        bar.setMinimum(0);
        bar.setFont(smallComponentFont());
        colorProgressBarBackground().background(bar);
        colorProgressBarForeground().foreground(bar);
        return bar;
    }

    public JScrollPane newScrollPane(final JComponent child, final AdjustmentListener listener)
    {
        final var scrollPane = new JScrollPane(child, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        colorTableBackground().background(scrollPane);
        final var scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setPreferredSize(new Dimension(15, scrollbar.getWidth()));
        scrollbar.addAdjustmentListener(listener);
        return scrollPane;
    }

    public JTextField newSearchField()
    {
        return configureSearchField(newTextField());
    }

    public JTextField newSearchField(final Source<Boolean> enabled)
    {
        return configureSearchField(newTextField(enabled));
    }

    public KivaKitPanel newShadedPanel()
    {
        return configureShadedPanel(new KivaKitPanel());
    }

    public KivaKitPanel newShadedSubPanel()
    {
        return configureShadedSubPanel(new KivaKitPanel());
    }

    public JTabbedPane newTabbedPane()
    {
        return new JTabbedPane();
    }

    public final JTextField newTextField(final int characters, final Source<Boolean> enabled)
    {
        final var field = new JTextField(characters)
        {
            @Override
            public boolean isEnabled()
            {
                return enabled.get();
            }
        };
        return configure(field);
    }

    public final JTextField newTextField(final int characters)
    {
        final var field = characters < 0 ? new JTextField() : new JTextField(characters);
        return configure(field);
    }

    public final JTextField newTextField()
    {
        return configure(newTextField(0));
    }

    public final JTextField newTextField(final Source<Boolean> enabled)
    {
        return configure(newTextField(0, enabled));
    }

    public final JSeparator newVerticalSeparator()
    {
        final var separator = new JSeparator(JSeparator.VERTICAL);
        colorSeparator().foreground(separator);
        return separator;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public JSplitPane newVerticalSplitPane(final JPanel top, final JPanel bottom)
    {

        final var splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        splitPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        splitPane.setOneTouchExpandable(false);
        splitPane.setResizeWeight(0.75);
        splitPane.setContinuousLayout(true);
        return splitPane;
    }

    public void popupInformation(final JFrame frame, final String title, final String message)
    {
        UIManager.put("OptionPane.background", new ColorUIResource(colorShadedPanel().asAwtColor()));
        UIManager.put("Panel.background", new ColorUIResource(colorShadedPanel().asAwtColor()));

        final JOptionPane option = new JOptionPane("<html><h4><font color='#f0f0f0'>" + message + "</font></h4></html>",
                JOptionPane.INFORMATION_MESSAGE);
        option.setFont(fontComponent());
        colorText().foreground(option);

        final JDialog dialog = option.createDialog(frame, title);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public Font smallComponentFont()
    {
        return Fonts.component(11);
    }

    public Font tableFont()
    {
        return Fonts.component(13);
    }
}
