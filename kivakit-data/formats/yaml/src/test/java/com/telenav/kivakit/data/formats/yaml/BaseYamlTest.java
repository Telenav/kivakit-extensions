package com.telenav.kivakit.data.formats.yaml;

import com.telenav.kivakit.data.formats.yaml.model.YamlArray;
import com.telenav.kivakit.data.formats.yaml.model.YamlBlock;
import com.telenav.kivakit.testing.UnitTest;

import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.yamlArray;
import static com.telenav.kivakit.data.formats.yaml.model.YamlBlock.yamlBlock;
import static com.telenav.kivakit.data.formats.yaml.model.YamlLiteral.yamlLiteral;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

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
        return yamlArray("array")
            .with(yamlBlock()
                .with(yamlScalar("e1-a", "moo"))
                .with(yamlScalar("e1-b", 7))
                .with(yamlBlock("e1-block1")
                    .with(yamlLiteral("e1-type", "object"))
                    .with(yamlScalar("e1-x", 5))
                    .with(yamlScalar("e1-y", "boo"))))
            .with(yamlBlock()
                .with(yamlScalar("e2-a", "moo"))
                .with(yamlScalar("e2-b", 7))
                .with(yamlBlock("e2-block1")
                    .with(yamlLiteral("e2-type", "object"))
                    .with(yamlScalar("e2-x", 5))
                    .with(yamlScalar("e2-y", "boo"))));
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
        return yamlBlock("root")
            .with(yamlLiteral("type", "object"))
            .with(yamlScalar("a", "moo"))
            .with(yamlScalar("b", 7))
            .with(yamlBlock("block1")
                .with(yamlLiteral("type", "object"))
                .with(yamlScalar("x", 5))
                .with(yamlScalar("y", "boo"))
                .with(yamlBlock("block2")
                    .with(yamlLiteral("type", "object"))
                    .with(yamlScalar("tuffy", 3))
                    .with(yamlScalar("duck", "tuffster"))));
    }
}
