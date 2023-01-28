package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.core.language.trait.TryCatchTrait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.function.Functions.firstSuccessfulFunction;
import static com.telenav.kivakit.core.string.Strings.isInteger;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLANK;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.BLOCK_LABEL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.COMMENT;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.LITERAL;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.ENUM_VALUE;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.NUMBER;
import static com.telenav.kivakit.data.formats.yaml.reader.YamlLineType.STRING;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import static java.util.regex.Pattern.compile;

/**
 * A parser to turn text lines into the model object, {@link YamlLine}.
 *
 * @author Jonathan Locke
 */
public class YamlLineParser implements TryCatchTrait
{
    // Regexes

    private static final String REGEX_IS_ARRAY_ELEMENT = "(?<isArrayElement>\\s*-\\s+)?";

    private static final String REGEX_NUMBER = "(?<number>[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)";

    private static final String REGEX_STRING = "\"(?<string>.*)\"";

    private static final String REGEX_IDENTIFIER = "(?<identifier>[a-zA-Z0-9_]+)";

    private static final String REGEX_LABEL = REGEX_IS_ARRAY_ELEMENT + "(?<label>[a-zA-Z0-9_]+):";

    // Patterns

    private static final Pattern LABEL_PATTERN = compile(REGEX_LABEL + "\\s*");

    private static final Pattern SCALAR_STRING_PATTERN = compile(REGEX_LABEL + "\\s+" + REGEX_STRING);

    private static final Pattern SCALAR_NUMBER_PATTERN = compile(REGEX_LABEL + "\\s+" + REGEX_NUMBER);

    private static final Pattern LITERAL_PATTERN = compile(REGEX_LABEL + "\\s+" + REGEX_IDENTIFIER);

    private static final Pattern STRING_ARRAY_ELEMENT = compile("- " + REGEX_STRING);

    private static final Pattern NUMERIC_ARRAY_ELEMENT = compile("- " + REGEX_NUMBER);

    private static final Pattern ENUM_ARRAY_ELEMENT = compile("- " + REGEX_IDENTIFIER);

    /**
     * Parses the given line text into a {@link YamlLine}
     *
     * @param line The line to parse
     * @return The {@link YamlLine}
     * @throws RuntimeException Thrown if parsing fails
     */
    public YamlLine parse(String line)
    {
        return tryCatchRethrow(() ->
        {
            var indentLevel = indentLevel(line);

            // Create the YAML line,
            var yaml = new YamlLine()
                .rawIndentLevel(indentLevel)
                .indentLevel(indentLevel)
                .line(line.trim());

            // If the line is blank,
            if (line.isBlank())
            {
                // return it as a BLANK line.
                return yaml.type(BLANK);
            }

            // If the line starts with '#',
            if (line.startsWith("#"))
            {
                // return it as a COMMENT.
                return yaml.type(COMMENT);
            }

            yaml = firstSuccessfulFunction(yaml,
                this::numericArrayElement,
                this::enumArrayElement,
                this::stringArrayElement,
                this::label,
                this::string,
                this::number,
                this::literal);

            // If we failed to determine the type,
            if (yaml == null)
            {
                // then the
                fail("Unrecognized YAML line syntax: $", line);
            }

            return yaml;
        }, "Unable to parse line: \"$\"", line);
    }

    private static Number parseNumber(String text)
    {
        if (isInteger(text))
        {
            return parseLong(text);
        }
        else
        {
            return parseDouble(text);
        }
    }

    private YamlLine enumArrayElement(YamlLine yaml)
    {
        var matcher = ENUM_ARRAY_ELEMENT.matcher(yaml.line());
        if (matcher.matches())
        {
            return yaml
                .type(ENUM_VALUE)
                .string(matcher.group("identifier"))
                .arrayElement(true);
        }
        return null;
    }

    private YamlLine extractLabel(YamlLine yaml, Matcher matcher)
    {
        var isArrayElement = matcher.group("isArrayElement");
        yaml.arrayElement(isArrayElement != null && isArrayElement.trim().equals("-"));
        yaml.label(matcher.group("label"));
        return yaml;
    }

    private int indentLevel(String line)
    {
        var spaces = line.length() - line.stripLeading().length();
        ensure(spaces % 2 == 0, "Indentation must be two spaces");
        return spaces / 2;
    }

    private YamlLine label(YamlLine yaml)
    {
        var matcher = LABEL_PATTERN.matcher(yaml.line());
        if (matcher.matches())
        {
            // make a note of that,
            return extractLabel(yaml, matcher)
                .type(BLOCK_LABEL);
        }
        return null;
    }

    private YamlLine literal(YamlLine yaml)
    {
        var matcher = LITERAL_PATTERN.matcher(yaml.line());
        if (matcher.matches())
        {
            return extractLabel(yaml, matcher)
                .type(LITERAL)
                .string(matcher.group("identifier"));
        }
        return null;
    }

    private YamlLine number(YamlLine yaml)
    {
        var matcher = SCALAR_NUMBER_PATTERN.matcher(yaml.line());
        if (matcher.matches())
        {
            return extractLabel(yaml, matcher)
                .type(NUMBER)
                .number(parseNumber(matcher.group("number")));
        }
        return null;
    }

    private YamlLine numericArrayElement(YamlLine yaml)
    {
        var matcher = NUMERIC_ARRAY_ELEMENT.matcher(yaml.line());
        if (matcher.matches())
        {
            return yaml
                .type(NUMBER)
                .number(parseNumber(matcher.group("number")))
                .arrayElement(true);
        }
        return null;
    }

    private YamlLine string(YamlLine yaml)
    {
        var matcher = SCALAR_STRING_PATTERN.matcher(yaml.line());
        if (matcher.matches())
        {
            return extractLabel(yaml, matcher)
                .type(STRING)
                .string(matcher.group("string"));
        }
        return null;
    }

    private YamlLine stringArrayElement(YamlLine yaml)
    {
        var matcher = STRING_ARRAY_ELEMENT.matcher(yaml.line());
        if (matcher.matches())
        {
            return yaml
                .type(STRING)
                .string(matcher.group("identifier"))
                .arrayElement(true);
        }
        return null;
    }
}