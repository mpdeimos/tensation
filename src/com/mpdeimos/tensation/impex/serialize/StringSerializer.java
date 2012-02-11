package com.mpdeimos.tensation.impex.serialize;

/**
 * Serializer Class for Strings.
 * 
 * @author mpdeimos
 * 
 */
public class StringSerializer implements IObjectSerializer<String>
{
	@Override
	public Class<String> getHandledClass()
	{
		return String.class;
	}

	@Override
	public String serialize(String t)
	{
		return t;
	}

	@Override
	public String deserialize(String s, Class<?> type)
	{
		return s;
	}
}
