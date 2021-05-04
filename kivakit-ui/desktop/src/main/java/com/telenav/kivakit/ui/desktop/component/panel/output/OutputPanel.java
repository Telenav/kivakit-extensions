package com.telenav.kivakit.ui.desktop.component.panel.output;

import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Fonts;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.theme.KivaKitColors;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * @author jonathanl (shibo)
 */
public class OutputPanel extends KivaKitPanel
{
    public enum Type
    {
        VARIABLE_WIDTH,
        FIXED_WIDTH
    }

    private final JTextPane output;

    public OutputPanel(final Type type)
    {
        setMinimumSize(new Dimension(0, 200));

        final var kit = new HTMLEditorKit();

        final StyleSheet stylesheet = kit.getStyleSheet();

        if (type == Type.VARIABLE_WIDTH)
        {
            stylesheet.addRule("body           { color: " + KivaKitColors.WHITE.asHexString() + ";      font-weight: 900; font-size: 10px; font-family: Avenir,Helvetica; white-space:nowrap; }");
            stylesheet.addRule(".aqua          { color: " + KivaKitColors.AQUA.asHexString() + ";       font-weight: 600; font-size: 10px; }");
            stylesheet.addRule(".section0      { color: " + KivaKitColors.TANGERINE.asHexString() + ";  font-weight: 900; font-size: 11px; }");
            stylesheet.addRule(".section1      { color: " + KivaKitColors.LIME.asHexString() + ";       font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".section2      { color: " + KivaKitColors.LIGHT_GRAY.asHexString() + "; font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".section3      { color: " + KivaKitColors.LIGHT_GRAY.asHexString() + "; font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".label         { color: " + KivaKitColors.LIGHT_GRAY.asHexString() + "; font-weight: 800; font-size: 10px } ");
            stylesheet.addRule(".value         { color: " + KivaKitColors.WHITE.asHexString() + ";      font-weight: 800; font-size: 10px; }");
            stylesheet.addRule(".not-available { color: " + KivaKitColors.FOSSIL.asHexString() + ";       font-weight: 800; font-size: 10px; }");
        }

        final var document = kit.createDefaultDocument();

        final var theme = theme();
        output = (JTextPane) theme.applyTo(new JTextPane());
        theme.styleTextArea().applyToSelectionStyle(output);
        output.setEditorKit(kit);
        output.setDocument(document);
        output.setEditable(false);

        if (type == Type.FIXED_WIDTH)
        {
            output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            output.setFont(Fonts.fixedWidth(Font.PLAIN, 12));
        }

        Margins.of(10).apply(output);

        setLayout(new BorderLayout());
        add(scrollPane(), BorderLayout.CENTER);
    }

    public void font(final Font font)
    {
        output.setFont(font);
    }

    public void html(final String html, final Object... arguments)
    {
        SwingUtilities.invokeLater(() ->
        {
            output.setText("<html><body>" + Message.format(html, arguments) + "</body></html>");
            output.setCaretPosition(0);
        });
    }

    public void text(final String text, final Object... arguments)
    {
        html(StringTo.html(Message.format(text, arguments)));
    }

    private JScrollPane scrollPane()
    {
        return new JScrollPane(output, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
}
