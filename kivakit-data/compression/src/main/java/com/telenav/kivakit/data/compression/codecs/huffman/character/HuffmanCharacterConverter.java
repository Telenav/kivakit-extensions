package com.telenav.kivakit.data.compression.codecs.huffman.character;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strip;

public class HuffmanCharacterConverter extends BaseStringConverter<Character>
{
    public HuffmanCharacterConverter(Listener listener)
    {
        super(listener, Character.class);
    }

    @Override
    protected String onToString(Character character)
    {
        return "0x" + Ints.intToHex(character, 2);
    }

    @Override
    protected Character onToValue(String value)
    {
        return (char) Long.parseLong(Strip.stripLeading(value, "0x"), 16);
    }
}
