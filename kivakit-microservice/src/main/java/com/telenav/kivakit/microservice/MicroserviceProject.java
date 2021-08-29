package com.telenav.kivakit.microservice;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;

public class MicroserviceProject extends Project
{
    private static final Lazy<MicroserviceProject> SINGLETON = Lazy.of(MicroserviceProject::new);

    public static MicroserviceProject get()
    {
        return SINGLETON.get();
    }

    protected MicroserviceProject()
    {
    }
}
