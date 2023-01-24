package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

public class YamlTest extends UnitTest
{
    @Test
    public void testIndentation()
    {
        var yaml = abcde();

        ensureEqual(yaml.toString(),
            """
                a
                  b
                    c
                  d
                e""");
    }

    @Test
    public void testWithLabel()
    {
        var yaml = Yaml.yaml()
            .withLabel("x")
            .withLabel("y");

        ensureEqual("x:\ny:", yaml.toString());
    }

    @Test
    public void testWithPrefixed()
    {
        var yaml = Yaml.yaml()
            .with("x")
            .indented()
            .withPrefixed("- ", abcde())
            .outdented()
            .with("y");

        ensureEqual(yaml.toString(), """
            x
              - a
              -   b
              -     c
              -   d
              - e
            y""");
    }

    @Test
    public void testWithScalar()
    {
        var yaml = Yaml.yaml()
            .withScalar("x", 1)
            .withScalar("y", 2);

        ensureEqual("x: 1\ny: 2", yaml.toString());
    }

    @Test
    public void testWithYaml()
    {
        var yaml = Yaml.yaml()
            .with("x")
            .indented()
            .withBlock("", abcde())
            .outdented()
            .with("y");

        ensureEqual(yaml.toString(),
            """
                x
                  a
                    b
                      c
                    d
                  e
                y""");
    }

    private static Yaml abcde()
    {
        return Yaml.yaml()
            .with("a")
            .indented()
            .with("b")
            .indented()
            .with("c")
            .outdented()
            .with("d")
            .outdented()
            .with("e");
    }
}
