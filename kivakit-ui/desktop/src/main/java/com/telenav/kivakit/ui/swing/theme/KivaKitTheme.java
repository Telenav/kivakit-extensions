package com.telenav.kivakit.ui.swing.theme;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Success;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.component.dropdown.DropDownRenderer;
import com.telenav.kivakit.ui.swing.graphics.font.Fonts;
import com.telenav.kivakit.ui.swing.graphics.style.Color;
import com.telenav.kivakit.ui.swing.graphics.style.Style;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.layout.Size;
import com.telenav.kivakit.ui.swing.theme.darcula.KivaKitDarculaTheme;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.JTableHeader;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.CHERRY;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.CLOVER;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.DARK_KIVAKIT_YELLOW;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.LIGHT_GRAY;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.TANGERINE;
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
        if (theme == null)
        {
            theme = new KivaKitDarculaTheme();
        }
        return theme;
    }

    public static void set(final KivaKitTheme theme)
    {
        KivaKitTheme.theme = theme;
    }

    public KivaKitTheme()
    {
        UIManager.put("Button.arc", 15);

        UIManager.put("TabbedPane.minimumTabWidth", 150);

        UIManager.put("TabbedPane.foreground", styleTab().fillColor().asColorUiResource());
        UIManager.put("TabbedPane.background", styleTab().fillColor().asColorUiResource());

        UIManager.put("TabbedPane.selectedForeground", styleSelectedTab().fillColor().asColorUiResource());
        UIManager.put("TabbedPane.selectedBackground", styleSelectedTab().fillColor().asColorUiResource());

        UIManager.put("TabbedPane.underlineColor", styleTabHover().fillColor().asColorUiResource());
        UIManager.put("TabbedPane.hoverColor", styleTabHover().fillColor().asColorUiResource());
    }

    public final JButton applyTo(final JButton button)
    {
        styleButton().apply(button);
        return button;
    }

    public final JTableHeader applyTo(final JTableHeader header)
    {
        styleTableHeader().apply(header);
        return header;
    }

    public final JEditorPane applyTo(final JEditorPane editor)
    {
        styleTextArea().apply(editor);
        styleSelection().applyToSelectionStyle(editor);
        return editor;
    }

    public <T> JComboBox<T> applyTo(final JComboBox<T> dropdown)
    {
        return applyTo(dropdown, -1);
    }

    public <T> JComboBox<T> applyTo(final JComboBox<T> dropdown, final int preferredWidth)
    {
        dropdown.setMaximumRowCount(50);
        if (preferredWidth > 0)
        {
            Size.widthOf(preferredWidth).preferred(dropdown);
        }
        styleDropdown().apply(dropdown);
        dropdown.setRenderer(new DropDownRenderer());
        return dropdown;
    }

    public <T> JList<T> applyTo(final JList<T> list)
    {
        styleList().apply(list);
        styleSelection().applyToSelectionStyle(list);
        Margins.of(10).apply(list);
        return list;
    }

    public JTable applyTo(final JTable table)
    {
        styleTableHeader().apply(table.getTableHeader());
        styleTable().apply(table);
        styleSelection().applyToSelectionStyle(table);
        return table;
    }

    public JCheckBox applyTo(final JCheckBox checkbox)
    {
        styleLabel().apply(checkbox);
        return checkbox;
    }

    public JTextField applyTo(final JTextField field)
    {
        styleTextField().apply(field);
        field.setCaretColor(colorCaret().asAwtColor());
        return field;
    }

    public final JLabel applyToComponentLabel(final JLabel label)
    {
        styleComponentLabel().apply(label);
        return label;
    }

    public JPanel applyToContainerPanel(final JPanel panel)
    {
        return panel;
    }

    public JTextField applyToSearchField(final JTextField field)
    {
        styleTextField().apply(field);
        field.setCaretColor(colorCaret().asAwtColor());
        return field;
    }

    public KivaKitPanel applyToShadedPanel(final KivaKitPanel panel)
    {
        colorPanel().applyAsBackground(panel);
        return panel;
    }

    public KivaKitPanel applyToShadedSubPanel(final KivaKitPanel panel)
    {
        colorSubPanel().applyAsBackground(panel);
        return panel;
    }

    public abstract Color colorBorder();

    public abstract Color colorCaret();

    public abstract Color colorPanel();

    public abstract Color colorSeparator();

    public abstract Color colorSubPanel();

    public Font fontFixedWidth()
    {
        return Fonts.fixedWidth(Font.PLAIN, 12);
    }

    public Font fontNormal()
    {
        return Fonts.component(12);
    }

    public Font fontSmall()
    {
        return Fonts.component(10);
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
        applyTo(button);
        button.addActionListener(listener);
        return button;
    }

    public final JButton newButton(final String text)
    {
        return applyTo(new JButton(text));
    }

    public final JCheckBox newCheckBox(final String text)
    {
        return applyTo(new JCheckBox(text));
    }

    public final JLabel newComponentLabel(final String text)
    {
        return applyToComponentLabel(new JLabel(text));
    }

    public KivaKitPanel newContainerPanel()
    {
        return (KivaKitPanel) applyToContainerPanel(new KivaKitPanel());
    }

    public KivaKitPanel newContainerPanel(final LayoutManager layout)
    {
        return (KivaKitPanel) applyToContainerPanel(new KivaKitPanel(layout));
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
        colorSeparator().applyAsForeground(separator);
        return separator;
    }

    public JLabel newInformationLabel(final String text)
    {
        final var label = newComponentLabel(text);
        styleInformationLabel().apply(label);
        return label;
    }

    public JLabel newListCellRenderer(final String text, final boolean isSelected)
    {
        final var renderer = newComponentLabel(text);
        renderer.setOpaque(true);

        if (isSelected)
        {
            styleSelection().applyColors(renderer);
        }
        else
        {
            styleList().apply(renderer);
        }
        return renderer;
    }

    public final JLabel newNote(final String text)
    {
        final var label = newComponentLabel(text);
        styleNote().apply(label);
        return label;
    }

    public final JProgressBar newProgressBar()
    {
        final var bar = new JProgressBar(JProgressBar.HORIZONTAL);
        bar.setOpaque(false);
        bar.setMinimum(0);
        styleProgressBar().apply(bar);
        return bar;
    }

    public JScrollPane newScrollPane(final JComponent child, final AdjustmentListener listener)
    {
        final var scrollPane = new JScrollPane(child, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
        styleTable().fillColor().applyAsBackground(scrollPane);
        final var scrollbar = scrollPane.getVerticalScrollBar();
        scrollbar.setPreferredSize(new Dimension(15, scrollbar.getWidth()));
        scrollbar.addAdjustmentListener(listener);
        return scrollPane;
    }

    public JTextField newSearchField()
    {
        return applyToSearchField(newTextField());
    }

    public JTextField newSearchField(final Source<Boolean> enabled)
    {
        return applyToSearchField(newTextField(enabled));
    }

    public KivaKitPanel newShadedPanel()
    {
        return applyToShadedPanel(new KivaKitPanel());
    }

    public KivaKitPanel newShadedSubPanel()
    {
        return applyToShadedSubPanel(new KivaKitPanel());
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
        return applyTo(field);
    }

    public final JTextField newTextField(final int characters)
    {
        final var field = characters < 0 ? new JTextField() : new JTextField(characters);
        return applyTo(field);
    }

    public final JTextField newTextField()
    {
        return applyTo(newTextField(0));
    }

    public final JTextField newTextField(final Source<Boolean> enabled)
    {
        return applyTo(newTextField(0, enabled));
    }

    public final JSeparator newVerticalSeparator()
    {
        final var separator = new JSeparator(JSeparator.VERTICAL);
        colorSeparator().applyAsForeground(separator);
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
        UIManager.put("OptionPane.background", new ColorUIResource(colorPanel().asAwtColor()));
        UIManager.put("Panel.background", new ColorUIResource(colorPanel().asAwtColor()));

        final JOptionPane option = new JOptionPane("<html><h4><font color='#f0f0f0'>" + message + "</font></h4></html>",
                JOptionPane.INFORMATION_MESSAGE);

        styleLabel().apply(option);

        final JDialog dialog = option.createDialog(frame, title);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public abstract Style styleButton();

    public abstract Style styleCaption();

    public abstract Style styleComponentLabel();

    public abstract Style styleDropdown();

    public abstract Style styleInformationLabel();

    public abstract Style styleLabel();

    public abstract Style styleList();

    public Style styleMessage(final Class<? extends Message> type)
    {
        if (type == Problem.class)
        {
            return Style.create().withTextColor(CHERRY);
        }
        if (type == Quibble.class)
        {
            return Style.create().withTextColor(DARK_KIVAKIT_YELLOW);
        }
        if (type == Success.class)
        {
            return Style.create().withTextColor(CLOVER);
        }
        if (type == Warning.class)
        {
            return Style.create().withTextColor(TANGERINE);
        }
        return Style.create().withTextColor(LIGHT_GRAY);
    }

    public abstract Style styleNote();

    public abstract Style styleProgressBar();

    public abstract Style styleSelectedTab();

    public abstract Style styleSelection();

    public abstract Style styleTab();

    public abstract Style styleTabHover();

    public abstract Style styleTable();

    public abstract Style styleTableHeader();

    public abstract Style styleTextArea();

    public abstract Style styleTextField();

    public abstract Style styleTitle();

    public Font tableFont()
    {
        return Fonts.component(13);
    }
}
