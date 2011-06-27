package com.mpdeimos.tensor.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Immutable List implementation. raises illegal state exception on each write access.
 * 
 * @author mpdeimos
 * 
 */
public class ImmutableList<T> implements List<T>
{
	/** The list backing up this ImmutalList */
	private List<T> backend;

	/** Constructs an Immutal list using a given list as backend */
	public ImmutableList(List<T> backend)
	{
		this.backend = backend;
	}

	@Override
	public boolean add(T arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public void add(int arg0, T arg1)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1)
	{
		throw new IllegalStateException();
	}

	@Override
	public void clear()
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean contains(Object o)
	{
		return this.backend.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return this.backend.containsAll(c);
	}

	@Override
	public T get(int pos)
	{
		return this.backend.get(pos);
	}

	@Override
	public int indexOf(Object o)
	{
		return this.backend.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return this.backend.isEmpty();
	}

	@Override
	public Iterator<T> iterator()
	{
		return this.backend.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.backend.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return this.backend.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return this.backend.listIterator(index);
	}

	@Override
	public boolean remove(Object arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public T remove(int arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		throw new IllegalStateException();
	}

	@Override
	public T set(int arg0, T arg1)
	{
		throw new IllegalStateException();
	}

	@Override
	public int size()
	{
		return this.backend.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return new ImmutableList<T>(this.backend.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray()
	{
		return this.backend.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.backend.toArray(a);
	}

}
