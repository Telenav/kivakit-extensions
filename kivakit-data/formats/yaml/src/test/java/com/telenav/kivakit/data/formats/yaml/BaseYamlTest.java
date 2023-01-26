package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.testing.UnitTest;

import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.array;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.block;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.literal;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.scalar;

public class BaseYamlTest extends UnitTest
{
    /**
     * <pre>
     * array:
     *   - e1-a: "moo"
     *     e1-b: 7
     *     e1-block1
     *       e1-type: object
     *       e1-x: 5
     *       e1-y: "boo"
     *   - e2-a: "moo"
     *     e2-b: 7
     *     e2-block1
     *       e2-type: object
     *       e2-x: 5
     *       e2-y: "boo" </pre>
     */
    protected YamlArray mooArray()
    {
        return array("array")
            .with(block()
                .with(scalar("e1-a", "moo"))
                .with(scalar("e1-b", 7))
                .with(block("e1-block1")
                    .with(literal("e1-type", "object"))
                    .with(scalar("e1-x", 5))
                    .with(scalar("e1-y", "boo"))))
            .with(block()
                .with(scalar("e2-a", "moo"))
                .with(scalar("e2-b", 7))
                .with(block("e2-block1")
                    .with(literal("e2-type", "object"))
                    .with(scalar("e2-x", 5))
                    .with(scalar("e2-y", "boo"))));
    }

    /**
     * <pre>
     * root:
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
    protected YamlBlock tuffyBlock()
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
