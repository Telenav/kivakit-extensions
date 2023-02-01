package com.telenav.kivakit.data.formats.yaml.model;

public class YamlLiteral extends YamlNode
{
    public static YamlLiteral literal(String name, String value)
    {
        return new YamlLiteral(name, value);
    }

    private final String value;

    private YamlLiteral(String name, String value)
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
