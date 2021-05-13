package com.telenav.kivakit.ui.desktop.model;

import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jonathanl (shibo)
 */
public class RefreshableListModel<E> extends AbstractListModel<E> implements Serializable
{
    private final List<E> items;

    private E selected;

    private final ReadWriteLock lock = new ReadWriteLock();

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public RefreshableListModel()
    {
        items = new ArrayList<>();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with an array of objects.
     *
     * @param items an array of Object objects
     */
    public RefreshableListModel(final E[] items)
    {
        this.items = new ArrayList<>(items.length);

        int i;
        final int c;
        for (i = 0, c = items.length; i < c; i++)
        {
            this.items.add(items[i]);
        }

        if (getSize() > 0)
        {
            selected = getElementAt(0);
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with a vector.
     *
     * @param v a Vector object ...
     */
    public RefreshableListModel(final ArrayList<E> v)
    {
        items = v;

        if (getSize() > 0)
        {
            selected = getElementAt(0);
        }
    }

    /**
     * Adds all of the elements present in the collection.
     *
     * @param c the collection which contains the elements to add
     * @throws NullPointerException if {@code c} is null
     */
    public void addAll(final Collection<? extends E> c)
    {
        lock.write(() ->
        {
            if (c.isEmpty())
            {
                return;
            }

            final int startIndex = getSize();

            items.addAll(c);
            fireIntervalAdded(this, startIndex, getSize() - 1);
        });
    }

    // implements javax.swing.ComboBoxModel

    /**
     * Adds all of the elements present in the collection, starting from the specified index.
     *
     * @param index index at which to insert the first element from the specified collection
     * @param c the collection which contains the elements to add
     * @throws ArrayIndexOutOfBoundsException if {@code index} does not fall within the range of number of elements
     * currently held
     * @throws NullPointerException if {@code c} is null
     */
    public void addAll(final int index, final Collection<? extends E> c)
    {
        lock.write(() ->
        {
            if (index < 0 || index > getSize())
            {
                throw new ArrayIndexOutOfBoundsException("index out of range: " +
                        index);
            }

            if (c.isEmpty())
            {
                return;
            }

            items.addAll(index, c);
            fireIntervalAdded(this, index, index + c.size() - 1);
        });
    }

    // implements javax.swing.ListModel
    @Override
    public E getElementAt(final int index)
    {
        return lock.read(() ->
        {
            if (index >= 0 && index < items.size())
            {
                return items.get(index);
            }
            else
            {
                return null;
            }
        });
    }

    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject the object to return the index of
     * @return an int representing the index position, where 0 is the first position
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public int getIndexOf(final Object anObject)
    {
        return lock.read(() -> items.indexOf(anObject));
    }

    // implements javax.swing.ListModel
    @Override
    public int getSize()
    {
        return items.size();
    }

    /**
     * Updates model without changing selection, if possible
     */
    public void refresh(final List<E> items, final E defaultSelection)
    {
        lock.write(() ->
        {
            if (!items.isEmpty())
            {
                final var selectedBefore = selected;
                removeAllElements();
                addAll(items);
                if (this.items.contains(selectedBefore))
                {
                    selected = selectedBefore;
                }
                else
                {
                    selected = defaultSelection;
                }
            }
        });
    }

    /**
     * Empties the list.
     */
    public void removeAllElements()
    {
        lock.write(() ->
        {
            if (items.size() > 0)
            {
                final int firstIndex = 0;
                final int lastIndex = items.size() - 1;
                items.clear();
                selected = null;
                fireIntervalRemoved(this, firstIndex, lastIndex);
            }
            else
            {
                selected = null;
            }
        });
    }
}

