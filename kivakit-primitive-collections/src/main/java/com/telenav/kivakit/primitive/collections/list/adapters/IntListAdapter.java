package com.telenav.kivakit.primitive.collections.list.adapters;

import com.telenav.kivakit.kernel.language.values.count.Count;
import com.telenav.kivakit.primitive.collections.PrimitiveCollection;
import com.telenav.kivakit.primitive.collections.list.IntList;

public class IntListAdapter implements IntList
{
    private int cursor;

    @Override
    public boolean add(int value)
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
    public void cursor(int position)
    {
        cursor = position;
    }

    @Override
    public int get(int index)
    {
        return 0;
    }

    @Override
    public boolean hasNullInt()
    {
        return false;
    }

    @Override
    public PrimitiveCollection hasNullInt(boolean has)
    {
        return null;
    }

    @Override
    public boolean isNull(int value)
    {
        return false;
    }

    @Override
    public int nullInt()
    {
        return 0;
    }

    @Override
    public int safeGet(int index)
    {
        return 0;
    }

    @Override
    public long safeGetPrimitive(int index)
    {
        return 0;
    }

    @Override
    public void set(int index, int value)
    {
    }

    @Override
    public void setPrimitive(int index, long value)
    {
    }

    @Override
    public int size()
    {
        return 0;
    }
}
