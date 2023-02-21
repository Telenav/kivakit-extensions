package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.value.count.Bytes;

import java.util.function.Function;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_DATE_TIME_SECONDS;
import static com.telenav.kivakit.core.time.LocalTime.localTime;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;

public class YamlScalar extends YamlNode
{
    public static YamlScalar yamlEnumValue(String name)
    {
        return new YamlScalar(name);
    }

    public static YamlScalar yamlScalar(String name, Number value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar yamlScalar(String name, Boolean value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar yamlScalar(String name, String value)
    {
        return new YamlScalar(name, ensureNotNull(value));
    }

    public static YamlScalar yamlScalar(String value)
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

    protected YamlScalar(String name, String value)
    {
        super(name);
        this.string = ensureNotNull(value);
        this.number = null;
        this.truth = null;
    }

    protected YamlScalar(String name)
    {
        super(name);
        this.isEnum = true;
        this.string = ensureNotNull(name);
        this.number = null;
        this.truth = null;
    }

    protected YamlScalar(String name, Number value)
    {
        super(name);
        this.string = null;
        this.number = ensureNotNull(value);
        this.truth = null;
    }

    protected YamlScalar(String name, Boolean value)
    {
        super(name);
        this.string = null;
        this.number = null;
        this.truth = ensureNotNull(value);
    }

    public Bytes asBytes()
    {
        return bytes(asInt());
    }

    public int asInt()
    {
        return number().intValue();
    }

    public LocalTime asLocalFilesystemTime()
    {
        return asLocalTime().asLocalFilesystemTime();
    }

    public LocalTime asLocalTime()
    {
        return localTime(KIVAKIT_DATE_TIME_SECONDS, string());
    }

    public long asLong()
    {
        return number().longValue();
    }

    public <T> T asObject(StringConverter<T> converter)
    {
        return converter.convert(string());
    }

    public <T> T asObject(Function<String, T> function)
    {
        return function.apply(string());
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
