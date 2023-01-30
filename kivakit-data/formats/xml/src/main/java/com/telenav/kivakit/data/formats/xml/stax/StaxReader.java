package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.resource.Resource;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Closeable;
import java.io.InputStream;
import java.util.function.Function;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.FOUND;
import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.NOT_FOUND;

/**
 * <p>
 * A thin wrapper around Java's STAX XML stream API (java.xml.stream) that makes it easy to locate XML elements in the
 * stream, including elements relative to XML {@link StaxPath}s.
 * </p>
 *
 * <p><b>Using a {@link StaxReader}</b></p>
 *
 * <p>
 * A STAX reader can be created with the {@link #openXml(Resource)} method. The method will either successfully return a
 * {@link StaxReader} or it will throw and exception. After information from the resource's XML stream has been
 * processed, the reader can be closed with {@link #close()}. Since {@link StaxReader} implements {@link Closeable}, it
 * can be used within a try-with-resources statement.
 * </p>
 *
 * <p><b>Reader State</b></p>
 *
 * <p>
 * These methods give the reader's position in the XML stream and its current element:
 * </p>
 *
 * <ul>
 *     <li>{@link #path()} - The current {@link StaxPath} the reader is at</li>
 *     <li>{@link #at()} - The element that the STAX parser is at</li>
 * </ul>
 *
 * <p><b>Moving Through the Stream</b></p>
 *
 * <p>
 * The following methods advance the reader to a subsequent element in the stream.
 * </p>
 *
 * <ul>
 *     <li>{@link #hasNext()} - True if the reader has more elements</li>
 *     <li>{@link #next()} - Advances the reader to the next element</li>
 *     <li>{@link #nextAttribute()} - Advances to the next XML attribute</li>
 *     <li>{@link #nextCharacters()} - Advances to the next text element</li>
 *     <li>{@link #nextCloseTag()} - Advances to the next close tag</li>
 *     <li>{@link #nextMatching(Matcher)} - Advances to the next element matching the given matcher</li>
 *     <li>{@link #nextOpenTag()} - Advances to the next open tag</li>
 *     <li>{@link #nextAtOrInside(StaxPath, Matcher)} - Advances ot the next element matching the given matcher that is at or under the given StaxPath</li>
 *     <li>{@link #enclosedText()} - If the stream is at an open tag and the next element is a text element, returns the text element and advances to the close tag.</li>
 * </ul>
 *
 * <p><b>Stream Positioning</b></p>
 *
 * <p>
 * The following methods help to determine where the reader is positioned in the XML input stream.
 * </p>
 *
 * <ul>
 *     <li>{@link #isAtEnd()} - True if the reader is at the end of the XML stream</li>
 *     <li>{@link #isAtCharacters()} - True if the reader is at a text element</li>
 *     <li>{@link #isAtOpenTag()} - True if the reader is at an open tag</li>
 *     <li>{@link #isAtCloseTag()} ()} - True if the reader is at a close tag</li>
 *     <li>{@link #isAtOpenCloseTag()} - True if the reader is at an "open close" tag like &lt;tag/&gt;</li>
 * </ul>
 *
 *
 * <p><b>StaxPath Positioning Operations</b></p>
 *
 * <p>
 * The following methods test the position of the reader relative to a given {@link StaxPath}. The reader is considered <i>at</i>
 * a given path if the sequence of elements leading to the current element (the reader's path) is equal to the given path.
 * For example, if the reader is at the hierarchical element path a/b/c and the given path is a/b/c, the reader is <i>at</i>
 * the given path. The reader is <i>inside</i> a path if the given path is a prefix of the reader's current path. For example,
 * if the reader is at a/b/c/d and the path is a/b/c, then the reader is <i>inside</i> the given path. Finally, the
 * reader is <i>outside</i> the given path in the reverse situation, where the reader is at a/b and the path is a/b/c.
 * </p>
 *
 * <ul>
 *     <li>{@link #findNext(StaxPath)} - Advances the reader to the next location where {@link #path()} matches the given path</li>
 *     <li>{@link #isAt(StaxPath)} - True if the reader is at the level of the given {@link StaxPath}</li>
 *     <li>{@link #isInside(StaxPath)} - True if the reader is under the given {@link StaxPath}</li>
 *     <li>{@link #isAtOrInside(StaxPath)} - True if the reader is at or under the level of the given {@link StaxPath}</li>
 *     <li>{@link #isOutside(StaxPath)} - True if the reader is above the given {@link StaxPath}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see StaxPath
 * @see XMLEventReader
 * @see XMLEvent
 * @see Matcher
 * @see Resource
 */
