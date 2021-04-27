package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.*;

/**
 * @author jonathanl (shibo)
 */
public class Layouts
{
    public static Box leftJustify(final JComponent component)
    {
        final Box box = Box.createHorizontalBox();
        box.add(component);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public static Box rightJustify(final JComponent component)
    {
        final Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        return box;
    }
}
