package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.StringMap;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;

public class YamlBlock extends YamlNode implements YamlNodeContainer
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

    public void addReference(String type)
    {
        nameToElement.clear();
        elements.clear();
        var reference = scalar("$ref", "#/components/schemas/" + type);
        elements.add(reference);
        nameToElement.put(reference.name(), reference);
    }

    public YamlBlock copy()
    {
        return new YamlBlock(this);
    }

    @Override
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

    @Override
    public YamlBlock prepending(YamlNode node)
    {
        ensureNotNull(node);

        node.parent(this);

        var copy = copy();
        copy.elements.add(0, node);
        if (node.isNamed())
        {
            copy.nameToElement.put(node.name(), node);
        }
        return copy;
    }

    @Override
    public int size()
    {
        return nameToElement.size();
    }

    public YamlBlock with(YamlNode node)
    {
        ensureNotNull(node);

        node.parent(this);

        var copy = copy();
        copy.elements.add(node);
        if (node.isNamed())
        {
            copy.nameToElement.put(node.name(), node);
        }
        return copy;
    }
}
