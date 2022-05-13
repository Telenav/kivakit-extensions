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

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.test.SlowTest;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.FileName;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assume.assumeTrue;

@Category({ SlowTest.class })
public class S3FolderTest extends UnitTest
{
    @SuppressWarnings("EmptyMethod")
    @BeforeClass
    public static void testInitialize()
    {
    }

    @Before
    public void beforeMethod()
    {
        assumeTrue(systemProperty("AWS_ACCESS_KEY_ID") != null);
        assumeTrue(systemProperty("AWS_SECRET_ACCESS_KEY") != null);
    }

    @Test
    public void testClear()
    {
        FolderService folder = new S3Folder("s3://default-region/kivakit/test-data/folder");
        var child = folder.folder(FileName.parseFileName(this, "child"));
        child.mkdirs();
        ensure(child.exists());
        ensureFalse(folder.isEmpty());
        folder.clear();
        ensure(!child.exists());
        ensureFalse(!folder.isEmpty());
    }

    @Test
    public void testDelete()
    {
        FolderService folder = new S3Folder("s3://default-region/kivakit/test-data/folder");
        folder.mkdirs();
        ensure(folder.exists());
        folder.clear();
        folder.delete();
        ensure(!folder.exists());
    }

    @Test
    public void testRename()
    {
        var folder = Folder.parse(this, "s3://default-region/kivakit/test-data/old-" + FileName.dateTime());
        assert folder != null;
        folder.mkdirs();
        var file = folder.file(FileName.parseFileName(this, "tmp.txt"));
        var printWriter = file.printWriter();
        printWriter.println("for test");
        printWriter.close();

        var that = folder.parent().folder(FileName.dateTime());
        folder.renameTo(that);
        ensure(that.exists());
        ensure(!folder.exists());
        that.clearAll();
        ensure(that.isEmpty());
        that.delete();
        ensure(!that.exists());
        folder.delete();
        ensure(!folder.exists());
    }
}
