package com.telenav.kivakit.ui.desktop.project;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class UiSwingProject extends Project
{
    private static final Lazy<UiSwingProject> project = Lazy.of(UiSwingProject::new);

    public static UiSwingProject get()
    {
        return project.get();
    }

    protected UiSwingProject()
    {
    }
}
