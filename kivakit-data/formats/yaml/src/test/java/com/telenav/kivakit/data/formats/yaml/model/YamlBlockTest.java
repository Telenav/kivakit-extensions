package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.data.formats.yaml.BaseYamlTest;
import org.junit.Test;

import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;

public class YamlBlockTest extends BaseYamlTest
{
    @Test
    public void testCreation()
    {
        ensure(block().isUnnamed());
        ensure(block("tuffy").isNamed());
    }

    /**
     * <pre>
     * root
     *   type: object
     *   a: "moo"
     *   b: 7
     *   block1:
     *     type: object
     *     x: 5
     *     y: "boo"
     *     block2:
     *       type: object
     *       tuffy: 3
     *       duck: "tuffster" </pre>
     */
    @Test
    public void testElementsAndSize()
    {
        var root = tuffyBlock();
        {
            var elements = root.elements();
            ensure(elements.size() == 4);
            ensure(elements.get(0).name().equals("type"));
            ensure(elements.get(1).name().equals("a"));
            ensure(elements.get(2).name().equals("b"));
            ensure(elements.get(3).name().equals("block1"));
        }
        {
            var block1 = (YamlBlock) tuffyBlock().elements().get(3);
            var elements = block1.elements();
            ensure(elements.size() == 4);
            ensure(elements.get(0).name().equals("type"));
            ensure(elements.get(1).name().equals("x"));
            ensure(elements.get(2).name().equals("y"));
            ensure(elements.get(3).name().equals("block2"));
        }
        {
            var block1 = (YamlBlock) tuffyBlock().elements().get(3);
            var block2 = (YamlBlock) block1.elements().get(3);
            var elements = block2.elements();
            ensure(elements.size() == 3);
            ensure(elements.get(0).name().equals("type"));
            ensure(elements.get(1).name().equals("tuffy"));
            ensure(elements.get(2).name().equals("duck"));
        }
    }

    /**
     * <pre>
     * root
     *   type: object
     *   a: "moo"
     *   b: 7
     *   block1:
     *     type: object
     *     x: 5
     *     y: "boo"
     *     block2:
     *       type: object
     *       tuffy: 3
     *       duck: "tuffster" </pre>
     */
    @Test
    public void testGetAndHas()
    {
        var root = tuffyBlock();

        ensure(root.has("a"));
        ensure(root.has("b"));
        ensure(root.has("block1"));

        var a = root.get("a");
        ensure(a instanceof YamlScalar);
        ensure(a.name().equals("a"));
        if (a instanceof YamlScalar scalar)
        {
            ensure(scalar.string().equals("moo"));
        }

        var b = root.get("b");
        ensure(b instanceof YamlScalar);
        ensure(b.name().equals("b"));
        if (b instanceof YamlScalar scalar)
        {
            ensure(scalar.number().equals(7));
        }

        var block1 = (YamlBlock) root.get("block1");
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

    @Test
    public void testWith()
    {
        var block = block("duck");
        block = block.with(scalar("color", "red"));
        block = block.with(scalar("age", 3));
        ensureEqual(block.size(), 2);
        ensure(((YamlScalar) block.elements().get(0)).string().equals("red"));
        ensure(((YamlScalar) block.elements().get(1)).number().intValue() == 3);
    }
}
