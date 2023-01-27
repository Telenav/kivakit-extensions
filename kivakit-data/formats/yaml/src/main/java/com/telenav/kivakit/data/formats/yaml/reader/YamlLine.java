package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.language.trait.TryCatchTrait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.core.string.Strings.isNaturalNumber;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLANK;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLOCK_LABEL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.COMMENT;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.SCALAR_ENUM_VALUE;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.SCALAR_NUMBER;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.SCALAR_STRING;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import static java.util.regex.Pattern.compile;

public class YamlLine implements TryCatchTrait
{
    private static final String ARRAY_ELEMENT = "(?<isArrayElement>\\s*-\\s+)?";

    private static final String LABEL = ARRAY_ELEMENT + "(?<label>[a-zA-Z0-9_]+):";

    private static final Pattern LABEL_PATTERN = compile(LABEL + "\\s*");

    private static final Pattern SCALAR_STRING_PATTERN = compile(LABEL + "\\s+\"(?<string>.*)\"");

    private static final Pattern SCALAR_NUMBER_PATTERN = compile(LABEL + "\\s+(?<number>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)");

    private static final Pattern LITERAL_PATTERN = compile(LABEL + "\\s+(?<literal>[a-zA-Z0-9_]+)");

    private static final Pattern ENUM_PATTERN = compile("- (?<name>[a-zA-Z0-9_]+)");

    /** The line */
    private final String line;

    /** The indent level of the line */
    private final int rawIndentLevel;

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
    private boolean isArrayElement;

    /** Any outdent adjustment to the indent level */
    private int indentLevel;

    public YamlLine(String line)
    {
        // Get the indent level of the line,
        this.rawIndentLevel = indentLevel = indentLevel(line);

        // then trim it.
        line = line.trim();
        this.line = line;

        parseLine(line);
    }

    public int indentLevel()
    {
        return indentLevel;
    }

    public boolean isArrayElement()
    {
        return isArrayElement;
    }

    public boolean isBlank()
    {
        return type == BLANK;
    }

    public boolean isComment()
    {
        return type == COMMENT;
    }

    public boolean isEnumValue()
    {
        return type == SCALAR_ENUM_VALUE;
    }

    public boolean isLabel()
    {
        return type == BLOCK_LABEL;
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
        return isNumber() || isString() || isEnumValue();
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

    public void outdent(int levels)
    {
        indentLevel -= levels;
    }

    public int rawIndentLevel()
    {
        return rawIndentLevel;
    }

    public String string()
    {
        return string;
    }

    public String text()
    {
        return switch (type)
            {
                case SCALAR_STRING -> "\"" + string() + "\"";
                case SCALAR_NUMBER -> number.toString();
                case LITERAL -> string();
                default -> "";
            };
    }

    @Override
    public String toString()
    {
        var indent = repeat(rawIndentLevel, "  ");
        return String.format("%d%16s %s%s: %s", lineNumber, type, indent, label(), text());
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

    private void extractLabel(Matcher matcher)
    {
        var isArrayElement = matcher.group("isArrayElement");
        this.isArrayElement = isArrayElement != null && isArrayElement.trim().equals("-");
        label = matcher.group("label");
    }

    private int indentLevel(String line)
    {
        var spaces = line.length() - line.stripLeading().length();
        ensure(spaces % 2 == 0, "Indentation must be two spaces");
        return spaces / 2;
    }

    private void parseLine(String line)
    {
        tryCatchRethrow(() ->
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
                // If the line is a stand-alone label,
                var matcher = LABEL_PATTERN.matcher(line);
                if (matcher.matches())
                {
                    // make a note of that,
                    type = BLOCK_LABEL;
                    extractLabel(matcher);
                }

                // then check for a scalar string,
                if (type == null)
                {
                    matcher = SCALAR_STRING_PATTERN.matcher(line);
                    if (matcher.matches())
                    {
                        type = SCALAR_STRING;
                        extractLabel(matcher);
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
                        extractLabel(matcher);
                        var numberString = matcher.group("number");
                        if (isNaturalNumber(numberString))
                        {
                            number = parseLong(numberString);
                        }
                        else
                        {
                            number = parseDouble(numberString);
                        }
                    }
                }

                // a literal,
                if (type == null)
                {
                    matcher = LITERAL_PATTERN.matcher(line);
                    if (matcher.matches())
                    {
                        type = LITERAL;
                        extractLabel(matcher);
                        string = matcher.group("literal");
                    }
                }

                // or an enum value,
                if (type == null)
                {
                    matcher = ENUM_PATTERN.matcher(line);
                    if (matcher.matches())
                    {
                        type = SCALAR_ENUM_VALUE;
                        string = matcher.group("name");
                        isArrayElement = true;
                    }
                }

                // If we failed to determine the type,
                if (type == null)
                {
                    // then the
                    fail("Unrecognized YAML line syntax: $", line);
                }
            }
        }, "Unable to parse line: \"$\"", line);
    }
}
