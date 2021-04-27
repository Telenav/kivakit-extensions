package com.telenav.kivakit.ui.desktop.component.panel.stack;

import com.telenav.kivakit.core.kernel.language.time.Duration;

import javax.swing.*;
import java.awt.*;

/**
 * @author jonathanl (shibo)
 */
public class FlashCardPanel extends JPanel
{
    private final CardLayout cardLayout;

    public FlashCardPanel()
    {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    public void start(final Duration delay)
    {
        delay.every(timer -> cardLayout.next(this));
    }
}
