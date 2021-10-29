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

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

/**
 * Settings that must be provided by the user of {@link HdfsFileSystemService} via {@link
 * Settings#registerSettings(Object)}.
 *
 * <p><b>Settings</b></p>
 *
 * <ul>
 *     <li>{@link #configurationFolder(ResourceFolder)} - A resource folder containing hdfs-site.xml and related configuration files</li>
 *     <li>{@link #proxyJar(Resource)} - The location of the *kivakit-filesystem-hdfs-project* executable JAR file to allow it to be launched</li>
 *     <li>{@link #contactEmail()} - Contact email for RMI registry entry</li>
 *     <li>{@link #username(String)} - The username to use when accessing HDFS</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlNotPublicApi
@UmlClassDiagram(diagram = DiagramHdfs.class)
public class HdfsSettings
{
    /** Proxy executable JAR file */
    private Resource proxyJar;

    /** Contact email for proxy's RMI and data services */
    private EmailAddress contactEmail;

    /** User information */
    private String username;

    /** The name of the HDFS cluster */
    private String clusterName;

    /** Container of HDFS site configuration resources */
    private ResourceFolder configurationFolder;

    public String clusterName()
    {
        return clusterName;
    }

    @KivaKitPropertyConverter
    public HdfsSettings clusterName(String clusterName)
    {
        this.clusterName = clusterName;
        return this;
    }

    @KivaKitPropertyConverter(ResourceFolder.Converter.class)
    public HdfsSettings configurationFolder(ResourceFolder configuration)
    {
        configurationFolder = configuration;
        return this;
    }

    @KivaKitPropertyConverter(EmailAddress.Converter.class)
    public HdfsSettings contactEmail(EmailAddress contactEmail)
    {
        this.contactEmail = contactEmail;
        return this;
    }

    @KivaKitPropertyConverter(Resource.Converter.class)
    public HdfsSettings proxyJar(Resource proxyJar)
    {
        this.proxyJar = proxyJar;
        return this;
    }

    @KivaKitPropertyConverter
    public HdfsSettings username(String username)
    {
        this.username = username;
        return this;
    }

    ResourceFolder configurationFolder()
    {
        return configurationFolder;
    }

    EmailAddress contactEmail()
    {
        return contactEmail;
    }

    Resource proxyJar()
    {
        return proxyJar;
    }

    String username()
    {
        return username;
    }
}
