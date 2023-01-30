package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.language.trait.TryCatchTrait;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.string.AsciiArt.repeat;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLANK;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLOCK_LABEL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.COMMENT;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.ENUM_VALUE;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.NUMBER;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.STRING;

public class YamlLine implements TryCatchTrait
{
    /** The line */
    private String line;

    /** The indent level of the line */
    private int rawIndentLevel;

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

    public YamlLine arrayElement(boolean arrayElement)
    {
        isArrayElement = arrayElement;
        return this;
    }

    public int indentLevel()
    {
        return indentLevel;
    }

    public YamlLine indentLevel(int indentLevel)
    {
        this.indentLevel = indentLevel;
        return this;
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
        return type == ENUM_VALUE;
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
        return type == NUMBER;
    }

    public boolean isScalar()
    {
        return isNumber() || isString() || isEnumValue();
    }

    public boolean isString()
    {
        return type == STRING;
    }

    public String label()
    {
        return label;
    }

    public YamlLine label(String label)
    {
        this.label = ensureNotNull(label);
        return this;
    }

    public String line()
    {
        return line;
    }

    public YamlLine line(String line)
    {
        this.line = ensureNotNull(line);
        return this;
    }

    public int lineNumber()
    {
        return lineNumber;
    }

    public YamlLine lineNumber(int lineNumber)
    {
        this.lineNumber = lineNumber;
        return this;
    }

    public Number number()
    {
        return number;
    }

    public YamlLine number(Number number)
    {
        this.number = ensureNotNull(number);
        return this;
    }

    public int ordinal()
    {
        return ordinal;
    }

    public YamlLine ordinal(int ordinal)
    {
        this.ordinal = ordinal;
        return this;
    }

    public YamlLine outdent(int levels)
    {
        indentLevel -= levels;
        return this;
    }

    public int rawIndentLevel()
    {
        return rawIndentLevel;
    }

    public YamlLine rawIndentLevel(int rawIndentLevel)
    {
        this.rawIndentLevel = rawIndentLevel;
        return this;
    }

    public String string()
    {
        return string;
    }

    public YamlLine string(String string)
    {
        this.string = ensureNotNull(string);
        return this;
    }

    public String text()
    {
        return switch (type)
            {
                case STRING -> "\"" + string() + "\"";
                case NUMBER -> number.toString();
                case LITERAL -> string();
                default -> "";
            };
    }

    @Override
    public String toString()
    {
        var indent = repeat(rawIndentLevel, "  ");
        return String.format("%d%16s %s%s%s: %s", lineNumber, type, indent,
            isArrayElement ? "- " : "",
            label() == null ? "" : label(), text());
    }

    public YamlLineType type()
    {
        return type;
    }

    public YamlLine type(YamlLineType type)
    {
        this.type = type;
        return this;
    }
}
