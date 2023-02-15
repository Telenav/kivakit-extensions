package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;

public class YamlArray extends YamlNode implements YamlNodeContainer
{
    public static YamlArray yamlArray(String name)
    {
        return new YamlArray(name);
    }

    public static YamlArray yamlArray()
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

    public boolean isEmpty()
    {
        return size() == 0;
    }

    @Override
    public YamlArray prepending(YamlNode node)
    {
        node.parent(this);

        var copy = copy();
        copy.elements.add(0, node);
        return copy;
    }

    @Override
    public int size()
    {
        return elements.size();
    }

    public YamlArray with(YamlNode node)
    {
        node.parent(this);

        var copy = copy();
        copy.elements = elements.with(node);
        return copy;
    }
}
