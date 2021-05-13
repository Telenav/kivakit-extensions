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
public class RefreshableComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable
{
    private final List<E> items;

    private E selected;

    private final ReadWriteLock lock = new ReadWriteLock();

    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public RefreshableComboBoxModel()
    {
        items = new ArrayList<>();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with an array of objects.
     *
     * @param items an array of Object objects
     */
    public RefreshableComboBoxModel(final E[] items)
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
    public RefreshableComboBoxModel(final ArrayList<E> v)
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

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void addElement(final E anObject)
    {
        lock.write(() ->
        {
            items.add(anObject);
            fireIntervalAdded(this, items.size() - 1, items.size() - 1);
            if (items.size() == 1 && selected == null && anObject != null)
            {
                setSelectedItem(anObject);
            }
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

    // implements javax.swing.ComboBoxModel
    @Override
    public Object getSelectedItem()
    {
        return selected;
    }

    // implements javax.swing.ListModel
    @Override
    public int getSize()
    {
        return items.size();
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void insertElementAt(final E anObject, final int index)
    {
        lock.write(() ->
        {
            items.set(index, anObject);
            fireIntervalAdded(this, index, index);
        });
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

    // implements javax.swing.MutableComboBoxModel
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void removeElement(final Object anObject)
    {
        lock.write(() ->
        {
            final int index = items.indexOf(anObject);
            if (index != -1)
            {
                removeElementAt(index);
            }
        });
    }

    // implements javax.swing.MutableComboBoxModel
    @Override
    public void removeElementAt(final int index)
    {
        lock.write(() ->
        {
            if (getElementAt(index) == selected)
            {
                if (index == 0)
                {
                    setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
                }
                else
                {
                    setSelectedItem(getElementAt(index - 1));
                }
            }

            items.remove(index);

            fireIntervalRemoved(this, index, index);
        });
    }

    /**
     * Set the value of the selected item. The selected item may be null.
     *
     * @param anObject The combo box value or null for no selection.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setSelectedItem(final Object anObject)
    {
        lock.write(() ->
        {
            if ((selected != null && !selected.equals(anObject)) ||
                    selected == null && anObject != null)
            {
                selected = (E) anObject;
                fireContentsChanged(this, -1, -1);
            }
        });
    }
}

