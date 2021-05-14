package com.telenav.kivakit.math.project;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class MathProject extends Project
{
    private static final Lazy<MathProject> project = Lazy.of(MathProject::new);

    public static MathProject get()
    {
        return project.get();
    }

    protected MathProject()
    {
    }
}
