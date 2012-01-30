package com.mpdeimos.tensation.impex.serialize;

import java.util.HashMap;

/**
 * Factory for serializing objects.
 * 
 * @author mpdeimos
 */
public class Serializer
{
	/** class serializers. */
	static private final HashMap<Class<?>, IObjectSerializer<?>> serializers = new HashMap<Class<?>, IObjectSerializer<?>>();

	/** generic serializers. */
	static private final HashMap<Class<?>, IObjectSerializer<?>> genericSerializers = new HashMap<Class<?>, IObjectSerializer<?>>();

	/** Static Constructor. */
	static
	{
		registerSerializers(
				new ColorSerializer(),
				new IntegerSerializer());
		registerGenericSerializers(new EnumSerializer());
	}

	/** @return the Serialized representation of an object. */
	@SuppressWarnings("unchecked")
	public static <T> Serialized serialize(T o)
	{
		IObjectSerializer<T> serializer = (IObjectSerializer<T>) serializers.get(o.getClass());
		if (serializer == null)
		{
			serializer = (IObjectSerializer<T>) getGenericSerializer(o.getClass());
		}
		if (serializer == null)
		{
			throw null;
		}
		return new Serialized(serializer.serialize(o), o.getClass());
	}

	/** @return An object representing by the Serialized. */
	public static Object deserialize(Serialized s)
	{
		IObjectSerializer<?> serializer = serializers.get(s.getType());
		if (serializer == null)
		{
			serializer = getGenericSerializer(s.getType());
		}
		if (serializer == null)
		{
			throw null;
		}
		return serializer.deserialize(s.getData(), s.getType());
	}

	/** @return the generic serializer that can handle a specific type. */
	private static IObjectSerializer<?> getGenericSerializer(Class<?> type)
	{
		for (Class<?> clazz : genericSerializers.keySet())
		{
			if (clazz.isAssignableFrom(type))
			{
				return genericSerializers.get(clazz);
			}
		}
		return null;
	}

	/** registers class serializers. */
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

	/** registers generic serializers. */
	private static void registerGenericSerializers(
			IObjectSerializer<?>... objectSerializers)
	{
		for (IObjectSerializer<?> objectSerializer : objectSerializers)
		{
			genericSerializers.put(
					objectSerializer.getHandledClass(),
					objectSerializer);
		}
	}
}
