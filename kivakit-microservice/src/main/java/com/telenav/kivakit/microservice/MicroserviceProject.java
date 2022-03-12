package com.telenav.kivakit.microservice;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;

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
    public ObjectSet<Project> dependencies()
    {
        return ObjectSet.objectSet(project(GsonSerializationProject.class));
    }
}