@SuppressWarnings("unused")
public class StaxReader extends BaseComponent implements Closeable
{
    /**
     * Returns a STAX reader (Java XML stream API) for the given resource, or an exception will be thrown
     */
    public static StaxReader openXml(Resource resource)
    {
        ensureNotNull(resource);

        var factory = XMLInputFactory.newInstance();
        try
        {
            var in = ensureNotNull(resource.openForReading(), "Could not open for reading: $", resource);
            return new StaxReader(resource, factory.createXMLEventReader(in), in);
        }
        catch (XMLStreamException e)
        {
            return fail(e, "Unable to read XML resource: $", resource);
        }
    }

    /**
     * Match state for {@link Matcher}s
     */
    public enum Match
    {
        FOUND,
        NOT_FOUND,
        STOP
    }

    /**
     * A {@link Matcher} implementation for a boolean function
     */
    public interface BooleanMatcher extends Function<XMLEvent, Boolean>, Matcher
    {
        @Override
        default Match matcher(XMLEvent event)
        {
            return apply(event) ? FOUND : NOT_FOUND;
        }
    }

    /**
     * Returns a {@link Match} type for a given XML event
     */
    public interface Matcher
    {
        Match matcher(XMLEvent event);
    }

    /** The STAX event reader */
    private final XMLEventReader reader;

    /** The resource that this reader is reading from */
    private final Resource resource;

    /** The input stream to auto-close at the end of reading */
    private final InputStream in;

    /** The current XML path */
    private final StaxPath path = new StaxPath();

    /** The current event */
    private XMLEvent at;

    /** The number of events that have been observed. */
    private int events;

    /**
     * @param reader The Java STAX event reader
     * @param in The input stream that the reader is processing
     */
    private StaxReader(Resource resource, XMLEventReader reader, InputStream in)
    {
        this.resource = resource;
        this.reader = reader;
        this.in = in;
    }

    /**
     * Returns the current event
     */
    public XMLEvent at()
    {
        return at;
    }

    @Override
    public void close()
    {
        IO.close(resource, in);
    }

    /**
     * Returns the text enclosed by an open/end tag pair. Must be called at an open tag. The following character data is
     * read and the XML stream is advanced to the close tag.
     */
    public String enclosedText()
    {
        // Skip current open tag,
        ensure(isAtOpenTag());
        next();

        // read character data,
        ensure(isAtCharacters());
        var data = at().asCharacters().getData();

        // skip to close tag
        next();
        ensure(isAtCloseTag());

        // and return the character data.
        return data;
    }

    /**
     * Finds the next element under the given path, or an exception is thrown
     */
    public XMLEvent findNext(StaxPath path)
    {
        return nextMatching(ignored -> this.path.isInside(path) ? FOUND : NOT_FOUND);
    }

    /**
     * Returns true if there is a next event
     */
    public boolean hasNext()
    {
        return !isAtEnd() && reader.hasNext();
    }

    /**
     * Returns true if this reader is at, but not under, the given path
     */
    public boolean isAt(StaxPath path)
    {
        return this.path.isAt(path);
    }

    /**
     * Returns true if this reader is at a characters element
     */
    public boolean isAtCharacters()
    {
        return at().isCharacters();
    }

    /**
     * Returns true if this reader is at a close tag
     */
    public boolean isAtCloseTag()
    {
        return at().isEndElement();
    }

    /**
     * Returns true if the current position is the end of the document
     */
    public boolean isAtEnd()
    {
        return events > 0 && at == null;
    }

