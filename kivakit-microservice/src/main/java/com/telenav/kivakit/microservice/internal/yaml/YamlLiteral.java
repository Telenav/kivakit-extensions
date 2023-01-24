package com.telenav.kivakit.microservice.internal.yaml;

import static com.telenav.kivakit.microservice.internal.yaml.Yaml.yaml;

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

    @Override
    protected Yaml asYaml()
    {
        return yaml().withLiteral(name(), value);
    }
}
