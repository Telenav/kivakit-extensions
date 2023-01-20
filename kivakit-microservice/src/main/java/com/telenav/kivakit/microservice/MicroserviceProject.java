package com.telenav.kivakit.microservice;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.BaseKivaKitProject;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.settings.SettingsProject;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link ProjectTrait#project(Class)}.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MicroserviceProject extends BaseKivaKitProject
{
    @Override
    public ObjectSet<Class<? extends Project>> dependencies()
    {
        return set(SettingsProject.class);
    }
}
