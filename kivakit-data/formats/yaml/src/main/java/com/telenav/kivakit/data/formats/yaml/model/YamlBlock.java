package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;

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

    private final StringMap<YamlNode> elements;

    private YamlBlock(String name)
    {
        super(name);
        elements = new StringMap<>();
    }

    private YamlBlock()
    {
        super(UNNAMED);
        elements = new StringMap<>();
    }

    private YamlBlock(YamlBlock that)
    {
        super(that);
        elements = that.elements.copy();
    }

    public YamlBlock copy()
    {
        return new YamlBlock(this);
    }

    public ObjectList<YamlNode> elements()
    {
        return list(elements.values());
    }

    public YamlNode get(String name)
    {
        return elements.get(name);
    }

    public boolean has(String name)
    {
        return elements.containsKey(name);
    }

    public int size()
    {
        return elements.size();
    }

    public YamlBlock with(YamlNode element)
    {
        var copy = copy();
        copy.elements.put(element.name(), element);
        return copy;
    }
}
