package com.telenav.kivakit.data.formats.yaml.tree;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.data.formats.yaml.tree.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlLiteral.literal;
import static com.telenav.kivakit.data.formats.yaml.tree.YamlScalar.scalar;

public class YamlArrayTest extends UnitTest
{
    @Test
    public void testAsYaml()
    {
        var array = array();
        ensureEqual(array.asYaml().toString().trim(), """
            array:
              - e1:
                e1-a: "moo"
                e1-b: 7
                e1-block1:
                  e1-type: object
                  e1-x: 5
                  e1-y: "boo"
              - e2:
                e2-a: "moo"
                e2-b: 7
                e2-block1:
                  e2-type: object
                  e2-x: 5
                  e2-y: "boo"
            """.trim());
    }

    @Test
    public void testGetAndSize()
    {
        var array = array();

        ensure(array instanceof YamlArray);
        ensure(array.size() == 2);

        var e1 = array.get(0);
        var e2 = array.get(1);

        ensure(e1 instanceof YamlBlock);
        ensure(e2 instanceof YamlBlock);

        ensure(e1.name().equals("e1"));
        ensure(e2.name().equals("e2"));

        ensure(e1.label().equals("- e1"));
        ensure(e2.label().equals("- e2"));
    }

    private static YamlArray array()
    {
        return YamlArray.array("array")
            .with(block("e1")
                .with(scalar("e1-a", "moo"))
                .with(scalar("e1-b", 7))
                .with(block("e1-block1")
                    .with(literal("e1-type", "object"))
                    .with(scalar("e1-x", 5))
                    .with(scalar("e1-y", "boo"))))
            .with(block("e2")
                .with(scalar("e2-a", "moo"))
                .with(scalar("e2-b", 7))
                .with(block("e2-block1")
                    .with(literal("e2-type", "object"))
                    .with(scalar("e2-x", 5))
                    .with(scalar("e2-y", "boo"))));
    }
}
