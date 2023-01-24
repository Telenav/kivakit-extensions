package com.telenav.kivakit.data.formats.yaml.reader;

public enum YamlLineType
{
    /** [whitespace]? */
    BLANK,

    /** # comment */
    COMMENT,

    /** label: */
    LABEL,

    /** x: 7 */
    SCALAR_NUMBER,

    /** cow: "moo" */
    SCALAR_STRING,

    /** type: object */
    LITERAL
}
