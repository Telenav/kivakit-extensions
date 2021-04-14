package com.telenav.kivakit.ui.swing.component.panel.output;

import com.telenav.kivakit.core.kernel.language.strings.StringTo;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.ui.swing.component.KivaKitPanel;
import com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors;
import com.telenav.kivakit.ui.swing.graphics.font.Fonts;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

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
            stylesheet.addRule("body           { color: " + KivaKitColors.WHITE.toHex() + ";      font-weight: 900; font-size: 10px; font-family: Avenir,Helvetica; white-space:nowrap; }");
            stylesheet.addRule(".aqua          { color: " + KivaKitColors.AQUA.toHex() + ";       font-weight: 600; font-size: 10px; }");
            stylesheet.addRule(".section0      { color: " + KivaKitColors.TANGERINE.toHex() + ";  font-weight: 900; font-size: 11px; }");
            stylesheet.addRule(".section1      { color: " + KivaKitColors.LIME.toHex() + ";       font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".section2      { color: " + KivaKitColors.LIGHT_GRAY.toHex() + "; font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".section3      { color: " + KivaKitColors.LIGHT_GRAY.toHex() + "; font-weight: 900; font-size: 10px; }");
            stylesheet.addRule(".label         { color: " + KivaKitColors.LIGHT_GRAY.toHex() + "; font-weight: 800; font-size: 10px } ");
            stylesheet.addRule(".value         { color: " + KivaKitColors.WHITE.toHex() + ";      font-weight: 800; font-size: 10px; }");
            stylesheet.addRule(".not-available { color: " + KivaKitColors.FOSSIL.toHex() + ";       font-weight: 800; font-size: 10px; }");
        }

        final var document = kit.createDefaultDocument();

        final var theme = KivaKitTheme.get();
        output = (JTextPane) theme.configure(new JTextPane());
        output.setEditorKit(kit);
        theme.colorTextAreaBackground().background(output);
        theme.colorTextFieldText().foreground(output);
        output.setSelectedTextColor(theme.colorSelectionText().asAwtColor());
        output.setSelectionColor(theme.colorSelectionBackground().asAwtColor());
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
