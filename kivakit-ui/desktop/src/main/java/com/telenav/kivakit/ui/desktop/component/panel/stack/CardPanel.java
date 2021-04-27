package com.telenav.kivakit.ui.desktop.component.panel.stack;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class CardPanel extends JPanel
{
    private final CardLayout cardLayout;

    public CardPanel()
    {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    public void addCard(final JComponent card, final String name)
    {
        add(card);
        cardLayout.addLayoutComponent(card, name);
    }

    public void first()
    {
        cardLayout.first(this);
    }

    public void last()
    {
        cardLayout.last(this);
    }

    public void next()
    {
        cardLayout.next(this);
    }

    public void show(final String name)
    {
        cardLayout.show(this, name);
    }
}
