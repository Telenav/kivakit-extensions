package com.telenav.kivakit.ui.desktop;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class DesktopUiProject extends Project
{
    private static final Lazy<DesktopUiProject> project = Lazy.of(DesktopUiProject::new);

    public static DesktopUiProject get()
    {
        return project.get();
    }

    protected DesktopUiProject()
    {
    }
}
