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

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.network.core.Host;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import com.telenav.kivakit.resource.path.FilePath;
import org.junit.Test;

public class HttpNetworkLocationTest extends WebUnitTest
{
    @Test
    public void testReadString()
    {
        var port = 8910;
        startWebServer(port, FilePath.parseFilePath(this, "/"));
        HttpNetworkLocation location = new HttpNetworkLocation(Host.loopback().http(port).path(this, "README.md"));
        String text = location.content();
        ensure(!text.isBlank());
    }
}
