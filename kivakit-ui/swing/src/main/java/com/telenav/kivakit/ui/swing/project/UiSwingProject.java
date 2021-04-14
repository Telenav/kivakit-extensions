package com.telenav.kivakit.ui.swing.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class UiSwingProject extends Project
{
    private static final Lazy<UiSwingProject> singleton = Lazy.of(UiSwingProject::new);

    public static UiSwingProject get()
    {
        return singleton.get();
    }

    protected UiSwingProject()
    {
    }
}
