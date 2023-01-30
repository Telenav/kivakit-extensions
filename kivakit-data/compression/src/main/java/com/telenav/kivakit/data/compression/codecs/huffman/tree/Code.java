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

import com.telenav.kivakit.core.string.Align;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.primitive.collections.array.bits.io.BitWriter;

/**
 * A Huffman code, having a length in bits and a value. The code can be written to a {@link BitWriter} with {@link
 * #write(BitWriter)}.
 *
 * @author jonathanl (shibo)
 * @see BitWriter
 */
public class Code
{
    private int lengthInBits;

    private int value;

    /**
     * Constructs a code from a binary string
     */
    public Code(String bits)
    {
        if (Strings.isNullOrBlank(bits))
        {
            lengthInBits = 0;
            value = 0;
        }
        else
        {
            lengthInBits = bits.length();
            value = Integer.parseInt(bits, 2);
        }
    }

    protected Code()
    {
    }

    public int lengthInBits()
    {
        return lengthInBits;
    }

    @Override
    public String toString()
    {
        return Align.rightAlign(Integer.toBinaryString(value), lengthInBits, '0');
    }

    public int value()
    {
        return value;
    }

    public void write(BitWriter writer)
    {
        writer.write(value, lengthInBits);
    }
}
