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

import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.AtEndOfDocument.STOP;
import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.FOUND;
import static com.telenav.kivakit.data.formats.xml.stax.StaxReader.Match.NOT_FOUND;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
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
        var factory = XMLInputFactory.newInstance();
        try
        {
            var in = resource.openForReading();
            return new StaxReader(factory.createXMLEventReader(in), in);
        }
        catch (XMLStreamException e)
        {
            return fail(e, "Unable to read XML resource: $", resource);
        }
    }

    /**
     * What to do at the end of the document
     */
    public enum AtEndOfDocument
    {
        STOP,
        THROW_EXCEPTION
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

    /** The input stream to auto-close at the end of reading */
    private final InputStream in;

    /** The current XML path */
    private StaxPath path;

    /** The current event */
    private XMLEvent at;

    /** True if reading should stop at the end of the document, false if an exception should be thrown */
    private AtEndOfDocument atEndOfDocument = STOP;

    /**
     * @param reader The Java STAX event reader
     * @param in The input stream that the reader is processing
     */
    protected StaxReader(final XMLEventReader reader, final InputStream in)
    {
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
        return at.isEndDocument();
    }

    /**
     * @param atEndOfDocument What to do if the end of document is reached unexpectedly
     */
    public void atEndOfDocument(final AtEndOfDocument atEndOfDocument)
    {
        this.atEndOfDocument = atEndOfDocument;
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
        return next(ignored -> isInside(path) ? FOUND : NOT_FOUND);
    }

    /**
     * @return True if there is a next event
     */
    public boolean hasNext()
    {
        return reader.hasNext();
    }

    public boolean isAtCharacters()
    {
        return at().isCharacters();
    }

    public boolean isAtCloseTag()
    {
        return at().isEndElement();
    }

    public boolean isAtOpenTag(String tagName)
    {
        return isAtOpenTag() && at().asStartElement().getName().getLocalPart().equals(tagName);
    }

    public boolean isAtOpenTag()
    {
        return at().isStartElement();
    }

    /**
     * @return True if the current XML path is inside the given path. For example, if the current path is a/b/c and the
     * given path is a/b/c or /a/b/c/d, this method would return true. However, if the path was a/b, it would return
     * false.
     */
    public boolean isInside(StaxPath path)
    {
        return path().startsWith(path);
    }

    /**
     * @return The next event, or an exception. If {@link #atEndOfDocument(AtEndOfDocument)} is true, returns null at
     * the end of the document instead.
     */
    public XMLEvent next()
    {
        return tryCatchThrow(() ->
        {
            at = reader.nextEvent();
            if (at.isStartElement())
            {
                path.push(at.asStartElement().getName().toString());
            }
            if (at.isEndElement())
            {
                path.pop();
            }
            if (at.isEndDocument() && atEndOfDocument == STOP)
            {
                return null;
            }

            return at;
        }, "Could not read next event");
    }

    /**
     * @param matcher The matcher
     * @return The next XMLEvent matching the matcher or null if the matcher asked to stop. A failure occurs (throwing
     * an exception) if a match cannot be found before the end of the XML stream.
     */
    public XMLEvent next(Matcher matcher)
    {
        for (; hasNext() && !atEnd(); next())
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

        return atEnd() ? null : fail("Out of input");
    }

    /**
     * @return The next attribute, or an exception is thrown
     */
    public Attribute nextAttribute()
    {
        return (Attribute) next((BooleanMatcher) XMLEvent::isAttribute);
    }

    /**
     * @return The next characters block, or an exception is thrown
     */
    public Characters nextCharacters()
    {
        return (Characters) next((BooleanMatcher) XMLEvent::isCharacters);
    }

    /**
     * @return The next close tag, or an exception is thrown
     */
    public EndElement nextCloseTag()
    {
        return (EndElement) next((BooleanMatcher) XMLEvent::isEndElement);
    }

    /**
     * @param path The path to stay inside
     * @param matcher The matcher to call
     * @return The next matching element inside the given path, or null if the element couldn't be found before leaving
     * the scope represented by the given path. If the element isn't found, the element returned by {@link #at()} will
     * be the one after the close tag where matching had to stop.
     */
    public XMLEvent nextInside(StaxPath path, Matcher matcher)
    {
        return next(event ->
        {
            if (!isInside(path))
            {
                return Match.STOP;
            }

            return matcher.matcher(at);
        });
    }

    /**
     * @return The next open tag, or an exception is thrown
     */
    public StartElement nextOpenTag()
    {
        return (StartElement) next((BooleanMatcher) XMLEvent::isStartElement);
    }

    /**
     * @return The current XML path
     */
    public StaxPath path()
    {
        return path;
    }
}
