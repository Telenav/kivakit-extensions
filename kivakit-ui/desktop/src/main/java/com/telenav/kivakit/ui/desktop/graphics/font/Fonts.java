package com.telenav.kivakit.ui.desktop.graphics.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static java.awt.font.TextAttribute.FAMILY;
import static java.awt.font.TextAttribute.SIZE;
import static java.awt.font.TextAttribute.WEIGHT;
import static java.awt.font.TextAttribute.WEIGHT_LIGHT;

/**
 * @author jonathanl (shibo)
 */
public class Fonts
{
    public static Font component(final int size)
    {
        final Map<TextAttribute, Object> attributes = new HashMap<>();

        attributes.put(FAMILY, "Open Sans,Avenir,Nunito,Arial,Helvetica,SansSerif");
        attributes.put(WEIGHT, WEIGHT_LIGHT);
        attributes.put(SIZE, size);

        return Font.getFont(attributes);
    }

    public static Font fixedWidth(final int style, final int size)
    {
        final var fonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (final var at : new String[]
                {
                        "Monaco",
                        "Consolas",
                        "Lucida Console",
                        "Menlo",
                        "Sans-Serif",
                        "Monospaced"
                })
        {
            if (fonts.contains(at))
            {
                return new Font(at, style, size);
            }
        }
        return fail("Could not find an acceptable fixed width font");
    }
}
