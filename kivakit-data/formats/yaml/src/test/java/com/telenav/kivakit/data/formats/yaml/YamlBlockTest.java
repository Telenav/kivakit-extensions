package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.data.formats.yaml.Yaml.yaml;
import static com.telenav.kivakit.data.formats.yaml.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.YamlLiteral.literal;
import static com.telenav.kivakit.data.formats.yaml.YamlScalar.scalar;

public class YamlBlockTest extends UnitTest
{
    @Test
    public void testAsYaml()
    {
        var block2 = yaml()
            .withLiteral("type", "object")
            .withScalar("tuffy", 3)
            .withScalar("duck", "tuffster");

        var block1 = yaml()
            .withLiteral("type", "object")
            .withScalar("x", 5)
            .withScalar("y", "boo")
            .withBlock("block2", block2);

        var expected = yaml()
            .withLabel("root")
            .indented()
            .withLiteral("type", "object")
            .withScalar("a", "moo")
            .withScalar("b", 7)
            .withBlock("block1", block1);

        ensureEqual(tree().asYaml().toString().trim(), expected.toString().trim());
    }

    @Test
    public void testBlock()
    {
        var tree = tree();

        ensureEqual(tree.asYaml().toString(), """
            root:
              type: object
              a: "moo"
              b: 7
              block1:
                type: object
                x: 5
                y: "boo"
                block2:
                  type: object
                  tuffy: 3
                  duck: "tuffster"
            """.trim());
    }

    @Test
    public void testGetAndHas()
    {
        var tree = tree();

        ensure(tree.has("a"));
        ensure(tree.has("b"));
        ensure(tree.has("block1"));

        var a = tree.get("a");
        ensure(a instanceof YamlScalar);
        ensure(a.name().equals("a"));
        if (a instanceof YamlScalar scalar)
        {
            ensure(scalar.string().equals("moo"));
        }

        var b = tree.get("b");
        ensure(b instanceof YamlScalar);
        ensure(b.name().equals("b"));
        if (b instanceof YamlScalar scalar)
        {
            ensure(scalar.number().equals(7));
        }

        var block1 = (YamlBlock) tree.get("block1");
        var x = (YamlScalar) block1.get("x");
        ensure(x.name().equals("x"));
        ensure(x.number().equals(5));

        var y = (YamlScalar) block1.get("y");
        ensure(y.name().equals("y"));
        ensure(y.string().equals("boo"));

        var block2 = block1.get("block2");
        ensure(block2 instanceof YamlBlock);
        ensure(block2.name().equals("block2"));
    }

    private static YamlBlock tree()
    {
        return block("root")
            .with(literal("type", "object"))
            .with(scalar("a", "moo"))
            .with(scalar("b", 7))
            .with(block("block1")
                .with(literal("type", "object"))
                .with(scalar("x", 5))
                .with(scalar("y", "boo"))
                .with(block("block2")
                    .with(literal("type", "object"))
                    .with(scalar("tuffy", 3))
                    .with(scalar("duck", "tuffster"))));
    }
}
