package com.mpdeimos.tensation.util;

/**
 * Miscellaneous util functions
 * 
 * @author mpdeimos
 * 
 */
public class MiscUtil
{
	/**
	 * Compares
	 * 
	 * @param target
	 *            Target object for comparison
	 * @param comparison
	 *            List of objects to compare
	 * @return true if the target object matches one of the comparison objects.
	 */
	public static boolean isOneOf(Object target, Object... comparison)
	{
		for (Object compare : comparison)
		{
			if (target == compare)
				return true;
		}
		return false;
	}
}
