package com.telenav.kivakit.ui.desktop.component.progress;

import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.ui.desktop.component.KivaKitPanel;
import com.telenav.kivakit.ui.desktop.layout.HorizontalBoxLayout;
import com.telenav.kivakit.ui.desktop.layout.Size;
import com.telenav.kivakit.ui.desktop.theme.KivaKitTheme;

import javax.swing.JLabel;
import java.awt.FlowLayout;

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

    public ProgressPanel(ProgressReporter reporter, int width, Callback<CompletionStatus> done)
    {
        // Get the KivaKit UI theme,
        var theme = KivaKitTheme.get();

        // create the progress bar,
        var progressBar = theme.newProgressBar();
        Size.widthOf(width).preferred(progressBar);
        progressBar.setMaximum(100);

        // and the cancel button
        var cancel = theme.newButton("cancel", ignored -> done.callback(CompletionStatus.CANCELLED));

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
            var percentComplete = (int) at.asZeroToOne();
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
