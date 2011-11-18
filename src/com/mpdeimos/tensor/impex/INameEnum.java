package com.mpdeimos.tensor.impex;

import java.util.Locale;

/**
 * Interface for xml nameing definition enums.
 * 
 * @author mpdeimos
 * 
 */
public interface INameEnum
{
	/** @return the name of an xml attribute or tag. */
	public String $(Object... format);

	/** @ignore */
	public class Impl
	{
		/** reference implementation with formatting. */
		protected static String $(String name, Object... format)
		{
			if (format != null && format.length > 0)
				return String.format(Locale.ENGLISH, name, format);

			return name;
		}
	}
}
