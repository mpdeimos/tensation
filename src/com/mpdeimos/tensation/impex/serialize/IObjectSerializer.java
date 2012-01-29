package com.mpdeimos.tensation.impex.serialize;

public interface IObjectSerializer<T>
{
	Class<T> getHandledClass();

	String serialize(T t);

	T deserialize(String s);
}
