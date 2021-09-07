////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry;

import com.telenav.kivakit.collections.CollectionsProject;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.serialization.kryo.KryoTypes;
import com.telenav.kivakit.service.registry.project.ServiceRegistryKryoTypes;

/**
 * The project class for kivakit-service-registry.
 *
 * @author jonathanl (shibo)
 */
public class ServiceRegistryProject extends Project
{
    private static final KryoTypes KRYO_TYPES = new ServiceRegistryKryoTypes()
            .mergedWith(new CoreKernelKryoTypes());

    private static final Lazy<ServiceRegistryProject> project = Lazy.of(ServiceRegistryProject::new);

    public static ServiceRegistryProject get()
    {
        return project.get();
    }

    private ServiceRegistryProject()
    {
        SerializationSessionFactory.threadLocal(KRYO_TYPES.sessionFactory());
    }

    @Override
    public ObjectSet<Project> dependencies()
    {
        return ObjectSet.of(CollectionsProject.get());
    }
}