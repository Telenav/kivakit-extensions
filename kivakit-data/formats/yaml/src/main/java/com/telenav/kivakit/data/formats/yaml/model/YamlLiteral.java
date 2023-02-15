package com.telenav.kivakit.data.formats.yaml.model;

public class YamlLiteral extends YamlNode
{
    public static YamlLiteral yamlLiteral(String name, String value)
    {
        return new YamlLiteral(name, value);
    }

    private final String value;

    protected YamlLiteral(String name, String value)
    {
        super(name);
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    public String value()
    {
        return value;
    }
}
