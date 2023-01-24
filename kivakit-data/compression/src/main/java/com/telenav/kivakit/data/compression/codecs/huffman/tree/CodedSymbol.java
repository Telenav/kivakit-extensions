////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.compression.codecs.huffman.tree;

import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.core.string.StringConversions;
import com.telenav.kivakit.core.value.count.Count;

/**
 * An encoded symbol in a Huffman {@link Tree}, having a symbol value, a frequency and a {@link Code}.
 *
 * @param <Symbol> The symbol value type
 * @author jonathanl (shibo)
 * @see Code
 */
public class CodedSymbol<Symbol>
{
    /** The number of times the symbol appears in the training input */
    private long frequency;

    /** The value of the symbol */
    private Symbol symbol;

    /** The Huffman {@link Code} for the symbol */
    private Code code;

    /**
     * Constructs a symbol with a frequency of 1
     *
     * @param value The symbol value
     */
    public CodedSymbol(Symbol value)
    {
        this(value, Count._1);
    }

    /**
     * Constructs a symbol with the given value and frequency
     */
    public CodedSymbol(Symbol value, Count frequency)
    {
        assert value != null;
        symbol = value;
        this.frequency = frequency.asInt();
    }

    protected CodedSymbol()
    {
    }

    public Code code()
    {
        assert code != null;
        return code;
    }

    public void code(Code code)
    {
        this.code = code;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof CodedSymbol)
        {
            var that = (CodedSymbol<Symbol>) object;
            return symbol.equals(that.symbol);
        }
        return false;
    }

    public long frequency()
    {
        return frequency;
    }

    @Override
    public int hashCode()
    {
        return symbol.hashCode();
    }

    public void increaseFrequency(long count)
    {
        frequency += count;
    }

    @Override
    public String toString()
    {
        return Formatter.format("[Symbol value = '$', frequency = $, code = $]", StringConversions.toHumanizedString(symbol), frequency, code);
    }

    public Symbol value()
    {
        return symbol;
    }
}
