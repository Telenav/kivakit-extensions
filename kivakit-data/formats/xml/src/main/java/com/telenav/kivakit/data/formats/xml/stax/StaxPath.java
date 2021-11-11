package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
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
     * {@inheritDoc}
     */
    @Override
    public StaxPath copy()
    {
        return (StaxPath) super.copy();
    }

    /**
     * @return True if this path is the given path, but not a path under the given path
     */
    public boolean isAt(StaxPath path)
    {
        return equals(path);
    }

    /**
     * @return True if the this path is hierarchically "under" the given path. For example, if this path is a/b/c and
     * the given path is a/b/c or /a/b, this method would return true. However, if this path was a/b/c, and the given
     * path was /a/b/c/d, it would return false.
     */
    public boolean isInside(StaxPath path)
    {
        return !isAt(path) && startsWith(path);
    }

    @Override
    public String separator()
    {
        return "/";
    }

    @Override
    public StaxPath withChild(String element)
    {
        return (StaxPath) super.withChild(element);
    }

    @Override
    protected StaxPath onCopy(String root, List<String> elements)
    {
        var path = new StaxPath();
        path.elements().addAll(elements);
        return path;
    }
}
