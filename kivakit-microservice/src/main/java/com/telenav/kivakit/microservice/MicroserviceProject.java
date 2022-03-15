package com.telenav.kivakit.microservice;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.settings.SettingsProject;

import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link ProjectTrait#project(Class)}.
 *
 * @author jonathanl (shibo)
 */
public class MicroserviceProject extends Project
{
    @Override
    public ObjectSet<Class<? extends Project>> dependencies()
    {
        return objectSet(SettingsProject.class);
    }
}
