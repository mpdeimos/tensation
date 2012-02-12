package com.mpdeimos.tensation.impex.serialize;

import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.Reflections;

/**
 * Serialized data object.
 * 
 * @author mpdeimos
 * 
 */
public class Serialized
{
	/** the serialized data */
	private final String data;

	/** the original type of the object. */
	private final Class<?> type;

	/** COnstructor. */
	public Serialized(String data, Class<?> type)
	{
		this.data = data;
		this.type = type;
	}

	/** Constructor. */
	public Serialized(String data, String type)
	{
		this.data = data;
		try
		{
			this.type = Reflections.getClassForName(type);
		}
		catch (ClassNotFoundException e)
		{
			Log.e(this, e);
			throw new IllegalArgumentException();
		}
	}

	/** @return the serialized data. */
	public String getData()
	{
		return this.data;
	}

	/** @return the class of the original type. */
	public Class<?> getType()
	{
		return this.type;
	}

	/** @return the class name of the original type. */
	public String getTypeName()
	{
		return this.type.getCanonicalName();
	}
}
