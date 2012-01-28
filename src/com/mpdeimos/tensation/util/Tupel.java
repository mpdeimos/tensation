package com.mpdeimos.tensation.util;

/**
 * Generic Tupel implementation.
 * 
 * @author mpdeimos
 */
public class Tupel<F, L>
{
	/** the first tupel item. */
	public F $1;

	/** the last tupel item. */
	public L $2;

	/** Constructor. */
	public Tupel(F first, L last)
	{
		this.$1 = first;
		this.$2 = last;
	}

	/** Constructor. */
	public Tupel()
	{
		// nothing
	}

	/** Constructor. */
	public Tupel(Tupel<F, L> t)
	{
		this(t.$1, t.$2);
	}
	//
	// @Override
	// public boolean equals(Object obj)
	// {
	// if (obj == null)
	// return false;
	//
	// if (!(obj instanceof Tupel))
	// return false;
	//
	// Tupel<?,?> tupel = (Tupel<?,?>) obj;
	//
	// if (this.$1 != null)
	// {
	//
	// }
	// }
}
