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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.KivaKit;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class S3ParsingTest extends UnitTest
{
    final S3File file = new S3File("s3://default-region/kivakit/" + apidocs());

    @Test
    public void testFile()
    {
        final var folder = Folder.parse("s3://default-region/my-bucket/my-sub-bucket");
        assert folder != null;
        final var file = folder.file(FileName.parse("name1"));
        ensure(file.fileName().toString().equals("name1"));
    }

    @Test
    public void testFileBucketName()
    {
        ensure(file.fileName().equals(FileName.parse("index.html")));
    }

    @Test
    public void testFileGetName()
    {
        ensure("index.html".equals(file.name()));
    }

    @Test
    public void testFileKeyName()
    {
        ensure(apidocs().equals(file.key()));
    }

    @Test
    public void testFileParent()
    {
        final FolderService folder = file.parent();
        ensure("docs".equals(folder.baseName().toString()));
        ensure(("s3://default-region/kivakit/" + KivaKit.get().projectVersion() + "/docs").equals(folder.path().toString()));
    }

    @Test
    public void testFilePath()
    {
        ensure(new S3File("s3://default-region/kivakit/s3-test.gz").path().equals(FilePath.parseFilePath("s3://default-region/kivakit/s3-test.gz")));
    }

    @Test
    public void testFileWithIdenticalKey()
    {
        ensure(file.withIdenticalKey(new S3File("s3://default-region/anotherBucket/" + apidocs())));
    }

    @Test
    public void testFolder()
    {
        final var path = "s3://default-region/kivakit/test-data/folder";
        final var folder = Folder.parse(path);
        assert folder != null;
        ensure(folder.folder("child").name().equals(FileName.parse("child")));
    }

    @Test
    public void testFolderBucketName()
    {
        final var s3Folder = new S3Folder("s3://default-region/kivakit/test-data/folder");
        ensure(s3Folder.bucket().equals("com-telenav-nav-user-analytics-dev"));
    }

    @Test
    public void testFolderGetName()
    {
        final var path = "s3://default-region/kivakit/test-data/folder";
        final var folder = Folder.parse(path);
        assert folder != null;
        ensure(folder.name().equals(FileName.parse("folder")));
    }

    @Test
    public void testFolderKeyName()
    {
        final var s3Folder = new S3Folder("s3://default-region/kivakit/test-data/folder");
        ensure(s3Folder.key().equals("nav-user-analytics/folder/"));
    }

    @Test
    public void testFolderParent()
    {
        final var path = "s3://default-region/kivakit/test-data/folder";
        final var folder = Folder.parse(path);
        assert folder != null;
        ensure(folder.parent().equals(Folder.parse("s3://default-region/kivakit/test-data")));
        ensure(folder.parent().name().equals(FileName.parse("nav-user-analytics")));
    }

    @Test
    public void testFolderPath()
    {
        final var path = "s3://default-region/kivakit/test-data/folder";
        final var folder = Folder.parse(path);
        assert folder != null;
        ensure(folder.path().equals(FilePath.parseFilePath(path)));
    }

    @Test
    public void testFolderPathString()
    {
        final var path = "s3://default-region/kivakit/test-data/folder";
        final var folder = Folder.parse(path);
        assert folder != null;
        ensure(folder.path().toString().equals(path));
    }

    @Test
    public void testFolderScheme()
    {
        final var s3Folder = new S3Folder("s3://default-region/kivakit/test-data/folder");
        ensure(s3Folder.scheme().equals("s3"));
    }

    @Test
    public void testInSameBucket()
    {
        ensure(file.inSameBucket(new S3File("s3://default-region/kivakit/" + KivaKit.get().projectVersion() + "/another")));
    }

    @Test
    public void testIsFolder()
    {
        ensure(file.isFile());
        ensureFalse(file.isFolder());
    }

    @NotNull
    private String apidocs()
    {
        return KivaKit.get().projectVersion() + "/docs/index.html";
    }
}
