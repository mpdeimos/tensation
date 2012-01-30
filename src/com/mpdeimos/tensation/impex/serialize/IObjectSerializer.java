package com.mpdeimos.tensation.impex.serialize;

/**
 * Interface for serializing and deserializing objects.
 * 
 * @author mpdeimos
 */
public interface IObjectSerializer<T>
{
	/** @return the class name of the handled class. */
	Class<T> getHandledClass();

	/** @return the object as serialized string. */
	String serialize(T t);

	/** @return the deserialized object from a string. */
	T deserialize(String s, Class<?> type);
}
