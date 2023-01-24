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

package com.telenav.kivakit.data.compression.codecs.huffman;

import com.telenav.kivakit.core.collections.map.CountMap;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.data.compression.SymbolConsumer;
import com.telenav.kivakit.data.compression.codecs.huffman.tree.Symbols;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.value.count.Count._10;
import static com.telenav.kivakit.core.value.count.Count._100;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class HuffmanCodecTest extends DataCompressionUnitTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * 123,869,689 per second
     */
    // @Test
    @SuppressWarnings("JUnit3StyleTestMethodInJUnit4Class")
    public void testBenchmark()
    {
        var symbols = new Symbols<>(new CountMap<String>()
                .plus("a", Count._1)
                .plus("b", _10)
                .plus("c", Count._1_000)
                .plus("d", _100)
                .plus("last", Count._10_000));

        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> a (1)
        //        2. 001 -> b (10)
        //        3. 01 -> d (100)
        //        4. 1 -> c (1,000)

        // Message.println(codec.toString());

        var encoded = encode(codec, List.of("a", "b", "a", "c", "a", "a", "a", "a", "last"));

        var progress = BroadcastingProgressReporter.progressReporter(LOGGER);
        for (int i = 0; i < 1_000_000_000; i++)
        {
            codec.decode(encoded, (ordinal, next) -> "last".equals(next) ? SymbolConsumer.Directive.STOP : SymbolConsumer.Directive.CONTINUE);
            progress.next();
        }
    }

    @Test
    public void testDecode()
    {
        var symbols = fixedSymbolSet();
        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> jkl (1)
        //        2. 001 -> ghi (10)
        //        3. 01 -> def (100)
        //        4. 1 -> abc (1,000)

        // Message.println(codec.toString());

        test(codec, List.of("abc", "abc", "abc"));
        test(codec, List.of("def", "def", "def"));
        test(codec, List.of("ghi", "ghi", "ghi"));
        test(codec, List.of("jkl", "jkl", "jkl", "jkl", "jkl"));
    }

    @Test
    public void testEncode()
    {
        var symbols = fixedSymbolSet();

        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 000 -> jkl (1)
        //        2. 001 -> ghi (10)
        //        3. 01 -> def (100)
        //        4. 1 -> abc (1,000)

        // Message.println(codec.toString());

        ensureEqual(4, codec.size());
        ensureEqual(1, encode(codec, List.of("abc", "abc", "abc")).size());
        ensureEqual(1, encode(codec, List.of("def", "def", "def")).size());
        ensureEqual(2, encode(codec, List.of("ghi", "ghi", "ghi")).size());
        ensureEqual(2, encode(codec, List.of("jkl", "jkl", "jkl", "jkl", "jkl")).size());
    }

    @Test
    public void testFailure()
    {
        var symbols = new Symbols<>(new CountMap<String>()
                .plus("db", Count._1)
                .plus("qts", Count._1)
                .plus("vkl", Count._1)
                .plus("oonpv", Count._1));

        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        test(codec, List.of("db", "qts", "vkl", "qts", "oonpv", "db", "db", "db"));
        test(codec, List.of("db", "oonpv", "vkl", "qts", "oonpv", "db"));
        test(codec, List.of("db", "db", "vkl", "qts", "vkl", "db", "db", "db"));
        test(codec, List.of("db", "qts", "db", "qts", "oonpv", "db"));
    }

    @Test
    public void testFailure2()
    {
        var symbols = new Symbols<>(new CountMap<String>()
                .plus("stxq", Count.count(803))
                .plus("sshtp", Count.count(1_366))
                .plus("i", Count.count(7_088))
                .plus("zvgupm", Count.count(7_486)));

        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        //        [HuffmanCodec size = 4, bits = 3]:
        //        1. 100 -> stxq (803)
        //        2. 101 -> sshtp (1,366)
        //        3. 11 -> i (7,088)
        //        4. 0 -> zvgupm (7,486)

        // Message.println(codec.toString());

        test(codec, List.of("stxq", "sshtp", "sshtp", "i", "zvgupm", "zvgupm", "zvgupm", "stxq"));
    }

    @Test
    public void testFailure3()
    {
        var symbols = new Symbols<>(new CountMap<String>()
                .plus("a", Count._1)
                .plus("b", Count._1)
                .plus("c", Count._1)
                .plus("d", Count._1)
                .plus("end", Count._1));

        var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

        //        [HuffmanCodec size = 5, bits = 3]:
        //        1. 00 -> a (1)
        //        2. 111 -> b (1)
        //        3. 10 -> c (1)
        //        4. 110 -> d (1)
        //        5. 01 -> end (1)

        // Message.println(codec.toString());

        var values = List.of("a", "c", "a", "d", "a", "a", "a", "a", "end");
        var data = encode(codec, values);

        // Message.println("data:\n$", data.toBinaryString());

        // a  c  a  d      a  a  a  a     end
        // 00 10 00 11 | 0 00 00 00 0 | 0 01

        testDecode(codec, data, values);
    }

    @Test
    public void testRandom()
    {
        random().seed(485465258L);
        var progress = BroadcastingProgressReporter.progressReporter();

        // For each random codec
        _10.forEachInteger(codecNumber ->
        {
            var symbols = randomStringSymbols(2, 200, 1, 8);
            var codec = HuffmanCodec.huffmanCodec(symbols, Maximum._8);

            // test it a few times
            _10.forEachInteger(testNumber ->
            {
                // by creating a random list of values to encode from the coded symbols in the codec
                var values = new ArrayList<String>();
                var choices = new ArrayList<>(codec.codedSymbols());
                random().rangeInclusive(2, 100).loop(() -> values.add(choices.get(random().randomIntExclusive(0, choices.size() - 1)).value()));

                // and trying to encode and decode those values
                test(codec, values);

                progress.next();
            });
        });
    }
}
