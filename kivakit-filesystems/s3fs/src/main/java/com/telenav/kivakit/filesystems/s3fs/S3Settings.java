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

import com.telenav.kivakit.kernel.language.reflection.populator.KivaKitPropertyConverter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

/**
 * The settings required to access S3: The access key and secret access key. From this information, {@link
 * #credentials()} creates an {@link AwsBasicCredentials} object.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class S3Settings
{
    private String accessKey;

    private String secretAccessKey;

    @KivaKitPropertyConverter
    public void accessKey(final String accessKey)
    {
        this.accessKey = accessKey;
    }

    public AwsBasicCredentials credentials()
    {
        return AwsBasicCredentials.create(accessKey, secretAccessKey);
    }

    @KivaKitPropertyConverter
    public void secretAccessKey(final String secretAccessKey)
    {
        this.secretAccessKey = secretAccessKey;
    }
}
