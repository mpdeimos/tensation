package com.mpdeimos.tensor.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;

/**
 * Reflections utility class.
 * 
 * @author mpdeimos
 */
public class Reflections
{
	/**
	 * @return the generic type class of a class.
	 */
	public static Class<?> getGenericType(
			Object o,
			Class<?> declaringClass,
			int idx)
	{
		Class<?> clazz = o.getClass();
		LinkedList<Class<?>> clazzStack = new LinkedList<Class<?>>();
		while (!clazz.getSuperclass().equals(declaringClass))
		{
			clazzStack.push(clazz);
			clazz = clazz.getSuperclass();
		}

		while (clazz != null)
		{
			ParameterizedType ptype = (ParameterizedType) clazz.getGenericSuperclass();

			Type type = ptype.getActualTypeArguments()[idx];

			if (type instanceof Class<?>)
			{
				return (Class<?>) type;
			}

			if (type instanceof ParameterizedType)
			{
				return (Class<?>) ((ParameterizedType) type).getRawType();
			}

			TypeVariable<?> tvi = (TypeVariable<?>) type;
			TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
			for (idx = 0; idx < typeParameters.length; idx++)
			{
				TypeVariable<?> ctv = typeParameters[idx];
				if (ctv.getName().equals(tvi.getName()))
				{
					break;
				}
			}
			clazz = clazzStack.pop();
		}

		return null;
	}
}
