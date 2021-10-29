package com.telenav.kivakit.ui.desktop.component.panel.stack;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.CardLayout;

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

    public void addCard(JComponent card, String name)
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

    public void show(String name)
    {
        cardLayout.show(this, name);
    }
}
