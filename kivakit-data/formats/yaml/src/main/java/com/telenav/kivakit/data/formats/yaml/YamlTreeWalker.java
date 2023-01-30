package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.core.collections.set.IdentitySet;
import com.telenav.kivakit.data.formats.yaml.model.YamlNode;
import com.telenav.kivakit.data.formats.yaml.model.YamlNodeContainer;
import com.telenav.kivakit.interfaces.code.Callback;

import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.illegalState;

public class YamlTreeWalker
{
    private final YamlNode root;

    public YamlTreeWalker(YamlNode root)
    {
        this.root = root;
    }

    public void walk(Callback<YamlNode> callback)
    {
        walk(root, callback, new IdentitySet<>());
    }

    private void walk(YamlNode node, Callback<YamlNode> callback, Set<YamlNode> visited)
    {
        // If we've already seen this node,
        if (visited.contains(node))
        {
            // we have a graph and not a tree.
            illegalState("YAML tree has circularity at: $", node);
        }

        // We've now visited this node.
        visited.add(node);

        // If the node contains other nodes,
        if (node instanceof YamlNodeContainer container)
        {
            // then go through them,
            for (var element : container.elements())
            {
                // call back for the element,
                callback.call(element);

                // then walk its children, if any.
                walk(element, callback, visited);
            }
        }
        else
        {
            // We're at a leaf node.
            callback.call(node);
        }
    }
}
