package com.telenav.kivakit.ui.swing.component;

import com.telenav.kivakit.core.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.ui.swing.graphics.color.Color;
import com.telenav.kivakit.ui.swing.graphics.color.KivaKitColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author jonathanl (shibo)
 */
public class Components
{
    private static final Color[] DEBUG_COLORS = { KivaKitColors.CHERRY, KivaKitColors.AQUAMARINE, KivaKitColors.LIME };

    private static int debugColor;

    public static void children(final Container container, final Consumer<Component> consumer)
    {
        for (final var component : container.getComponents())
        {
            consumer.accept(component);
            if (component instanceof Container)
            {
                children((Container) component, consumer);
            }
        }
    }

    public static <T extends Component> T debugColor(final T component)
    {
        DEBUG_COLORS[debugColor++ % DEBUG_COLORS.length].background(component);
        return component;
    }

    public static void fadeIn(final Component component, final Color color, final Duration update, final int step)
    {
        final var alphaVariable = new MutableCount(0);
        color.withAlpha(0).background(component);
        final var completed = new CompletionLatch();
        update.every(timer ->
        {
            final var alpha = alphaVariable.get();
            final var newAlpha = Math.min(255, alpha + step);
            alphaVariable.set(newAlpha);
            color.withAlpha((int) newAlpha).background(component);
            if (newAlpha == 255)
            {
                timer.cancel();
                completed.completed();
            }
        });
        completed.waitForCompletion();
    }

    public static void fadeOut(final Component component, final Color color, final Duration update, final int step)
    {
        final var alphaVariable = new MutableCount(255);
        color.withAlpha(255).background(component);
        final var completed = new CompletionLatch();
        update.every(timer ->
        {
            final var alpha = alphaVariable.get();
            final var newAlpha = Math.max(0, alpha - step);
            alphaVariable.set(newAlpha);
            color.withAlpha((int) newAlpha).background(component);
            if (newAlpha == 0)
            {
                timer.cancel();
                completed.completed();
            }
        });
        completed.waitForCompletion();
    }

    public static <T> java.util.List<T> items(final JComboBox<T> dropdown)
    {
        final int count = dropdown.getItemCount();
        final var items = new ArrayList<T>();
        for (int index = 0; index < count; index++)
        {
            items.add(dropdown.getItemAt(index));
        }
        return items;
    }
}
