package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;

public class YamlArray extends YamlNode implements YamlNodeContainer
{
    public static YamlArray array(String name)
    {
        return new YamlArray(name);
    }

    public static YamlArray array()
    {
        return new YamlArray(UNNAMED);
    }

    private ObjectList<YamlNode> elements;

    private YamlArray(String name)
    {
        super(name);
        elements = new ObjectList<>();
    }

    private YamlArray(YamlArray that)
    {
        super(that);
        elements = that.elements.copy();
    }

    public YamlArray copy()
    {
        return new YamlArray(this);
    }

    public Count count()
    {
        return elements.count();
    }

    @Override
    public ObjectList<YamlNode> elements()
    {
        return elements;
    }

    public YamlNode get(int index)
    {
        return elements.get(index);
    }

    public YamlArray prepending(YamlNode element)
    {
        var copy = copy();
        copy.elements.add(0, element);
        return copy;
    }

    public int size()
    {
        return elements.size();
    }

    public YamlArray with(YamlNode node)
    {
        var copy = copy();
        copy.elements = elements.with(node);
        return copy;
    }
}
