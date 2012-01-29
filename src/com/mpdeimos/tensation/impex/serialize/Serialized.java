package com.mpdeimos.tensation.impex.serialize;

import com.mpdeimos.tensation.util.Log;

public class Serialized
{
	private final String data;
	private final Class<?> type;

	public Serialized(String data, Class<?> type)
	{
		this.data = data;
		this.type = type;
	}

	public Serialized(String data, String type)
	{
		this.data = data;
		try
		{
			this.type = Class.forName(type);
		}
		catch (ClassNotFoundException e)
		{
			Log.e(this, e);
			throw new IllegalArgumentException();
		}
	}

	public String getData()
	{
		return data;
	}

	public Class<?> getType()
	{
		return type;
	}

	public String getTypeName()
	{
		return type.getCanonicalName();
	}
}
