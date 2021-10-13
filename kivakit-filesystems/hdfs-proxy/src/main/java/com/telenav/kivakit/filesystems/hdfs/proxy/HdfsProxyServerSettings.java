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

package com.telenav.kivakit.filesystems.hdfs.proxy;

import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystems.hdfs.proxy.converters.UserGroupInformationConverter;
import com.telenav.kivakit.filesystems.hdfs.proxy.project.lexakai.diagrams.DiagramHdfsProxy;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * <b>Not public API</b>
 * <p>
 * This class holds settings that are used by {@link HdfsFileSystem} to establish a connection to HDFS. The {@link
 * HdfsProxyServer} application uses command line switches to populate this object and register it with {@link
 * Settings#registerSettings(Object)}. The {@link HdfsFileSystem} class then looks up the settings with {@link
 * Settings#requireSettings(Class)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfsProxy.class)
public class HdfsProxyServerSettings
{
    /** User information */
    private UserGroupInformation user;

    /** Container of HDFS site configuration resources */
    private ResourceFolder configurationFolder;

    public ResourceFolder configurationFolder()
    {
        return configurationFolder;
    }

    @KivaKitPropertyConverter(ResourceFolder.Converter.class)
    public HdfsProxyServerSettings configurationFolder(final ResourceFolder configuration)
    {
        configurationFolder = configuration;
        return this;
    }

    public UserGroupInformation user()
    {
        return user;
    }

    @KivaKitPropertyConverter(UserGroupInformationConverter.class)
    public HdfsProxyServerSettings user(final UserGroupInformation user)
    {
        this.user = user;
        return this;
    }
}
