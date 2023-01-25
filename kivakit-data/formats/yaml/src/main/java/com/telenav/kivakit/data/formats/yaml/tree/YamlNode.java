package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.data.formats.yaml.Yaml;
import com.telenav.kivakit.interfaces.naming.Named;

public abstract class YamlNode implements Named
{
    private final String name;

    private boolean arrayElement;

    public YamlNode(String name)
    {
        this.name = name;
    }

    @SuppressWarnings("CopyConstructorMissesField")
    protected YamlNode(YamlNode that)
    {
        this.name = that.name;
    }

    public YamlNode arrayElement(boolean arrayElement)
    {
        this.arrayElement = arrayElement;
        return this;
    }

    public abstract Yaml asYaml();

    public boolean isArrayElement()
    {
        return arrayElement;
    }

    public final String label()
    {
        return arrayElement
            ? "- " + name
            : name;
    }

    @Override
    public final String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return asYaml().toString();
    }
}
