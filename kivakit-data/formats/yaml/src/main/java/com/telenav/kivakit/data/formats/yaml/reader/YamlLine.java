package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.string.AsciiArt;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLANK;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.COMMENT;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LABEL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.SCALAR_NUMBER;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.SCALAR_STRING;
import static java.lang.Double.parseDouble;
import static java.util.regex.Pattern.compile;

public class YamlLine
{
    private static final Pattern LABEL_PATTERN = compile("(?<label>[a-zA-Z]+):\\s*");

    private static final Pattern SCALAR_STRING_PATTERN = compile("(?<label>[a-zA-Z]+):\\s+\"(?<string>.*)\"");

    private static final Pattern SCALAR_NUMBER_PATTERN = compile("(?<label>[a-zA-Z]+):\\s+(?<number>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+))");

    private static final Pattern LITERAL_PATTERN = compile("(?<label>[a-zA-Z]+):\\s+(?<literal>[a-zA-Z]+)");

    /** The line */
    private final String line;

    /** The indent level of the line */
    private final int indentLevel;

    /** The ordinal of the line, not including comments */
    private int ordinal;

    /** The line number in the input */
    private int lineNumber;

    /** The label, without the colon */
    private String label;

    /** Any string scalar value */
    private String string;

    /** Any numeric scalar value */
    private Number number;

    /** The type of line */
    private YamlLineType type;

    /** True if this line is a list element */
    private boolean isListElement;

    public YamlLine(String line)
    {
        // Get the indent level of the line,
        this.indentLevel = indentLevel(line);

        // then trim it.
        line = line.trim();
        this.line = line;

        parseLine(line);
    }

    public int indentLevel()
    {
        return indentLevel;
    }

    public boolean isBlank()
    {
        return type == BLANK;
    }

    public boolean isComment()
    {
        return type == COMMENT;
    }

    public boolean isLabel()
    {
        return type == LABEL;
    }

    public boolean isArrayElement()
    {
        return isListElement;
    }

    public boolean isLiteral()
    {
        return type == LITERAL;
    }

    public boolean isNumber()
    {
        return type == SCALAR_NUMBER;
    }

    public boolean isScalar()
    {
        return isNumber() || isString();
    }

    public boolean isString()
    {
        return type == SCALAR_STRING;
    }

    public String label()
    {
        return label;
    }

    public String line()
    {
        return line;
    }

    public int lineNumber()
    {
        return lineNumber;
    }

    public Number number()
    {
        return number;
    }

    public int ordinal()
    {
        return ordinal;
    }

    public String string()
    {
        return string;
    }

    @Override
    public String toString()
    {
        var text = switch (type)
            {
                case SCALAR_STRING -> "\"" + string() + "\"";
                case SCALAR_NUMBER -> number.toString();
                case LITERAL -> string();
                default -> "";
            };
        var indent = AsciiArt.repeat(indentLevel, "  ");
        return String.format("%d%16s %s%s: %s", lineNumber, type, indent, label(), text);
    }

    public YamlLineType type()
    {
        return type;
    }

    YamlLine lineNumber(int lineNumber)
    {
        this.lineNumber = lineNumber;
        return this;
    }

    YamlLine ordinal(int ordinal)
    {
        this.ordinal = ordinal;
        return this;
    }

    private int indentLevel(String line)
    {
        var spaces = line.length() - line.stripLeading().length();
        ensure(spaces % 2 == 0, "Indentation must be two spaces");
        return spaces / 2;
    }

    private void parseLine(String line)
    {
        // If the line starts with '#',
        if (line.isBlank())
        {
            type = BLANK;
        }
        else if (line.startsWith("#"))
        {
            // it is a comment.
            type = COMMENT;
        }
        else
        {
            // If the line starts with a '-',
            if (line.startsWith("-"))
            {
                // the line is a list element.
                line = line.substring(1).trim();
                isListElement = true;
            }

            // If the line is a stand-alone label,
            var matcher = LABEL_PATTERN.matcher(line);
            if (matcher.matches())
            {
                // make a note of that,
                type = LABEL;
                label = matcher.group("label");
            }

            // then check for a scalar string,
            if (type == null)
            {
                matcher = SCALAR_STRING_PATTERN.matcher(line);
                if (matcher.matches())
                {
                    type = SCALAR_STRING;
                    label = matcher.group("label");
                    string = matcher.group("string");
                }
            }

            // a scalar number,
            if (type == null)
            {
                matcher = SCALAR_NUMBER_PATTERN.matcher(line);
                if (matcher.matches())
                {
                    type = SCALAR_NUMBER;
                    label = matcher.group("label");
                    number = parseDouble(matcher.group("number"));
                }
            }

            // or a literal,
            if (type == null)
            {
                matcher = LITERAL_PATTERN.matcher(line);
                if (matcher.matches())
                {
                    type = LITERAL;
                    label = matcher.group("label");
                    string = matcher.group("literal");
                }
            }

            // If we failed to determine the type,
            if (type == null)
            {
                // then the
                fail("Could not parse YAML: $", line);
            }
        }
    }
}
