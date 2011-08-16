/**
 * 
 */
package com.mpdeimos.tensor.util;

/**
 * Utility class for common string functions.
 * 
 * @author mpdeimos
 * 
 */
public class StringUtil
{
	/** string representation of the newline character */
	public static final String NEWLINE = "\n"; //$NON-NLS-1$

	/** string representation of the tabulator character */
	public static final String TABULATOR = "\t"; //$NON-NLS-1$

	/** an empty string. */
	public static final String EMPTY = ""; //$NON-NLS-1$

	/** checks whether a string is null or empty */
	public static boolean isNullOrEmpty(String s)
	{
		return s == null || s.isEmpty();
	}
}
