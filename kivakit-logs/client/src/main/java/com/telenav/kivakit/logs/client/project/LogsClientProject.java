////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.client.project;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.collections.set.Sets;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.project.Project;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.kivakit.serialization.kryo.CoreKernelKryoTypes;
import com.telenav.kivakit.serialization.kryo.KryoTypes;
import com.telenav.mesakit.core.MesaKit;

import java.util.Set;

public class LogsClientProject extends Project
{
    private static final Lazy<LogsClientProject> project = Lazy.of(LogsClientProject::new);

    private static final KryoTypes KRYO_TYPES = new CoreKernelKryoTypes();

    public static LogsClientProject get()
    {
        return project.get();
    }

    protected LogsClientProject()
    {
    }

    @Override
    public Set<Project> dependencies()
    {
        return Sets.of();
    }

    /**
     * @return The folder where various kinds of data are cached
     */
    public Folder mesakitMapFolder()
    {
        return MesaKit.get().cacheFolder()
                .folder("map")
                .mkdirs();
    }

    public SerializationSessionFactory sessionFactory()
    {
        return KRYO_TYPES.sessionFactory();
    }
}
