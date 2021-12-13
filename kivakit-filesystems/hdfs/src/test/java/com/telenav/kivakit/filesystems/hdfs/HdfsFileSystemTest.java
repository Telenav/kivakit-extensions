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
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.network.core.EmailAddress;
import com.telenav.kivakit.network.core.NetworkPath;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.test.annotations.SlowTests;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ SlowTests.class })
@Ignore
public class HdfsFileSystemTest extends UnitTest
{
    /**
     * The navhacluster doesn't seem to work from the EU, so we can turn off the relevant tests here
     */
    private static final boolean TEST_NAVTEAM_CLUSTER = false;

    /** Only run the tests about one in every ten times to speed up the build */
    private static final boolean TEST_OSMTEAM_CLUSTER = true;

    @Before
    public void testBefore()
    {
        var kivakitVersion = KivaKit.get().kivakitVersion();
        var settings = new HdfsSettings()
                .clusterName("cluster1ns")
                .configurationFolder(Package.packageFrom(this, getClass(), "settings/cluster1ns"))
                .contactEmail(EmailAddress.parse(this, "jonathanl@telenav.com"))
                .username("automation")
                .proxyJar(new HttpNetworkLocation(NetworkPath.parseNetworkPath(this,
                        "https://www.kivakit.org/" + kivakitVersion + "/applications/kivakit-hdfs-proxy-" + kivakitVersion + ".jar")).get());
        Settings.of(this).registerSettingsObject(settings);
    }

    @Test
    public void testFolder()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            var folder = Folder.parse(this, osmteamZambia().toString());
            assert folder != null;
            ensure(folder.exists());
            ensure(!folder.isEmpty());
            ensure(folder.nestedFiles().size() > 1);
        }
    }

    @Test
    public void testFoldersAndFiles()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            var hdfs = osmteamHdfs().toString();
            ensureEqual(hdfs + "foo/bar", osmteamHdfs().folder("foo/bar").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().file("foo/bar/baz.txt").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().folder("foo/bar").file("baz.txt").toString());
            ensureEqual(hdfs + "foo/bar/baz.txt", osmteamHdfs().folder("foo").file("bar/baz.txt").toString());
        }
    }

    @Test
    public void testListFiles()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            ensure(osmteamZambia().files().size() > 1);
        }
    }

    @Test
    public void testListFolders()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            var folders = osmteamZambia().folders();
            ensure(folders.size() > 1);
        }
    }

    @Test
    public void testReadFile()
    {
        if (TEST_OSMTEAM_CLUSTER)
        {
            // Read a file from the osmteam HDFS
            var file = osmteamHdfs().file("/graph-api/graph-server/repository/Zambia/test.txt");
            var i = 1;
            for (String line : file.reader().lines())
            {
                ensureEqual(i++, Integer.parseInt(line));
            }
        }

        if (TEST_NAVTEAM_CLUSTER)
        {
            // Read a file from the navteam HDFS to test using multiple HDFS filesystems
            var file = navteamHdfs().file("graph-api/kivakit-hdfs-unit-test-do-not-remove.txt");
            ensure(file.exists());
            var i = 1;
            for (String line : file.reader().lines())
            {
                ensureEqual(i++, Integer.parseInt(line));
            }
            ensureEqual(6, i);
        }
    }

    private Folder navteamHdfs()
    {
        return Folder.parse(this, "hdfs://navhacluster");
    }

    private Folder osmteamHdfs()
    {
        return Folder.parse(this, "hdfs://cluster1ns");
    }

    private Folder osmteamZambia()
    {
        return osmteamHdfs().folder("graph-api/graph-server/repository/Zambia");
    }
}
