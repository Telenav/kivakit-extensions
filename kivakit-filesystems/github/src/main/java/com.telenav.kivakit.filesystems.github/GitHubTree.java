package com.telenav.kivakit.filesystems.github;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.messaging.Listener;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GHTreeEntry;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * Holds information for an entire GitHub repository tree, given a username, repository and branch.
 *
 * @author jonathanl (shibo)
 */
public class GitHubTree extends BaseComponent
{
    private static final Map<String, GitHubTree> trees = new HashMap<>();

    /**
     * The current tree for the given username, repository and branch
     *
     * @param username The GitHub username
     * @param repository The name of the repository on GitHub
     * @param branch The branch of the repository
     * @return A complete GitHub tree for the given username, repository and branch.
     */
    public static GitHubTree tree(Listener listener, String username, String repository, String branch)
    {
        var key = username + "/" + repository + "/" + branch;
        return trees.computeIfAbsent(key, ignored -> listener.listenTo(new GitHubTree(username, repository, branch)));
    }

    public enum EntryType
    {
        FILE,
        FOLDER
    }

    private final String username;

    private final String repository;

    private final String branch;

    private GHTree tree;

    protected GitHubTree(String username, String repository, String branch)
    {
        this.username = username;
        this.repository = repository;
        this.branch = branch;
    }

    public List<GHTreeEntry> entries(String path, EntryType type, boolean recursive)
    {
        var entries = new ArrayList<GHTreeEntry>();

        // Go through each entry in the tree
        for (var entry : entries())
        {
            // and if it is a folder,
            if (entryType(entry) == type)
            {
                //  get the entry path
                final String entryPath = entry.getPath();

                // and if it starts with the given path,
                if (entryPath.startsWith(path))
                {
                    // get the tail of the path
                    var tail = Strip.leading(entryPath, path);

                    // and if we are including all recursively, or the tail is not a path,
                    if (recursive || !tail.contains("/"))
                    {
                        // then add the entry.
                        entries.add(entry);
                    }
                }
            }
        }

        return entries;
    }

    public List<GHTreeEntry> entries()
    {
        return tree().getTree();
    }

    public GHTreeEntry entry(String path)
    {
        return tree().getEntry(path);
    }

    public void load()
    {
        try
        {
            tree = GitHub.connectAnonymously()
                    .getUser(username)
                    .getRepository(repository)
                    .getTreeRecursive(branch, 1);
        }
        catch (Exception e)
        {
            problem(e, "Unable to load tree for $/$/$", username, repository, branch);
        }
    }

    private EntryType entryType(final GHTreeEntry entry)
    {
        switch (entry.getType())
        {
            case "blob":
                return EntryType.FILE;

            case "tree":
                return EntryType.FOLDER;

            default:
                return unsupported();
        }
    }

    private GHTree tree()
    {
        if (tree == null)
        {
            load();
        }
        return tree;
    }
}
