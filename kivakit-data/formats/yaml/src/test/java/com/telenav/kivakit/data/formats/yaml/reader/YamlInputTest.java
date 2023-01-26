package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class YamlInputTest extends UnitTest
{
    /**
     * <pre>
     * 00 0 type: object
     * 01 0 description: "Arrays test"
     * 02 0 properties:
     * 03 1   animals:
     * 04 2     type: array
     * 05 2     description: "a list of animals"
     * 06 2     items:
     * 07 3       - element: "1"
     * 08 3         name: "dinosaur"
     * 09 3         occupation: "eating trees"
     * 10 3         friends:
     * 11 4           type: array
     * 12 4           description: "list of friends"
     * 13 4           items:
     * 14 5             - name: "penguin"
     * 15 5             - name: "bat"
     * 16 3       - element: "2"
     * 17 3         name: "penguin"
     * 18 3         occupation: "keeping warm" </pre>
     */
    @Test
    public void testArrayIndentLevels()
    {
        var indents = new int[]
            {
                0, 0, 0, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 5, 5, 3, 3, 3
            };
        var in = new YamlInput(packageResource("ArrayIndentTest.yml"));
        while (in.hasMore())
        {
            var next = in.read();
            var indentLevel = next.indentLevel();
            var lineNumber = next.lineNumber();
            ensure(indentLevel == indents[lineNumber - 1],
                "Indentation level $ wrong at line $: $: $", indentLevel, lineNumber, next.label(), next.text());
        }
    }

    @Test
    public void testNext()
    {
        var in = input();
        {
            ensure(in.current().isLiteral());
            ensure(in.lookahead().isString());
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 0);
            ensure(next.isLiteral());
            ensure(next.label().equals("type"));
            ensure(next.string().equals("object"));
        }
        {
            ensure(in.current().isString());
            ensure(in.lookahead().isLabel());
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 0);
            ensure(next.isString());
            ensure(next.label().equals("description"));
            ensure(next.string().equals("A distance in any unit"));
            ensure(in.lookahead().isLabel());
        }
        {
            ensure(in.current().isLabel());
            ensure(in.lookahead().isLabel());
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 0);
            ensure(next.isLabel());
            ensure(next.label().equals("properties"));
        }
        {
            ensure(in.current().isLabel());
            ensure(in.lookahead().isLiteral());
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 1);
            ensure(next.isLabel());
            ensure(next.label().equals("distance"));
        }
        {
            ensure(in.current().isLiteral());
            ensure(in.lookahead().isString());
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 2);
            ensure(next.isLiteral());
            ensure(next.label().equals("type"));
            ensure(next.string().equals("string"));
        }
        {
            ensure(in.current().isString());
            ensure(in.lookahead() == null);
            ensure(in.hasMore());
            var next = in.read();
            ensure(next.indentLevel() == 2);
            ensure(next.isString());
            ensure(next.label().equals("description"));
            ensure(next.string().equals("Distance in any unit, like 6 feet, 4.5 miles or 15 meters"));
            ensure(in.current() == null);
            ensure(!in.hasMore());
        }
    }

    @Test
    public void testSize()
    {
        var input = input();
        ensure(input.size() == 6);
    }

    /**
     * <pre>
     * type: object
     * description: "A distance in any unit"
     * properties:
     *   distance:
     *     type: string
     *     description: "Distance in any unit, like 6 feet, 4.5 miles or 15 meters"
     * </pre>
     */
    @NotNull
    private YamlInput input()
    {
        return new YamlInput(packageResource("Distance.yml"));
    }
}
