package com.telenav.kivakit.ui.swing.component.status;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.ui.swing.component.health.HealthPanel;
import com.telenav.kivakit.ui.swing.component.icon.logo.kivakit.KivaKitLogo;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.swing.layout.Margins;
import com.telenav.kivakit.ui.swing.layout.Size;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.telenav.kivakit.ui.swing.component.icon.logo.kivakit.KivaKitLogo.Size._32x32;
import static com.telenav.kivakit.ui.swing.layout.Spacing.MANUAL_SPACING;

/**
 * @author jonathanl (shibo)
 */
public class StatusPanel extends JPanel implements StatusDisplay, Listener
{
    public enum Display
    {
        SHOW_HEALTH_PANEL,
        NO_HEALTH_PANEL
    }

    private final JLabel status = theme().configureComponentLabel(new JLabel("Ready"));

    private final HealthPanel healthPanel = new HealthPanel();

    public StatusPanel(final Display type)
    {
        final var logo = new KivaKitLogo(_32x32);
        Margins.of(5).apply(logo);

        theme().configureContainerPanel(this);

        Margins.leftAndRightOf(10).bottom(5).apply(this);

        Size.heightOf(40).preferred(this).maximum(this);

        if (type == Display.SHOW_HEALTH_PANEL)
        {
            new HorizontalBoxLayout(this, MANUAL_SPACING)
                    .add(logo)
                    .space(10)
                    .add(status)
                    .horizontalGlue()
                    .add(healthPanel);
        }
        else
        {
            new HorizontalBoxLayout(this, MANUAL_SPACING)
                    .add(logo)
                    .space(10)
                    .add(status);
        }
    }

    @Override
    public void onMessage(final Message message)
    {
        if (message instanceof Problem)
        {
            status(Duration.MAXIMUM, theme().colorError(), message.description());
        }
        if (message instanceof Warning)
        {
            status(Duration.seconds(15), theme().colorWarning(), message.description());
        }
        if (message instanceof Announcement)
        {
            status(theme().colorText(), message.description());
        }
    }

    public void status(final Color color, final String message, final Object... arguments)
    {
        status(Duration.seconds(15), color, message, arguments);
    }

    public void status(final Duration stayFor, final Color color, final String message, final Object... arguments)
    {
        trace(message, arguments);
        final var formatted = Message.format(message, arguments);
        if (!status.getText().equals(formatted))
        {
            color.foreground(status);
            status.setText(formatted);
            final var timer = new Timer();
            linger(stayFor, formatted, timer);
        }
    }

    @Override
    public void status(final Duration stayFor, final String message, final Object... arguments)
    {
        status(stayFor, theme().colorText(), message, arguments);
    }

    public void update(final JavaVirtualMachineHealth health)
    {
        healthPanel.update(health);
    }

    protected KivaKitTheme theme()
    {
        return KivaKitTheme.get();
    }

    private void linger(final Duration stayFor, final String formatted, final Timer timer)
    {
        if (stayFor != null && !stayFor.equals(Duration.MAXIMUM))
        {
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    if (status.getText().equals(formatted))
                    {
                        status.setText("");
                    }
                    timer.cancel();
                }
            }, stayFor.asMilliseconds());
        }
    }
}
