package com.telenav.kivakit.ui.desktop.layout;

import javax.swing.Box;
import javax.swing.JComponent;

/**
 * @author jonathanl (shibo)
 */
public class Layouts
{
    public static Box leftJustify(JComponent component)
    {
        Box box = Box.createHorizontalBox();
        box.add(component);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public static Box rightJustify(JComponent component)
    {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(component);
        return box;
    }
}
