package com.telenav.kivakit.ui.desktop.component;

import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.ui.desktop.graphics.drawing.style.Color;
import com.telenav.kivakit.ui.desktop.theme.KivaKitColors;

import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author jonathanl (shibo)
 */
public class Components
{
    private static final Color[] DEBUG_COLORS = { KivaKitColors.MARASCHINO, KivaKitColors.AQUAMARINE,
            KivaKitColors.LIME };

    private static int debugColor;

    public static void children(Container container, Consumer<Component> consumer)
    {
        for (var component : container.getComponents())
        {
            consumer.accept(component);
            if (component instanceof Container)
            {
                children((Container) component, consumer);
            }
        }
    }

    public static <T extends Component> T debugColor(T component)
    {
        DEBUG_COLORS[debugColor++ % DEBUG_COLORS.length].applyAsBackground(component);
        return component;
    }

    public static void fadeIn(Component component, Color color, Duration update, int step)
    {
        var alphaVariable = new MutableCount(0);
        color.withAlpha(0).applyAsBackground(component);
        var completed = new CompletionLatch();
        update.every(timer ->
        {
            var alpha = alphaVariable.get();
            var newAlpha = Math.min(255, alpha + step);
            alphaVariable.set(newAlpha);
            color.withAlpha((int) newAlpha).applyAsBackground(component);
            if (newAlpha == 255)
            {
                timer.cancel();
                completed.completed();
            }
        });
        completed.waitForCompletion();
    }

    public static void fadeOut(Component component, Color color, Duration update, int step)
    {
        var alphaVariable = new MutableCount(255);
        color.withAlpha(255).applyAsBackground(component);
        var completed = new CompletionLatch();
        update.every(timer ->
        {
            var alpha = alphaVariable.get();
            var newAlpha = Math.max(0, alpha - step);
            alphaVariable.set(newAlpha);
            color.withAlpha((int) newAlpha).applyAsBackground(component);
            if (newAlpha == 0)
            {
                timer.cancel();
                completed.completed();
            }
        });
        completed.waitForCompletion();
    }

    public static <T> java.util.List<T> items(JComboBox<T> dropdown)
    {
        int count = dropdown.getItemCount();
        var items = new ArrayList<T>();
        for (int index = 0; index < count; index++)
        {
            items.add(dropdown.getItemAt(index));
        }
        return items;
    }
}
