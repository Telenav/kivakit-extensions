package com.telenav.kivakit.logs.client.project;

import com.telenav.kivakit.logs.client.view.ClientLogPanel;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;
import com.telenav.kivakit.ui.desktop.theme.darcula.KivaKitDarculaTheme;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author jonathanl (shibo)
 */
public class LogsClientTheme
{
    public static void configure(JFrame frame, ClientLogPanel panel, Image icon)
    {
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setLocation(100, 100);
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(new Dimension(screen.width * 4 / 5, screen.height * 4 / 5));
        frame.setMinimumSize(new Dimension(1000, 500));
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                panel.log().exit();
            }
        });
        frame.setIconImage(icon);
        frame.pack();
        frame.setVisible(true);
    }

    public static void initialize()
    {
        KivaKitTheme.set(new KivaKitDarculaTheme());
    }
}
