package com.mpdeimos.tensor.util;

/**
 * Utility class for wrapping an object.
 * 
 * @author mpdeimos
 * 
 */
public class Wrapper<T>
{
	/** the wrapper's fill. */
	private T inside = null;

	/** Constructor. */
	public Wrapper(T inside)
	{
		this.inside = inside;
	}

	/** @return the inside of the wrapper. */
	public T get()
	{
		return this.inside;
	}

	/** set the inside of this wrapper. */
	public void set(T inside)
	{
		this.inside = inside;
	}
}
