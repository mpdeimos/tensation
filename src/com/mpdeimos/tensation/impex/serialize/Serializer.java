package com.mpdeimos.tensation.impex.serialize;

import java.util.HashMap;

/**
 * Factory for serializing objects.
 * 
 * @author mpdeimos
 */
public class Serializer
{
	static private final HashMap<Class<?>, IObjectSerializer<?>> serializers = new HashMap<Class<?>, IObjectSerializer<?>>();

	static
	{
		registerSerializers(
				new ColorSerializer(),
				new IntegerSerializer(),
				new LineStyleSerializer());
	}

	public static <T> Serialized serialize(T o)
	{
		@SuppressWarnings("unchecked")
		IObjectSerializer<T> serializer = (IObjectSerializer<T>) serializers.get(o.getClass());
		return new Serialized(serializer.serialize(o), o.getClass());
	}

	public static Object deserialize(Serialized s)
	{
		@SuppressWarnings("unchecked")
		IObjectSerializer<?> serializer = serializers.get(s.getType());
		return serializer.deserialize(s.getData());
	}

	private static void registerSerializers(
			IObjectSerializer<?>... objectSerializers)
	{
		for (IObjectSerializer<?> objectSerializer : objectSerializers)
		{
			serializers.put(
					objectSerializer.getHandledClass(),
					objectSerializer);
		}
	}
}
