package com.mpdeimos.tensation.util;

import java.util.HashMap;

/**
 * Utility Class for Hash Maps
 * 
 * @author mpdeimos
 * 
 */
public class MapUtil
{
	/** Clones a map. */
	@SuppressWarnings("unchecked")
	public static <A, B> HashMap<A, B> clone(HashMap<A, B> map)
	{
		return (HashMap<A, B>) map.clone();
	}
}
