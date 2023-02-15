package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.data.formats.yaml.BaseYamlTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Count._2;
import static com.telenav.kivakit.data.formats.yaml.model.YamlArray.yamlArray;
import static com.telenav.kivakit.data.formats.yaml.model.YamlScalar.yamlScalar;

public class YamlArrayTest extends BaseYamlTest
{
    @Test
    public void testCreation()
    {
        ensure(yamlArray().isUnnamed());
        ensure(yamlArray("fred").isNamed());
    }

    /**
     * <pre>
     * - e1:
     *   e1-a: "moo"
     *   e1-b: 7
     *   e1-block1
     *     e1-type: object
     *     e1-x: 5
     *     e1-y: "boo"
     * - e2:
     *   e2-a: "moo"
     *   e2-b: 7
     *   e2-block1
     *     e2-type: object
     *     e2-x: 5
     *     e2-y: "boo" </pre>
     */
    @Test
    public void testGetSizeAndCount()
    {
        var array = mooArray();

        ensure(array instanceof YamlArray);
        ensure(array.size() == 2);
        ensure(array.count().equals(_2));

        var e1 = array.get(0);
        var e2 = array.get(1);

        ensure(e1 instanceof YamlBlock);
        ensure(e2 instanceof YamlBlock);

        ensure(e1.isUnnamed());
        ensure(e2.isUnnamed());
    }

    @Test
    public void testWithAndElements()
    {
        var array = yamlArray("duck");
        array = array.with(yamlScalar("color", "red"));
        array = array.with(yamlScalar("age", 3));
        ensureEqual(array.size(), 2);
        ensureEqual(array.elements().size(), 2);
        ensure(((YamlScalar) array.elements().get(0)).string().equals("red"));
        ensure(((YamlScalar) array.elements().get(1)).number().intValue() == 3);
    }
}
