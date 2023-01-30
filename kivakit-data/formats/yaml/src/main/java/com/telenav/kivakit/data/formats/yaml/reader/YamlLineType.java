package com.telenav.kivakit.data.formats.yaml.reader;

public enum YamlLineType
{
    /** [whitespace]? */
    BLANK,

    /** # comment */
    COMMENT,

    /** label: */
    BLOCK_LABEL,

    /** x: 7 */
    NUMBER,

    /** cow: "moo" */
    STRING,

    /** - BLUE */
    ENUM_VALUE,

    /** type: object */
    LITERAL
}
