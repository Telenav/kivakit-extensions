package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.data.formats.yaml.Yaml;

import static com.telenav.kivakit.data.formats.yaml.Yaml.yaml;

public class YamlArray extends YamlNode
{
    public static YamlArray array(String name)
    {
        return new YamlArray(name);
    }

    public static YamlArray array()
    {
        return new YamlArray("");
    }

    private ObjectList<YamlNode> nodes;

    private YamlArray(String name)
    {
        super(name);
        nodes = new ObjectList<>();
    }

    private YamlArray(YamlArray that)
    {
        super(that);
        nodes = that.nodes.copy();
    }

    @Override
    public YamlArray arrayElement(boolean arrayElement)
    {
        return (YamlArray) super.arrayElement(arrayElement);
    }

    @Override
    public Yaml asYaml()
    {
        var yaml = yaml()
            .withLabel(label())
            .indented();

        for (var node : nodes)
        {
            yaml = yaml.withBlock(node.asYaml().asArray());
        }

        return yaml.outdented();
    }

    public YamlArray copy()
    {
        return new YamlArray(this);
    }

    public Count count()
    {
        return nodes.count();
    }

    public YamlNode get(int index)
    {
        return nodes.get(index);
    }

    public int size()
    {
        return nodes.size();
    }

    public YamlArray with(YamlNode node)
    {
        var copy = copy();
        copy.nodes = nodes.with(node.arrayElement(true));
        return copy;
    }
}
