package com.mpdeimos.tensation.util;

import java.util.ArrayList;
import java.util.List;

/**
 * List utility.
 * 
 * @author mpdeimos
 */
public class ListUtil
{
	/** Filters items of a list by class type. */
	@SuppressWarnings("unchecked")
	public static <T, F extends T> List<F> filterByClass(
			List<T> list,
			Class<F> filter)
	{
		ArrayList<F> r = new ArrayList<F>();
		for (T t : list)
		{
			if (filter.isAssignableFrom(t.getClass()))
			{
				r.add((F) t);
			}
		}
		return r;
	}
}
