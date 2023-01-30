package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.path.StringPath;

import java.util.List;

/**
 * <p>
 * An element path in an XML stream.
 * </p>
 *
 * <p>
 * A {@link StaxPath} gives the sequence of elements that must be encountered to get from the root element of the XML
 * document to a position in the document's element stream. For example, when streaming the following XML document, if
 * the current element is the one indicated by the arrow, the path is a/b:
 * </p>
 *
 * <pre>
 * &lt;a&gt;
 *     &lt;b&gt;   &lt;--- {@link StaxPath} is a/b
 *         &lt;c&gt;
 *         &lt;/c&gt;
 *     &lt;/b&gt;
 * &lt;a&gt;
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see StaxReader
 */
@SuppressWarnings("unused")
public class StaxPath extends StringPath
{
    /**
     * @param path A slash-separated XML path
     * @return A STAX path for the given string
     */
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
     * Returns true if this path is the given path, but not a path under the given path
     */
    public boolean isAt(StaxPath path)
    {
        return equals(path);
    }

    /**
     * Returns true if the this path is hierarchically "under" the given path. For example, if this path is a/b/c and
     * the given path is a/b/c or /a/b, this method would return true. However, if this path was a/b/c, and the given
     * path was /a/b/c/d, it would return false.
     */
    public boolean isInside(StaxPath path)
    {
        return !isAt(path) && startsWith(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaxPath parent()
    {
        return (StaxPath) super.parent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String separator()
    {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
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
