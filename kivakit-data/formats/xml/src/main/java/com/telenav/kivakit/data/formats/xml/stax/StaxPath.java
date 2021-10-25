package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.kernel.language.paths.StringPath;

import java.util.List;

public class StaxPath extends StringPath
{
    public static StaxPath parseXmlPath(String path)
    {
        return new StaxPath().withChild(path);
    }

    public StaxPath()
    {
        super(List.of());
    }

    @Override
    public String separator()
    {
        return "/";
    }

    @Override
    public StaxPath withChild(final String element)
    {
        return (StaxPath) super.withChild(element);
    }

    @Override
    protected StaxPath copy()
    {
        return new StaxPath();
    }
}
