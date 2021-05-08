package com.telenav.kivakit.ui.desktop.component;

import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.messaging.repeaters.RepeaterTrait;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitPanel extends JPanel implements RepeaterTrait
{
    public KivaKitPanel()
    {
        theme().applyToContainerPanel(this);
    }

    public KivaKitPanel(final LayoutManager layout)
    {
        super(layout);
    }

    public JButton configure(final JButton button)
    {
        return theme().applyTo(button);
    }

    public JTableHeader configure(final JTableHeader header)
    {
        return theme().applyTo(header);
    }

    public JLabel configure(final JLabel label)
    {
        return theme().applyToComponentLabel(label);
    }

    public <T> JList<T> configure(final JList<T> list)
    {
        return theme().applyTo(list);
    }

    public JEditorPane configure(final JEditorPane editor)
    {
        return theme().applyTo(editor);
    }

    public JTextField configure(final JTextField field)
    {
        return theme().applyTo(field);
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown)
    {
        return theme().applyTo(dropdown);
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown, final int preferredWidth)
    {
        return theme().applyTo(dropdown, preferredWidth);
    }

    public JTable configure(final JTable table)
    {
        return theme().applyTo(table);
    }

    public JCheckBox configure(final JCheckBox checkbox)
    {
        return theme().applyTo(checkbox);
    }

    public JLabel configureComponentLabel(final JLabel label)
    {
        return theme().applyToComponentLabel(label);
    }

    public JPanel configureContainerPanel(final JPanel panel)
    {
        return theme().applyToContainerPanel(panel);
    }

    public JTextField configureSearchField(final JTextField field)
    {
        return theme().applyToSearchField(field);
    }

    public KivaKitPanel configureShadedPanel(final KivaKitPanel panel)
    {
        return theme().applyToShadedPanel(panel);
    }

    public KivaKitPanel configureShadedSubPanel(final KivaKitPanel panel)
    {
        return theme().applyToShadedSubPanel(panel);
    }

    public void debugColor()
    {
        setOpaque(true);
        Components.debugColor(this);
    }

    public boolean isDark()
    {
        return theme().isDark();
    }

    public Margins margins()
    {
        return theme().margins();
    }

    public KivaKitPanel margins(final int width)
    {
        Margins.of(width).apply(this);
        return this;
    }

    public final JButton newButton(final String text, final Source<Boolean> enabled, final ActionListener listener)
    {
        return theme().newButton(text, enabled, listener);
    }

    public JButton newButton(final String text)
    {
        return theme().newButton(text);
    }

    public JButton newButton(final String text, final ActionListener listener)
    {
        return theme().newButton(text, listener);
    }

    public JLabel newCellRenderer(final String text, final boolean isSelected)
    {
        return theme().newListCellRenderer(text, isSelected);
    }

    public JCheckBox newCheckBox(final String text)
    {
        return theme().newCheckBox(text);
    }

    public JLabel newComponentLabel(final String text)
    {
        return theme().newComponentLabel(text);
    }

    public KivaKitPanel newContainerPanel()
    {
        return theme().newContainerPanel();
    }

    public KivaKitPanel newContainerPanel(final LayoutManager layout)
    {
        return theme().newContainerPanel(layout);
    }

    public <T> JComboBox<T> newDropDown(final ComboBoxModel<T> model)
    {
        return theme().newDropDown(model);
    }

    public <T> JComboBox<T> newDropDown(final T[] values, final Source<Boolean> enabled)
    {
        return theme().newDropDown(values, enabled);
    }

    public <T> JComboBox<T> newDropDown(final ComboBoxModel<T> model, final Source<Boolean> enabled)
    {
        return theme().newDropDown(model, enabled);
    }

    public <T> JComboBox<T> newDropDown(final T[] values)
    {
        return theme().newDropDown(values);
    }

    public JSeparator newHorizontalSeparator()
    {
        return theme().newHorizontalSeparator();
    }

    public JLabel newInformationLabel(final String text)
    {
        return theme().newInformationLabel(text);
    }

    public JLabel newLabel(final String text)
    {
        return theme().newComponentLabel(text);
    }

    public JLabel newListCellRenderer(final String text, final boolean isSelected)
    {
        return theme().newListCellRenderer(text, isSelected);
    }

    public JLabel newNote(final String text)
    {
        return theme().newNote(text);
    }

    public JProgressBar newProgressBar()
    {
        return theme().newProgressBar();
    }

    public JScrollPane newScrollPane(final JComponent child, final AdjustmentListener listener)
    {
        return theme().newScrollPane(child, listener);
    }

    public JTextField newSearchField(final Source<Boolean> enabled)
    {
        return theme().newSearchField(enabled);
    }

    public JTextField newSearchField()
    {
        return theme().newSearchField();
    }

    public KivaKitPanel newShadedPanel()
    {
        return theme().newShadedPanel();
    }

    public KivaKitPanel newShadedSubPanel()
    {
        return theme().newShadedSubPanel();
    }

    public JLabel newSmallFadedLabel(final String text)
    {
        return theme().newNote(text);
    }

    public JTabbedPane newTabbedPane()
    {
        return theme().newTabbedPane();
    }

    public JTextField newTextField(final int characters, final Source<Boolean> enabled)
    {
        return theme().newTextField(characters, enabled);
    }

    public JTextField newTextField(final Source<Boolean> enabled)
    {
        return theme().newTextField(enabled);
    }

    public JTextField newTextField()
    {
        return theme().newTextField();
    }

    public JTextField newTextField(final int characters)
    {
        return theme().newTextField(characters);
    }

    public JSeparator newVerticalSeparator()
    {
        return theme().newVerticalSeparator();
    }

    public JSplitPane newVerticalSplitPane(final JPanel top, final JPanel bottom)
    {
        return theme().newVerticalSplitPane(top, bottom);
    }

    public void popupInformation(final JFrame frame, final String title, final String message)
    {
        theme().popupInformation(frame, title, message);
    }

    public Font smallComponentFont()
    {
        return theme().fontSmall();
    }

    public Font tableFont()
    {
        return theme().tableFont();
    }

    protected KivaKitTheme theme()
    {
        return KivaKitTheme.get();
    }

    protected Box verticalCenter(final JComponent component)
    {
        final Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(component);
        return verticalBox;
    }
}
