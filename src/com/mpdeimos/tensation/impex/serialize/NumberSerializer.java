package com.mpdeimos.tensation.impex.serialize;

/**
 * Serializer Class for numbers.
 * 
 * @author mpdeimos
 * 
 */
public class NumberSerializer implements IObjectSerializer<Number>
{
	@Override
	public Class<Number> getHandledClass()
	{
		return Number.class;
	}

	@Override
	public String serialize(Number t)
	{
		return t.toString();
	}

	@Override
	public Number deserialize(String s, Class<?> type)
	{
		if (Integer.class.equals(type))
			return Integer.valueOf(s);
		if (Short.class.equals(type))
			return Integer.valueOf(s);
		if (Byte.class.equals(type))
			return Byte.valueOf(s);
		if (Long.class.equals(type))
			return Long.valueOf(s);
		if (Float.class.equals(type))
			return Float.valueOf(s);
		if (Double.class.equals(type))
			return Double.valueOf(s);

		throw new IllegalArgumentException();
	}
}
