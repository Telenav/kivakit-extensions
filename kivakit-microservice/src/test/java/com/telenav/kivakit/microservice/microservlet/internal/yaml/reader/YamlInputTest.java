package com.telenav.kivakit.microservice.microservlet.internal.yaml.reader;

import com.telenav.kivakit.microservice.internal.yaml.reader.YamlInput;
import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class YamlInputTest extends UnitTest
{
    @Test
    public void testBlock()
    {
        var input = input();
        for (var line : input.block())
        {
            println(line.toString());
        }
    }

    @Test
    public void testNext()
    {
        var input = input();
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isLiteral());
            ensure(at.label().equals("type"));
            ensure(at.string().equals("object"));
        }
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isString());
            ensure(at.label().equals("description"));
            ensure(at.string().equals("A distance in any unit"));
        }
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isLabel());
            ensure(at.label().equals("properties"));
        }
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isLabel());
            ensure(at.label().equals("distance"));
        }
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isLiteral());
            ensure(at.label().equals("type"));
            ensure(at.string().equals("string"));
        }
        {
            ensure(input.hasNext());
            var at = input.next();
            ensure(at.isString());
            ensure(at.label().equals("description"));
            ensure(at.string().equals("Distance in any unit, like 6 feet, 4.5 miles or 15 meters"));
        }

        ensure(!input.hasNext());
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
