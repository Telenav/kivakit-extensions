package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.data.formats.yaml.Yaml;

public class YamlArray extends YamlNode
{
    public static YamlArray array(String name)
    {
        return new YamlArray(name);
    }

    private ObjectList<YamlNode> nodes;

    private YamlArray(String name)
    {
        super(name);
        nodes = new ObjectList<>();
    }

    private YamlArray(YamlArray that)
    {
        super(that.name());
        nodes = that.nodes.copy();
    }

    public YamlArray copy()
    {
        return new YamlArray(this);
    }

    public YamlArray with(YamlNode node)
    {
        var copy = copy();
        copy.nodes = nodes.with(node);
        return copy;
    }

    @Override
    protected Yaml asYaml()
    {
        var yaml = Yaml.yaml()
            .withLabel(name())
            .indented();

        for (var node : nodes)
        {
            yaml = yaml.withPrefixed(" - ", node.asYaml());
        }

        return yaml.outdented();
    }
}
