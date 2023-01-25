package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.core.collections.map.StringMap;
import com.telenav.kivakit.data.formats.yaml.Yaml;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.data.formats.yaml.Yaml.yaml;

public class YamlBlock extends YamlNode
{
    public static YamlBlock block(String name)
    {
        return new YamlBlock(name);
    }

    public static YamlBlock block()
    {
        return new YamlBlock();
    }

    private final StringMap<YamlNode> children;

    private YamlBlock(String name)
    {
        super(name);
        children = new StringMap<>();
    }

    private YamlBlock()
    {
        super("");
        children = new StringMap<>();
    }

    private YamlBlock(YamlBlock that)
    {
        super(that);
        children = that.children.copy();
    }

    @Override
    public YamlBlock arrayElement(boolean arrayElement)
    {
        return (YamlBlock) super.arrayElement(arrayElement);
    }

    @Override
    public Yaml asYaml()
    {
        var yaml = name().isBlank()
            ? yaml()
            : yaml().withLabel(label()).indented();

        for (var key : list(children.keySet()))
        {
            var child = children.get(key);
            yaml = yaml.withBlock(child.asYaml());
        }

        return yaml.outdented();
    }

    public YamlBlock copy()
    {
        return new YamlBlock(this);
    }

    public YamlNode get(String name)
    {
        return children.get(name);
    }

    public boolean has(String name)
    {
        return children.containsKey(name);
    }

    public YamlBlock with(YamlNode child)
    {
        var copy = copy();
        copy.children.put(child.name(), child);
        return copy;
    }
}
