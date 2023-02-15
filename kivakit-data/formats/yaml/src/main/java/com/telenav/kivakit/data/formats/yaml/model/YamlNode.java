package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.data.formats.yaml.YamlFormatter;
import com.telenav.kivakit.interfaces.naming.Named;

public abstract class YamlNode implements Named
{
    public static String UNNAMED = "";

    private final String name;

    private YamlNode parent;

    protected YamlNode(String name)
    {
        this.name = name;
    }

    protected YamlNode(YamlNode that)
    {
        this.name = that.name;
        this.parent = that.parent;
    }

    public YamlArray asArray()
    {
        return (YamlArray) this;
    }

    public YamlBlock asBlock()
    {
        return (YamlBlock) this;
    }

    public YamlLiteral asLiteral()
    {
        return (YamlLiteral) this;
    }

    public YamlScalar asScalar()
    {
        return (YamlScalar) this;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof YamlNode that)
        {
            return this.name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    public boolean isNamed()
    {
        return !isUnnamed();
    }

    public boolean isUnnamed()
    {
        return name == null || name.isBlank();
    }

    @Override
    public final String name()
    {
        return name;
    }

    public void parent(YamlNode parent)
    {
        this.parent = parent;
    }

    public YamlNode parent()
    {
        return parent;
    }

    @Override
    public String toString()
    {
        return new YamlFormatter()
            .asStringList(this)
            .join("\n");
    }
}
