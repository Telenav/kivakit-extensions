package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.string.FormatProperty;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;

public class YamlScalar extends YamlNode
{
    public static YamlScalar enumValue(String name)
    {
        return new YamlScalar(name);
    }

    public static YamlScalar scalar(String name, Number value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar scalar(String name, Boolean value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar scalar(String name, String value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar scalar(String value)
    {
        return new YamlScalar(UNNAMED, ensureNotNull(value));
    }

    @FormatProperty
    private final Boolean truth;

    @FormatProperty
    private final String string;

    @FormatProperty
    private final Number number;

    @FormatProperty
    private boolean isEnum;

    private YamlScalar(String name, String value)
    {
        super(name);
        this.string = ensureNotNull(value);
        this.number = null;
        this.truth = null;
    }

    private YamlScalar(String name)
    {
        super(name);
        this.isEnum = true;
        this.string = ensureNotNull(name);
        this.number = null;
        this.truth = null;
    }

    private YamlScalar(String name, Number value)
    {
        super(name);
        this.string = null;
        this.number = ensureNotNull(value);
        this.truth = null;
    }

    private YamlScalar(String name, Boolean value)
    {
        super(name);
        this.string = null;
        this.number = null;
        this.truth = ensureNotNull(value);
    }

    public boolean isBoolean()
    {
        return truth != null;
    }

    public boolean isEnum()
    {
        return isEnum;
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
        if (isNamed())
        {
            return number.toString();
        }
        if (isString() || isEnum())
        {
            return string;
        }
        if (isBoolean())
        {
            return truth.toString();
        }
        return "???";
    }

    public boolean truth()
    {
        return truth;
    }
}
