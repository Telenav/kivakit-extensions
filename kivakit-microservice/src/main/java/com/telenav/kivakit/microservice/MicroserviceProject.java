package com.telenav.kivakit.microservice;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;

public class MicroserviceProject extends Project
{
    @Override
    public ObjectSet<Project> dependencies()
    {
        return ObjectSet.objectSet(new GsonSerializationProject());
    }
}
