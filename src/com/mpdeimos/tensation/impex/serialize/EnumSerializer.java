package com.mpdeimos.tensation.impex.serialize;

import com.mpdeimos.tensation.util.Log;

import java.lang.reflect.Method;

/**
 * Generic Serializer for Enums.
 * 
 * @author mpdeimos
 */
@SuppressWarnings("rawtypes")
public class EnumSerializer implements IObjectSerializer<Enum>
{
	@Override
	public Class<Enum> getHandledClass()
	{
		return Enum.class;
	}

	@Override
	public String serialize(Enum t)
	{
		return t.name();
	}

	@Override
	public Enum deserialize(String s, Class<?> type)
	{
		try
		{
			Method method = type.getDeclaredMethod("valueOf", String.class); //$NON-NLS-1$
			return (Enum) method.invoke(type, s);
		}
		catch (Exception e)
		{
			Log.e(this, e);
		}
		return null;
	}

}
