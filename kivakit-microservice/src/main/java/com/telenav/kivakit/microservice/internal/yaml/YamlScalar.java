package com.telenav.kivakit.microservice.internal.yaml;

import static com.telenav.kivakit.microservice.internal.yaml.Yaml.yaml;

public class YamlScalar extends YamlNode
{
    public static YamlScalar scalar(String name, String value)
    {
        return new YamlScalar(name, value);
    }

    public static YamlScalar scalar(String name, Number value)
    {
        return new YamlScalar(name, value);
    }

    private final String string;

    private final Number number;

    private YamlScalar(String name, String value)
    {
        super(name);
        this.string = value;
        this.number = null;
    }

    private YamlScalar(String name, Number value)
    {
        super(name);
        this.string = null;
        this.number = value;
    }

    public boolean isNumber()
    {
        return number != null;
    }

    public Number number()
    {
        return number;
    }

    public String string()
    {
        return string;
    }

    @Override
    public String toString()
    {
        return isNumber() ? number.toString() : string;
    }

    @Override
    protected Yaml asYaml()
    {
        return isNumber()
            ? yaml().withScalar(name(), number)
            : yaml().withScalar(name(), string);
    }
}
