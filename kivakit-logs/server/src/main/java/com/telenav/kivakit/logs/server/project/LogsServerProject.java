package com.telenav.kivakit.logs.server.project;

import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.KernelProject;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.network.core.project.NetworkCoreProject;
import com.telenav.kivakit.resource.project.ResourceProject;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.serialization.kryo.KryoTypes;

import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class LogsServerProject extends Project
{
    private static final Lazy<LogsServerProject> singleton = Lazy.of(LogsServerProject::new);

    private static final KryoTypes KRYO_TYPES = new CoreKernelKryoTypes()
            .mergedWith(new LogsServerKryoTypes());

    public static LogsServerProject get()
    {
        return singleton.get();
    }

    private LogsServerProject()
    {
    }

    @Override
    public Set<Project> dependencies()
    {
        return Set.of(KernelProject.get(), ResourceProject.get(), NetworkCoreProject.get());
    }

    public SerializationSessionFactory sessionFactory()
    {
        return KRYO_TYPES.sessionFactory();
    }
}
