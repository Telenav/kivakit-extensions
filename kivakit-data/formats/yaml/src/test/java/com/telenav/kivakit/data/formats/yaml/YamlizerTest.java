package com.telenav.kivakit.data.formats.yaml;

import org.junit.Test;

public class YamlizerTest extends BaseYamlTest
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
    @Test
    public void testMooArray()
    {
        ensureEqual(mooArray().toString().trim(), """
            array:
              - e1-a: "moo"
                e1-b: 7
                e1-block1:
                  e1-type: object
                  e1-x: 5
                  e1-y: "boo"
              - e2-a: "moo"
                e2-b: 7
                e2-block1:
                  e2-type: object
                  e2-x: 5
                  e2-y: "boo"
                   """.trim());
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
    @Test
    public void testTuffyBlock()
    {
        ensureEqual(tuffyBlock().toString().trim(), """
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
}
