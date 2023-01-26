package com.telenav.kivakit.data.formats.yaml.model;

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

    public static YamlScalar scalar(String name, Boolean value)
    {
        return new YamlScalar(name, value);
    }

    private final Boolean truth;

    private final String string;

    private final Number number;

    private YamlScalar(String name, String value)
    {
        super(name);
        this.string = value;
        this.number = null;
        this.truth = null;
    }

    private YamlScalar(String name, Number value)
    {
        super(name);
        this.string = null;
        this.number = value;
        this.truth = null;
    }

    private YamlScalar(String name, Boolean value)
    {
        super(name);
        this.string = null;
        this.number = null;
        this.truth = value;
    }

    public boolean isBoolean()
    {
        return truth != null;
    }

    public boolean isNumber()
    {
        return number != null;
    }

    public boolean isString()
    {
        return string != null;
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

    public boolean truth()
    {
        return truth;
    }
}
