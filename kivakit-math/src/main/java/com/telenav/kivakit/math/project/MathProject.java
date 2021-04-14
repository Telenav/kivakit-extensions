package com.telenav.kivakit.math.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class MathProject extends Project
{
    private static final Lazy<MathProject> singleton = Lazy.of(MathProject::new);

    public static MathProject get()
    {
        return singleton.get();
    }

    protected MathProject()
    {
    }
}
