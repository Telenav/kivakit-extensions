package com.telenav.kivakit.ui.desktop.component.status;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachineHealth;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.status.Announcement;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.ui.desktop.component.health.HealthPanel;
import com.telenav.kivakit.ui.desktop.component.icon.logo.kivakit.KivaKitLogo;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Style;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.desktop.layout.Margins;
import com.telenav.kivakit.ui.desktop.layout.Size;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Timer;
import java.util.TimerTask;

import static com.telenav.kivakit.ui.desktop.component.icon.logo.kivakit.KivaKitLogo.Size._32x32;
import static com.telenav.kivakit.ui.desktop.layout.Spacing.MANUAL_SPACING;

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

    private final JLabel status = theme().applyToComponentLabel(new JLabel("Ready"));

    private final HealthPanel healthPanel = new HealthPanel();

    public StatusPanel(Display type)
    {
        var logo = new KivaKitLogo(_32x32);
        Margins.of(5).apply(logo);

        theme().applyToContainerPanel(this);

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
    public void onMessage(Message message)
    {
        var style = theme().styleMessage(message.getClass());
        if (message instanceof Problem)
        {
            status(Duration.MAXIMUM, style, message.description());
        }
        if (message instanceof Warning)
        {
            status(Duration.seconds(15), style, message.description());
        }
        if (message instanceof Announcement)
        {
            status(style, message.description());
        }
    }

    public void status(Style color, String message, Object... arguments)
    {
        status(Duration.seconds(15), color, message, arguments);
    }

    public void status(Duration stayFor, Style color, String message, Object... arguments)
    {
        trace(message, arguments);
        var formatted = Message.format(message, arguments);
        if (!status.getText().equals(formatted))
        {
            color.apply(status);
            status.setText(formatted);
            var timer = new Timer();
            linger(stayFor, formatted, timer);
        }
    }

    @Override
    public void status(Duration stayFor, String message, Object... arguments)
    {
        status(stayFor, theme().styleMessage(Information.class), message, arguments);
    }

    public void update(JavaVirtualMachineHealth health)
    {
        healthPanel.update(health);
    }

    protected KivaKitTheme theme()
    {
        return KivaKitTheme.get();
    }

    private void linger(Duration stayFor, String formatted, Timer timer)
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
