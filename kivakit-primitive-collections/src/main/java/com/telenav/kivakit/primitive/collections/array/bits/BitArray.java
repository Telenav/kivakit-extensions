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

package com.telenav.kivakit.primitive.collections.array.bits;

import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.primitive.collections.array.PrimitiveArray;
import com.telenav.kivakit.primitive.collections.array.bits.io.BitReader;
import com.telenav.kivakit.primitive.collections.array.bits.io.BitWriter;
import com.telenav.kivakit.primitive.collections.array.bits.io.input.BaseBitReader;
import com.telenav.kivakit.primitive.collections.array.bits.io.output.BaseBitWriter;
import com.telenav.kivakit.primitive.collections.array.scalars.ByteArray;
import com.telenav.kivakit.primitive.collections.list.ByteList;
import com.telenav.kivakit.primitive.collections.project.lexakai.diagrams.DiagramPrimitiveArray;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * An array of bits backed by a {@link ByteArray}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveArray.class)
public final class BitArray extends PrimitiveArray implements Named
{
    /** The byte array */
    private ByteList bytes;

    /** The index where we started writing */
    private int offset;

    public BitArray(String objectName)
    {
        super(objectName);
    }

    public BitArray(String objectName, ByteList bytes)
    {
        super(objectName);
        this.bytes = bytes;
        offset = bytes.cursor();
    }

    /**
     * @return The bit at the given index
     */
    public boolean bit(int index)
    {
        var mask = 0x80 >>> (index % 8);
        return (getByte(index / 8) & mask) != 0;
    }

    /**
     * @return The underlying bytes
     */
    public ByteList bytes()
    {
        return bytes;
    }

    @Override
    public Count capacity()
    {
        return bytes.capacity();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object object)
    {
        return unsupported();
    }

    @Override
    public int hashCode()
    {
        return unsupported();
    }

    @Override
    public Method onCompress(Method method)
    {
        return bytes.compress(method);
    }

    @Override
    public void onInitialize()
    {
        if (bytes == null)
        {
            var bytes = new ByteArray(objectName() + ".bytes");
            bytes.initialSize(initialSize().dividedBy(8))
                    .maximumSize(maximumSize().dividedBy(8))
                    .hasNullByte(false);
            bytes.initialize();
            this.bytes = bytes;
        }
    }

    /**
     * @return A reader that reads the bits in this bit array
     */
    public BitReader reader()
    {
        return new BaseBitReader(bytes, count()) {};
    }

    /**
     * Sets the bit at the given index
     */
    public void set(int index, boolean value)
    {
        var mask = 0x80 >>> (index % 8);
        var current = getByte(index / 8);
        setByte(index / 8, (byte) ((current & ~mask) | (value ? 1 : 0)));
        size(Math.max(size(), index + 1));
    }

    public String toBitString()
    {
        return toString("", 8, " ", index -> bit(index) ? "1" : "0", 256);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return Message.format("[BitArray name = '$', size = $, values = $]", name(), size(), toBitString());
    }

    /**
     * @return A writer that writes bits to this bit array
     */
    public BitWriter writer()
    {
        var outer = this;
        return new BaseBitWriter()
        {
            @Override
            public void close()
            {
                try
                {
                    super.close();
                    size((int) super.cursor());
                }
                catch (Exception ignored)
                {
                }
                outer.bytes.compress(Method.RESIZE);
            }

            @Override
            protected void onFlush(byte value)
            {
                // Store the byte, but don't advance the add cursor
                outer.bytes.set(outer.bytes.cursor(), value);
            }

            @Override
            protected void onWrite(byte value)
            {
                outer.bytes.add(value);
                size(size() + 8);
            }
        };
    }

    private byte getByte(int index)
    {
        return bytes.get(offset + index);
    }

    private void setByte(int index, byte value)
    {
        bytes.set(offset + index, value);
    }
}
