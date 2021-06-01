package com.telenav.kivakit.primitive.collections.list.adapters;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.PrimitiveCollection;
import com.telenav.kivakit.primitive.collections.list.IntList;

public class IntListAdapter implements IntList
{
    private int cursor;

    @Override
    public boolean add(final int value)
    {
        return false;
    }

    @Override
    public Count capacity()
    {
        return Count.MAXIMUM;
    }

    @Override
    public int cursor()
    {
        return cursor;
    }

    @Override
    public void cursor(final int position)
    {
        this.cursor = position;
    }

    @Override
    public int get(final int index)
    {
        return 0;
    }

    @Override
    public boolean hasNullInt()
    {
        return false;
    }

    @Override
    public PrimitiveCollection hasNullInt(final boolean has)
    {
        return null;
    }

    @Override
    public boolean isNull(final int value)
    {
        return false;
    }

    @Override
    public int nullInt()
    {
        return 0;
    }

    @Override
    public int safeGet(final int index)
    {
        return 0;
    }

    @Override
    public long safeGetPrimitive(final int index)
    {
        return 0;
    }

    @Override
    public void set(final int index, final int value)
    {
    }

    @Override
    public void setPrimitive(final int index, final long value)
    {
    }

    @Override
    public int size()
    {
        return 0;
    }
}
