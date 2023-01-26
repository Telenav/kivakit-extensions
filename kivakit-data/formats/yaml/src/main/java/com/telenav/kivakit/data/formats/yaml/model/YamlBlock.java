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

    /** Elements by name */
    private final StringMap<YamlNode> nameToElement;

    /** All elements (we cannot use the map because some elements are unnamed) */
    private ObjectList<YamlNode> elements = list();

    private YamlBlock(String name)
    {
        super(name);
        nameToElement = new StringMap<>();
    }

    private YamlBlock()
    {
        super(UNNAMED);
        nameToElement = new StringMap<>();
    }

    private YamlBlock(YamlBlock that)
    {
        super(that);
        elements = that.elements.copy();
        nameToElement = that.nameToElement.copy();
    }

    public YamlBlock copy()
    {
        return new YamlBlock(this);
    }

    public ObjectList<YamlNode> elements()
    {
        return elements;
    }

    public YamlNode get(String name)
    {
        return nameToElement.get(name);
    }

    public boolean has(String name)
    {
        return nameToElement.containsKey(name);
    }

    public YamlBlock prepending(YamlNode element)
    {
        var copy = copy();
        copy.elements.add(0, element);
        if (element.isNamed())
        {
            copy.nameToElement.put(element.name(), element);
        }
        return copy;
    }

    public int size()
    {
        return nameToElement.size();
    }

    public YamlBlock with(YamlNode element)
    {
        var copy = copy();
        if (element.isNamed())
        {
            copy.nameToElement.put(element.name(), element);
        }
        copy.elements.add(element);
        return copy;
    }
}
