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

import com.telenav.kivakit.primitive.collections.array.bits.io.BitReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;

/**
 * A Huffman coding tree, composed of {@link Node} and {@link Leaf} sub-trees. Symbols in the tree are assigned a code
 * with {@link #encode()} and the encoded symbols can be retrieved with {@link #codedSymbols()}. Symbols can be decoded
 * with {@link #decode(BitReader)}, which reads compressed data with {@link BitReader} and walks the tree until if finds
 * the code symbol.
 *
 * @author jonathanl (shibo)
 */
public abstract class Tree<Symbol> implements Comparable<Tree<Symbol>>
{
    /**
     * @return The Huffman tree for the given set of encoded symbols
     */
    public static <Symbol> Tree<Symbol> tree(Symbols<Symbol> symbols)
    {
        // Add a forest of leaves to a priority queue
        var queue = new PriorityQueue<Tree<Symbol>>();
        for (var symbol : symbols.encoded())
        {
            if (symbol.frequency() > 0)
            {
                queue.offer(new Leaf<>(symbol));
            }
        }

        // then build from the bottom to the top until we've got just one tree.
        while (queue.size() > 1)
        {
            var a = queue.poll();
            var b = queue.poll();

            assert a != null;
            assert b != null;

            queue.offer(new Node<>(a, b));
        }

        return queue.poll();
    }

    /** Frequency of this tree or sub-tree */
    private long frequency;

    /**
     * @param frequency The frequency of this sub-tree
     */
    Tree(long frequency)
    {
        this.frequency = frequency;
    }

    protected Tree()
    {
    }

    /**
     * @return The coded symbols in this tree. Codes are only valid once {@link #encode()} has been called.
     */
    public Set<CodedSymbol<Symbol>> codedSymbols()
    {
        var symbols = new HashSet<CodedSymbol<Symbol>>();
        codedSymbols(this, symbols);
        return symbols;
    }

    @Override
    public int compareTo(Tree<Symbol> that)
    {
        return Long.compare(frequency, that.frequency);
    }

    /**
     * @return A symbol decoded from the given input using this Huffman tree
     */
    public abstract CodedSymbol<Symbol> decode(BitReader bits);

    /**
     * Walks the tree, assigning Huffman codes to symbols based on the path through the tree
     *
     * @return A map from symbol to code for the symbols in this tree
     */
    public Map<CodedSymbol<Symbol>, Code> encode()
    {
        var codes = new HashMap<CodedSymbol<Symbol>, Code>();
        codes(codes, this, "");
        for (var symbol : codes.keySet())
        {
            var code = codes.get(symbol);
            ensure(code != null, "Symbol '$' does not have a code", symbol);
            symbol.code(code);
        }
        return codes;
    }

    long frequency()
    {
        return frequency;
    }

    int height()
    {
        return height(this);
    }

    private void codedSymbols(Tree<Symbol> tree, Set<CodedSymbol<Symbol>> symbols)
    {
        if (tree instanceof Leaf)
        {
            var leaf = (Leaf<Symbol>) tree;
            symbols.add(leaf.symbol());
        }
        if (tree instanceof Node)
        {
            var node = (Node<Symbol>) tree;
            codedSymbols(node.left, symbols);
            codedSymbols(node.right, symbols);
        }
    }

    private void codes(Map<CodedSymbol<Symbol>, Code> codes, Tree<Symbol> tree, String prefix)
    {
        if (tree instanceof Leaf)
        {
            var leaf = (Leaf<Symbol>) tree;
            codes.put(leaf.symbol(), new Code(prefix));
        }
        else if (tree instanceof Node)
        {
            var node = (Node<Symbol>) tree;
            codes(codes, node.left, prefix + "0");
            codes(codes, node.right, prefix + "1");
        }
    }

    private int height(Tree<Symbol> tree)
    {
        if (tree instanceof Node)
        {
            var node = (Node<Symbol>) tree;
            return 1 + Math.max(height(node.left), height(node.right));
        }
        return 0;
    }
}
