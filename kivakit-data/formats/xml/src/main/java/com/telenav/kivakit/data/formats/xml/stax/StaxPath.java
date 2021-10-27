package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.paths.Path;
import com.telenav.kivakit.kernel.language.paths.StringPath;

import java.util.List;

public class StaxPath extends StringPath
{
    public static StaxPath parseXmlPath(String path)
    {
        var stax = new StaxPath();
        stax.elements().addAll(StringList.split(path, "/"));
        return stax;
    }

    public StaxPath()
    {
        super(List.of());
    }

    /**
     * @return True if the this path is inside the given path. For example, if the current path is a/b/c and the given
     * path is a/b/c or /a/b/c/d, this method would return true. However, if the current path was a/b, and the given
     * path was /a/b/c/d, it would return false.
     */
    public boolean isInside(StaxPath path)
    {
        return startsWith(path);
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
    protected Path<String> onCopy(final String root, final List<String> elements)
    {
        var path = new StaxPath();
        path.elements().addAll(elements);
        return path;
    }
}
