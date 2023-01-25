package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

public class YamlReaderTest extends UnitTest
{
    @Test
    public void testRead()
    {
        testRead("Distance.yml");
        testRead("Location.yml");
        testRead("Rectangle.yml");
        testRead("LocationArray.yml");
    }

    private void testRead(String name)
    {
        var yaml = packageResource(name);
        var block = new YamlReader().read(yaml);
        ensureEqual(block.asYaml().toString().trim(), yaml.readText().trim());
    }
}
