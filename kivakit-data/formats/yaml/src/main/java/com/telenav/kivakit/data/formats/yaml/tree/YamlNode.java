package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.data.formats.yaml.Yaml;
import com.telenav.kivakit.interfaces.naming.Named;

public abstract class YamlNode implements Named
{
    private final String name;

    public YamlNode(String name)
    {
        this.name = name;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return asYaml().toString();
    }

    protected abstract Yaml asYaml();
}
