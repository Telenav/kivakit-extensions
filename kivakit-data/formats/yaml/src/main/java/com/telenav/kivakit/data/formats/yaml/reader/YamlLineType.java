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
    SCALAR_NUMBER,

    /** cow: "moo" */
    SCALAR_STRING,

    /** - BLUE */
    SCALAR_ENUM_VALUE,

    /** type: object */
    LITERAL
}
