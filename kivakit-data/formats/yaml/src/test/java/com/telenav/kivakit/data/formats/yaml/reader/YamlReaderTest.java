package com.telenav.kivakit.data.formats.yaml.reader;

import com.telenav.kivakit.data.formats.yaml.BaseYamlTest;
import org.junit.Test;

public class YamlReaderTest extends BaseYamlTest
{
    @Test
    public void testRead()
    {
        testRead("Servers.yml");
        testRead("LocationArray.yml");
        testRead("Location.yml");
        testRead("Distance.yml");
        testRead("Rectangle.yml");
    }

    private void testRead(String name)
    {
        var yaml = packageResource("resources/" + name);
        var node = new YamlReader().read(yaml);
        ensureEqual(node.toString().trim(), yaml.readText().trim());
    }
}
