package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.core.collections.list.StringList;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;

public class Yaml
{
    public static Yaml yaml()
    {
        return new Yaml();
    }

    private int indent;

    private final StringList lines;

    public Yaml(Yaml that)
    {
        this.indent = that.indent;
        this.lines = that.lines.copy();
    }

    private Yaml()
    {
        this.indent = 0;
        this.lines = stringList();
    }

    public Yaml copy()
    {
        return new Yaml(this);
    }

    public Yaml indented()
    {
        var copy = copy();
        copy.indent++;
        return copy;
    }

    public Yaml outdented()
    {
        var copy = copy();
        copy.indent--;
        return copy;
    }

    @Override
    public String toString()
    {
        return lines.join("\n");
    }

    public Yaml with(String text)
    {
        var copy = copy();
        copy.lines.add(repeat(indent * 2, " ") + text);
        return copy;
    }

    public Yaml withBlock(Yaml yaml)
    {
        return withBlock("", yaml);
    }

    public Yaml withBlock(String label, Yaml yaml)
    {
        var copy = copy();
        var labeled = !label.isBlank();
        if (labeled)
        {
            copy = copy
                .withLabel(label)
                .indented();
        }
        for (var line : yaml.lines)
        {
            copy = copy.with(line);
        }
        return labeled
            ? copy.outdented()
            : copy;
    }

    public Yaml withLabel(String text)
    {
        return with(text + ":");
    }

    public Yaml withLiteral(String key, String value)
    {
        return with(key + ": " + value);
    }

    public Yaml withScalar(String key, String value)
    {
        return with(key + ": \"" + value + "\"");
    }

    public Yaml withScalar(String key, Number value)
    {
        return with(key + ": " + value);
    }

    public Yaml withScalar(String key, Boolean value)
    {
        return with(key + ": " + value);
    }
}
