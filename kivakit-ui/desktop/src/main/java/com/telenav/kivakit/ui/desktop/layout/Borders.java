package com.telenav.kivakit.ui.desktop.layout;

import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.util.ArrayList;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * @author jonathanl (shibo)
 */
public class Borders
{
    public static Borders create()
    {
        return new Borders();
    }

    public static Borders insideMarginsOf(final Margins margins)
    {
        return create().insideMargins(margins);
    }

    public static Borders line()
    {
        return create().line(KivaKitTheme.get().colorBorder());
    }

    public static Borders lineOf(final Color color)
    {
        return create().line(color);
    }

    public static Borders outsideMarginsOf(final Margins margins)
    {
        return create().insideMargins(margins);
    }

    private Color color;

    private Margins inside;

    private Margins outside;

    protected Borders()
    {
    }

    public Border apply(final JComponent component)
    {
        final var borders = new ArrayList<Border>();
        if (inside != null)
        {
            borders.add(inside.border());
        }
        if (color != null)
        {
            borders.add(BorderFactory.createLineBorder(color.asAwtColor(), 1, true));
        }
        if (outside != null)
        {
            borders.add(outside.border());
        }
        switch (borders.size())
        {
            case 1:
                return borders.get(0);

            case 2:
                return new CompoundBorder(borders.get(0), borders.get(1));

            case 3:
                return new CompoundBorder(new CompoundBorder(borders.get(0), borders.get(1)), borders.get(2));

            case 0:
            default:
                return fail("Borders object needs a line color, an inside margin or an outside margin");
        }
    }

    public Borders insideMargins(final Margins inside)
    {
        this.inside = inside;
        return this;
    }

    public Borders line(final Color color)
    {
        this.color = color;
        return this;
    }

    public Borders outsideMargins(final Margins outside)
    {
        this.outside = outside;
        return this;
    }
}
