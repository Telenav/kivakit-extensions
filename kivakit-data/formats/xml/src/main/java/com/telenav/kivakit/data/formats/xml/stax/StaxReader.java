package com.telenav.kivakit.data.formats.xml.stax;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.kernel.interfaces.function.BooleanFunction;
import com.telenav.kivakit.kernel.language.io.IO;
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

import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.FOUND;
import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.NOT_FOUND;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * A thin wrapper around Java's STAX XML stream API that makes it easy to locate points in the XML stream.
 *
 * @author jonathanl (shibo)
 */
public class StaxReader extends BaseComponent implements Closeable
{
    /**
     * @return A STAX reader (Java XML stream API) for the given resource
     */
    public static StaxReader open(Resource resource)
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
    public interface BooleanMatcher extends BooleanFunction<XMLEvent>, Matcher
    {
        @Override
        default Match matcher(XMLEvent event)
        {
            return isTrue(event) ? FOUND : NOT_FOUND;
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
    private Resource resource;

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
    protected StaxReader(Resource resource, XMLEventReader reader, InputStream in)
    {
        this.resource = resource;
        this.reader = reader;
        this.in = in;
    }

    /**
     * @return The current event
     */
    public XMLEvent at()
    {
        return at;
    }

    /**
     * @return True if the current position is the end of the document
     */
    public boolean atEnd()
    {
        return events > 0 && at == null;
    }

    @Override
    public void close()
    {
        IO.close(in);
    }

    /**
     * @return The text enclosed by an open/end tag pair. Must be called at an open tag. The following character data is
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
        return nextMatching(ignored -> this.path.isUnder(path) ? FOUND : NOT_FOUND);
    }

    /**
     * @return True if there is a next event
     */
    public boolean hasNext()
    {
        return !atEnd() && reader.hasNext();
    }

    /**
     * @return True if this reader is at, but not under, the given path
     */
    public boolean isAt(StaxPath path)
    {
        return this.path.isAt(path);
    }

    /**
     * @return True if this reader is at a characters element
     */
    public boolean isAtCharacters()
    {
        return at().isCharacters();
    }

    /**
     * @return True if this reader is at a close tag
     */
    public boolean isAtCloseTag()
    {
        return at().isEndElement();
    }

    /**
     * @return True if this reader is at an open/close tag like &lt;tag/&gt;
     */
    public boolean isAtOpenCloseTag()
    {
        return at != null && at().isStartElement() && at().isEndElement();
    }

    /**
     * @return True if this reader is at an open tag with the given name
     */
    public boolean isAtOpenTag(String tagName)
    {
        return isAtOpenTag() && at().asStartElement().getName().getLocalPart().equals(tagName);
    }

    /**
     * @return True if this reader is at an open tag
     */
    public boolean isAtOpenTag()
    {
        return at != null && at().isStartElement() && !at().isEndElement();
    }

    /**
     * @return True if this reader is at, or under, the given path
     */
    public boolean isAtOrUnder(StaxPath path)
    {
        return isAt(path) || isUnder(path);
    }

    /**
     * @return True if the this path is hierarchically "under" the given path. For example, if this path is a/b/c and
     * the given path is /a/b, this method would return true. However, if this path was a/b/c, and the given path was
     * /a/b/c or /a/b/c/d, it would return false.
     */
    public boolean isUnder(StaxPath path)
    {
        return this.path.isUnder(path);
    }

    /**
     * @return The next event, or null if there are no more events
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
    public XMLEvent nextAtOrUnder(StaxPath path, Matcher matcher)
    {
        return nextMatching(event ->
        {
            // If we aren't under the given path anymore,
            if (!isAtOrUnder(path))
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
     * @return The next attribute, or an exception is thrown
     */
    public Attribute nextAttribute()
    {
        return (Attribute) nextMatching((BooleanMatcher) XMLEvent::isAttribute);
    }

    /**
     * @return The next characters block, or an exception is thrown
     */
    public Characters nextCharacters()
    {
        return (Characters) nextMatching((BooleanMatcher) XMLEvent::isCharacters);
    }

    /**
     * @return The next close tag, or an exception is thrown
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
     * @return The next open tag, or an exception is thrown
     */
    public StartElement nextOpenTag()
    {
        return (StartElement) nextMatching((BooleanMatcher) XMLEvent::isStartElement);
    }

    /**
     * @return The current XML path
     */
    public StaxPath path()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return resource.path() + ":" + path + " => " + at;
    }
}
