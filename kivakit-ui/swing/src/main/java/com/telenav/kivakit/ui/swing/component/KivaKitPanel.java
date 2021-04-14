package com.telenav.kivakit.ui.swing.component;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.interfaces.value.Source;
import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitPanel extends JPanel implements Repeater
{
    private final Lazy<BaseRepeater> repeater = Lazy.of(() -> new BaseRepeater("kivakit-panel-repeater", getClass()));

    public KivaKitPanel()
    {
        theme().configureContainerPanel(this);
    }

    public KivaKitPanel(final LayoutManager layout)
    {
        super(layout);
    }

    @Override
    public void addListener(final Listener listener)
    {
        repeater.get().addListener(listener);
    }

    @Override
    public void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        repeater.get().addListener(listener, filter);
    }

    @Override
    public void clearListeners()
    {
        repeater.get().clearListeners();
    }

    public Color colorBorder()
    {
        return theme().colorBorder();
    }

    public Color colorCaret()
    {
        return theme().colorCaret();
    }

    public Color colorComponentLabel()
    {
        return theme().colorComponentLabel();
    }

    public Color colorDropdownBackground()
    {
        return theme().colorDropdownBackground();
    }

    public Color colorDropdownText()
    {
        return theme().colorDropdownText();
    }

    public Color colorError()
    {
        return theme().colorError();
    }

    public Color colorFadedText()
    {
        return theme().colorFadedText();
    }

    public Color colorInformation()
    {
        return theme().colorInformation();
    }

    public Color colorInformationLabel()
    {
        return theme().colorInformationLabel();
    }

    public Color colorLabel()
    {
        return theme().colorComponentLabel();
    }

    public Color colorNote()
    {
        return theme().colorNote();
    }

    public Color colorProgressBarBackground()
    {
        return theme().colorProgressBarBackground();
    }

    public Color colorProgressBarForeground()
    {
        return theme().colorProgressBarForeground();
    }

    public Color colorQuibble()
    {
        return theme().colorQuibble();
    }

    public Color colorSelectionBackground()
    {
        return theme().colorSelectionBackground();
    }

    public Color colorSelectionText()
    {
        return theme().colorSelectionText();
    }

    public Color colorSeparator()
    {
        return theme().colorSeparator();
    }

    public Color colorShadedPanel()
    {
        return theme().colorShadedPanel();
    }

    public Color colorShadedSubPanel()
    {
        return theme().colorShadedSubPanel();
    }

    public Color colorSuccess()
    {
        return theme().colorSuccess();
    }

    public Color colorTableBackground()
    {
        return theme().colorTableBackground();
    }

    public Color colorTableHeaderBackground()
    {
        return theme().colorTableHeaderBackground();
    }

    public Color colorTableHeaderText()
    {
        return theme().colorTableHeaderText();
    }

    public Color colorTableText()
    {
        return theme().colorTableText();
    }

    public Color colorText()
    {
        return theme().colorText();
    }

    public Color colorTextAreaBackground()
    {
        return theme().colorTextAreaBackground();
    }

    public Color colorTextFieldBackground()
    {
        return theme().colorTextFieldBackground();
    }

    public Color colorTextFieldText()
    {
        return theme().colorTextFieldText();
    }

    public Color colorTitle()
    {
        return theme().colorTitle();
    }

    public Color colorTitleBackground()
    {
        return theme().colorTitleBackground();
    }

    public Color colorWarning()
    {
        return theme().colorWarning();
    }

    public JButton configure(final JButton button)
    {
        return theme().configure(button);
    }

    public JTableHeader configure(final JTableHeader header)
    {
        return theme().configure(header);
    }

    public JLabel configure(final JLabel label)
    {
        return theme().configureComponentLabel(label);
    }

    public <T> JList<T> configure(final JList<T> list)
    {
        return theme().configure(list);
    }

    public JEditorPane configure(final JEditorPane editor)
    {
        return theme().configure(editor);
    }

    public JTextField configure(final JTextField field)
    {
        return theme().configure(field);
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown)
    {
        return theme().configure(dropdown);
    }

    public <T> JComboBox<T> configure(final JComboBox<T> dropdown, final int preferredWidth)
    {
        return theme().configure(dropdown, preferredWidth);
    }

    public JTable configure(final JTable table)
    {
        return theme().configure(table);
    }

    public JCheckBox configure(final JCheckBox checkbox)
    {
        return theme().configure(checkbox);
    }

    public JLabel configureComponentLabel(final JLabel label)
    {
        return theme().configureComponentLabel(label);
    }

    public JPanel configureContainerPanel(final JPanel panel)
    {
        return theme().configureContainerPanel(panel);
    }

    public JTextField configureSearchField(final JTextField field)
    {
        return theme().configureSearchField(field);
    }

    public KivaKitPanel configureShadedPanel(final KivaKitPanel panel)
    {
        return theme().configureShadedPanel(panel);
    }

    public KivaKitPanel configureShadedSubPanel(final KivaKitPanel panel)
    {
        return theme().configureShadedSubPanel(panel);
    }

    @Override
    public CodeContext debugCodeContext()
    {
        return repeater.get().debugCodeContext();
    }

    @Override
    public void debugCodeContext(final CodeContext context)
    {
        repeater.get().debugCodeContext(context);
    }

    public void debugColor()
    {
        setOpaque(true);
        Components.debugColor(this);
    }

    public Font fixedWidthFont()
    {
        return theme().fontFixedWidth();
    }

    public Font fontComponent()
    {
        return theme().fontComponent();
    }

    public Font fontFixedWidth()
    {
        return theme().fontFixedWidth();
    }

    public Font fontTitle()
    {
        return theme().fontTitle();
    }

    @Override
    public boolean hasListeners()
    {
        return repeater.get().hasListeners();
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

    @Override
    public void onMessage(final Message message)
    {
        repeater.get().onMessage(message);
    }

    public void popupInformation(final JFrame frame, final String title, final String message)
    {
        theme().popupInformation(frame, title, message);
    }

    public Color progressBarBackground()
    {
        return theme().colorProgressBarBackground();
    }

    @Override
    public void removeListener(final Listener listener)
    {
        repeater.get().removeListener(listener);
    }

    public Font smallComponentFont()
    {
        return theme().smallComponentFont();
    }

    public Font tableFont()
    {
        return theme().tableFont();
    }

    @Override
    public void transmit(final Message message)
    {
        repeater.get().transmit(message);
    }

    protected Box verticalCenter(final JComponent component)
    {
        final Box verticalBox = Box.createVerticalBox();
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(component);
        return verticalBox;
    }

    private KivaKitTheme theme()
    {
        return KivaKitTheme.get();
    }
}
