package com.telenav.kivakit.ui.desktop.component.progress;

import com.telenav.kivakit.kernel.interfaces.code.Callback;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.desktop.layout.Size;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.*;
import java.awt.*;

import static com.telenav.kivakit.ui.desktop.layout.Spacing.MANUAL_SPACING;

/**
 * @author jonathanl (shibo)
 */
public class ProgressPanel extends KivaKitPanel
{
    public enum CompletionStatus
    {
        CANCELLED,
        COMPLETED
    }

    public ProgressPanel(final ProgressReporter reporter, final int width, final Callback<CompletionStatus> done)
    {
        // Get the KivaKit UI theme,
        final var theme = KivaKitTheme.get();

        // create the progress bar,
        final var progressBar = theme.newProgressBar();
        Size.widthOf(width).preferred(progressBar);
        progressBar.setMaximum(100);

        // and the cancel button
        final var cancel = theme.newButton("cancel", ignored -> done.callback(CompletionStatus.CANCELLED));

        // then add them to this panel
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        Size.heightOf(32).preferred(this);
        new HorizontalBoxLayout(this, MANUAL_SPACING)
                .add(progressBar)
                .add(new JLabel(" "))
                .add(cancel);

        // and update the progress bar as the reporter reports progress.
        reporter.listener(at ->
        {
            final var percentComplete = (int) at.asZeroToOne();
            progressBar.setValue(percentComplete);
            progressBar.setString(Message.format("$%", percentComplete));
            progressBar.setStringPainted(false);
            if (at.equals(Percent._100))
            {
                done.callback(CompletionStatus.COMPLETED);
            }
        });
    }
}
