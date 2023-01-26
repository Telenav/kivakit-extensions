package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.data.formats.yaml.Yamlizer;
import com.telenav.kivakit.interfaces.naming.Named;

public abstract class YamlNode implements Named
{
    public static String UNNAMED = "";

    private final String name;

    public YamlNode(String name)
    {
        this.name = name;
    }

    protected YamlNode(YamlNode that)
    {
        this.name = that.name;
    }

    public boolean isNamed()
    {
        return !isUnnamed();
    }

    public boolean isUnnamed()
    {
        return name.isBlank();
    }

    @Override
    public final String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return new Yamlizer()
            .asStringList(this)
            .join("\n");
    }
}
