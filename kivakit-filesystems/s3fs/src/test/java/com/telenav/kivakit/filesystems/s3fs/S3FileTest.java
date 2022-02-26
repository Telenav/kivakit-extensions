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

import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.test.annotations.SlowTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assume.assumeTrue;

@Category({ SlowTests.class })
public class S3FileTest extends UnitTest
{
    @Before
    public void beforeMethod()
    {
        assumeTrue(JavaVirtualMachine.property("AWS_ACCESS_KEY_ID") != null);
        assumeTrue(JavaVirtualMachine.property("AWS_SECRET_ACCESS_KEY") != null);
    }

    @Test
    public void testReadFile()
    {
        ensure("123".equals(file.reader().asString().trim()));
    }

    final S3File file = new S3File("s3://default-region/kivakit/test-data/test.txt");
}
