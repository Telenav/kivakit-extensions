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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.network.http.HttpNetworkLocation;
import org.junit.Test;

import static com.telenav.kivakit.network.core.Loopback.loopback;

public class HttpNetworkLocationTest extends WebUnitTest
{
    @Test
    public void testReadString()
    {
        var port = 8910;

        var filename = "test.txt";
        var temporary = Folder.kivakitTemporary().file(filename);
        temporary.writer().saveText("testing!");

        startWebServer(port, temporary.parent().path());

        var location = new HttpNetworkLocation(loopback().http(port).path(this, filename));
        var text = location.content(this);
        ensureEqual(text, "testing!");
    }
}
