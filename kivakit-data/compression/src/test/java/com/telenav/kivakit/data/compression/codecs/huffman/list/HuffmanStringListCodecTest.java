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

package com.telenav.kivakit.data.compression.codecs.huffman.list;

import com.telenav.kivakit.core.collections.map.CountMap;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.value.count.Minimum;
import com.telenav.kivakit.data.compression.Codec;
import com.telenav.kivakit.data.compression.codecs.huffman.DataCompressionUnitTest;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.collections.Lists.newArrayList;
import static com.telenav.kivakit.core.value.count.Count._10;
import static com.telenav.kivakit.core.value.count.Count._100;
import static com.telenav.kivakit.core.value.count.Count._1024;
import static com.telenav.kivakit.core.value.count.Count.parseCount;
import static com.telenav.kivakit.data.compression.SymbolConsumer.Directive.CONTINUE;
import static com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec.END_OF_STRING;
import static com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec.ESCAPE;
import static com.telenav.kivakit.data.compression.codecs.huffman.character.HuffmanCharacterCodec.characterCodec;
import static com.telenav.kivakit.data.compression.codecs.huffman.string.HuffmanStringCodec.stringCodec;

@SuppressWarnings("SpellCheckingInspection")
public class HuffmanStringListCodecTest extends DataCompressionUnitTest
{
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testBug()
    {
        //    [HuffmanCodec size = 2, bits = 1]:
        //        1. 0 -> 'm' (8,524)
        //        2. 1 -> 'gxafxac' (9,202)
        //    [HuffmanCodec size = 7, bits = 5]:
        //        1. 11100 -> 0x00 (1,024)
        //        2. 11101 -> 0x01 (1,024)
        //        3. 1111 -> 'x' (5,566)
        //        4. 110 -> 'e' (6,379)
        //        5. 00 -> 'j' (8,098)
        //        6. 01 -> 'd' (8,154)
        //        7. 10 -> 'm' (10,826)
        //
        //    input: [ohkh, m, ohkh, m, gxafxac, ohkh, gxafxac, m, m, gxafxac, ohkh, m, gxafxac, gxafxac, gxafxac, ohkh, m, m, m]

        var stringSymbols = new Symbols<>(new CountMap<String>()
                .plus("m", parseCount(this, "8,524"))
                .plus("gxafxac", parseCount(this, "9,202")));

        var stringCodec = stringCodec(stringSymbols, Maximum._8);

        var characterSymbols = new Symbols<>(new CountMap<Character>()
                .plus('m', parseCount(this, "10,826"))
                .plus('d', parseCount(this, "8,154"))
                .plus('j', parseCount(this, "8,098"))
                .plus('e', parseCount(this, "6,379"))
                .plus('x', parseCount(this, "5,566"))
                .plus(ESCAPE, _1024)
                .plus(END_OF_STRING, _1024), ESCAPE, Minimum._2);

        var characterCodec = characterCodec(characterSymbols, Maximum._8);

        var codec = new HuffmanStringListCodec(stringCodec, characterCodec);

        test(codec, newArrayList("ohkh", "m", "ohkh", "m", "gxafxac", "ohkh", "gxafxac",
                "m", "m", "gxafxac", "ohkh", "m", "gxafxac", "gxafxac", "gxafxac", "ohkh", "m", "m", "m"));

        test(codec, newArrayList("ohkh"));
    }

    @Test
    public void testDecode()
    {
        var stringCodec = stringCodec(properties("string.codec"));
        var characterCodec = characterCodec(this, properties("character.codec"), ESCAPE);
        var codec = new HuffmanStringListCodec(stringCodec, characterCodec);

        test(codec, newArrayList("bicycle", "barrier", "highway", "banana"));
        test(codec, newArrayList("oneway", "turkey", "foot", "access", "footway"));
        test(codec, newArrayList("gorilla", "amenity", "footway", "monkey", "maxspeed", "footway"));
    }

    @Test
    public void testRandom()
    {
        _10.forEachInteger(codecNumber ->
        {
            var stringSymbols = randomStringSymbols(2, 16, 2, 32);
            var string = stringCodec(stringSymbols, Maximum._8);

            var characterSymbols = randomCharacterSymbols(2, 25);
            var character = characterCodec(characterSymbols, Maximum._8);

            var codec = new HuffmanStringListCodec(string, character);

            var choices = stringSymbols.symbols();
            choices.addAll(randomStringSymbols(2, 8, 2, 32).symbols());

            _100.forEachInteger(testNumber ->
            {
                var input = new ArrayList<String>();
                random().rangeInclusive(2, 32).loop(() -> input.add(choices.get(random().randomIntExclusive(0, choices.size() - 1))));
                test(codec, input);
            });
        });
    }

    @Override
    protected void test(Codec<String> codec, List<String> symbols)
    {
        // Encode symbols with codec,
        var encoded = encode(codec, symbols);
        encoded.reset();

        // create a destination array, decoded, and fill it with nulls,
        var decoded = new ArrayList<>();
        count(symbols).loop(() -> decoded.add(null));

        // then decode the data,
        codec.decode(encoded, (index, value) ->
        {
            // setting each value into decoded array
            decoded.set(index, value);
            return CONTINUE;
        });

        // Finally, the input symbols and the decoded data should be the same.
        ensureEqual(decoded, symbols);
    }
}
