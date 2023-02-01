package com.telenav.kivakit.microservice.microservlet.internal.yaml.reader;

import com.telenav.kivakit.microservice.internal.yaml.reader.YamlReader;
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
    }

    private void testRead(String name)
    {
        var yaml = packageResource(name);
        var block = new YamlReader().read(yaml);
        ensureEqual(block.asYaml().toString().trim(), yaml.readText().trim());
    }
}
