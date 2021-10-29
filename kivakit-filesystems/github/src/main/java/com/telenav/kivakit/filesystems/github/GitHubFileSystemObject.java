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

package com.telenav.kivakit.filesystems.github;

import com.telenav.kivakit.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.language.objects.Objects;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.kohsuke.github.GHTreeEntry;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * Base functionality common to both {@link GitHubFile} and {@link GitHubFolder}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class GitHubFileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    // Parses URLs like: github://Telenav/kivakit/develop/<path>?/filename
    private static final Pattern URL_PATTERN = Pattern.compile("(?<scheme>github):/" +
            "/(?<user>[A-Za-z0-9-]+)(/access-token/(?<accessToken>\\w+))?" +
            "/(?<repository>[A-Za-z0-9-.]+)" +
            "/(?<branch>[A-Za-z0-9-.]+)" +
            "(?<path>(/[A-Za-z0-9_.-]+)*?)" +
            "(?<filename>(/[A-Za-z0-9_.-]*)?)"
    );

    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static boolean accepts(String path)
    {
        return URL_PATTERN.matcher(path).matches();
    }

    private String scheme;

    private String userName;

    private String accessToken;

    private String repositoryName;

    private String branchName;

    private String path;

    private String fileName;

    private GitHubTree tree;

    GitHubFileSystemObject(FilePath pathname)
    {
        super(normalize(pathname));

        var matcher = URL_PATTERN.matcher(path().toString());
        if (matcher.matches())
        {
            scheme = matcher.group("scheme");
            userName = matcher.group("user");
            accessToken = matcher.group("accessToken");
            repositoryName = matcher.group("repository");
            branchName = matcher.group("branch");
            path = Strip.leading(matcher.group("path"), "/");
            fileName = Strip.leading(matcher.group("filename"), "/");

            ensure(userName.length() <= 39, "GitHub username is too long: $", userName);
            ensure(!userName.contains("--"), "GitHub username cannot contain consecutive hyphens");
            ensure(!userName.startsWith("-"), "GitHub username cannot start with a hyphen");

            ensure(repositoryName.length() <= 100, "GitHub repository name is too long: $", userName);
            ensure(!repositoryName.contains("--"), "GitHub repository name cannot contain consecutive hyphens");
            ensure(!repositoryName.startsWith("-"), "GitHub repository name cannot start with a hyphen");

            ensure(branchName.length() <= 100, "GitHub branch name is too long: $", userName);
            ensure(!branchName.contains("--"), "GitHub branch name cannot contain consecutive hyphens");
            ensure(!branchName.startsWith("-"), "GitHub branch name cannot start with a hyphen");
        }
        else
        {
            LOGGER.illegalArgument("Invalid GitHub path: $", path);
        }
    }

    @Override
    public void copyFrom(Resource resource, CopyMode mode, ProgressReporter reporter)
    {
    }

    @Override
    public boolean delete()
    {
        return unsupported();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof GitHubFileSystemObject)
        {
            var that = (GitHubFileSystemObject) o;
            return Objects.equalPairs
                    (
                            scheme, that.scheme,
                            userName, that.userName,
                            repositoryName, that.repositoryName,
                            path, that.path,
                            fileName, that.fileName
                    );
        }
        return false;
    }

    @Override
    public boolean exists()
    {
        return entry() != null;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(scheme, userName, repositoryName, branchName, path, fileName);
    }

    @Override
    public boolean isFolder()
    {
        return this instanceof GitHubFolder;
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public abstract Boolean isWritable();

    public String name()
    {
        return path().fileName().name();
    }

    @Override
    public InputStream onOpenForReading()
    {
        return unsupported();
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return unsupported();
    }

    @Override
    public GitHubFolder parent()
    {
        var parent = path().parent();
        if (parent != null)
        {
            return new GitHubFolder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return FilePath.parseFilePath(super.path().toString());
    }

    /**
     * @return The path to this object without the leading root folder (github://username/repository/branch)
     */
    public FilePath relativePath()
    {
        return FilePath.parseFilePath(path);
    }

    @Override
    public FolderService root()
    {
        return new GitHubFolder(scheme + "://" + userName + "/" + repositoryName + "/" + branchName);
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    protected GHTreeEntry entry()
    {
        var path = Strings.isEmpty(this.path) ? fileName : this.path + "/" + fileName;
        var entry = tree().entry(path);
        ensure(entry != null, "No entry for $", path);
        return entry;
    }

    protected GitHubTree tree()
    {
        if (tree == null)
        {
            tree = listenTo(GitHubTree.tree(this, userName, accessToken, repositoryName, branchName));
        }
        return tree;
    }

    private static FilePath normalize(FilePath path)
    {
        return path;
    }

    Bytes length()
    {
        return unsupported();
    }
}