    /**
     * Returns true if this reader is at an open/close tag like &lt;tag/&gt;
     */
    public boolean isAtOpenCloseTag()
    {
        return at != null && at().isStartElement() && at().isEndElement();
    }

    /**
     * Returns true if this reader is at an open tag with the given name
     */
    public boolean isAtOpenTag(String tagName)
    {
        return isAtOpenTag() && at().asStartElement().getName().getLocalPart().equals(tagName);
    }

    /**
     * Returns true if this reader is at an open tag
     */
    public boolean isAtOpenTag()
    {
        return at != null && at().isStartElement() && !at().isEndElement();
    }

    /**
     * Returns true if this reader is at, or under, the given path
     */
    public boolean isAtOrInside(StaxPath path)
    {
        return isAt(path) || isInside(path);
    }

    /**
     * Returns true if the this path is hierarchically "under" the given path. For example, if this path is a/b/c and
     * the given path is /a/b, this method would return true. However, if this path was a/b/c, and the given path was
     * /a/b/c or /a/b/c/d, it would return false.
     */
    public boolean isInside(StaxPath path)
    {
        return this.path.isInside(path);
    }

    /**
     * Returns true if this reader is at, or under, the given path
     */
    public boolean isOutside(StaxPath path)
    {
        return !isAtOrInside(path);
    }

    /**
     * Returns the next event, or null if there are no more events
     */
    public XMLEvent next()
    {
        try
        {
            at = reader.nextEvent();
            events++;
            if (at.isStartElement())
            {
                path.push(at.asStartElement().getName().getLocalPart());
            }
            if (at.isEndElement())
            {
                path.pop();
            }
            if (at.isEndDocument())
            {
                return null;
            }
        }
        catch (Exception e)
        {
            at = null;
        }

        return at;
    }

    /**
     * @param path The path to stay at or under (inside)
     * @param matcher The matcher to call
     * @return The next matching element inside the given path, or null if the element couldn't be found before leaving
     * the scope represented by the given path. If the element isn't found, the element returned by {@link #at()} will
     * be the one after the close tag where matching had to stop.
     */
    public XMLEvent nextAtOrInside(StaxPath path, Matcher matcher)
    {
        return nextMatching(event ->
        {
            // If we aren't under the given path anymore,
            if (!isAtOrInside(path))
            {
                // then advance to the next element,
                next();

                // and stop.
                return Match.STOP;
            }

            // Otherwise, delegate to the given matcher
            return matcher.matcher(at);
        });
    }

    /**
     * Returns the next attribute, or an exception is thrown
     */
    public Attribute nextAttribute()
    {
        return (Attribute) nextMatching((BooleanMatcher) XMLEvent::isAttribute);
    }

    /**
     * Returns the next characters block, or an exception is thrown
     */
    public Characters nextCharacters()
    {
        return (Characters) nextMatching((BooleanMatcher) XMLEvent::isCharacters);
    }

    /**
     * Returns the next close tag, or an exception is thrown
     */
    public EndElement nextCloseTag()
    {
        return (EndElement) nextMatching((BooleanMatcher) XMLEvent::isEndElement);
    }

    /**
     * @param matcher The matcher
     * @return The next XMLEvent matching the matcher or null if the matcher asked to stop. A failure occurs (throwing
     * an exception) if a match cannot be found before the end of the XML stream.
     */
    public XMLEvent nextMatching(Matcher matcher)
    {
        for (; hasNext(); next())
        {
            switch (matcher.matcher(at))
            {
                case FOUND:
                    return at;

                case STOP:
                    return null;

                case NOT_FOUND:
                default:
                    break;
            }
        }

        return !hasNext() ? null : fail("Out of input");
    }

    /**
     * Returns the next open tag, or an exception is thrown
     */
    public StartElement nextOpenTag()
    {
        return (StartElement) nextMatching((BooleanMatcher) XMLEvent::isStartElement);
    }

    /**
     * Returns a copy of the current XML path
     */
    public StaxPath path()
    {
        return path.copy();
    }

    @Override
    public String toString()
    {
        return resource.path() + ":" + path + " => " + at;
    }
}
