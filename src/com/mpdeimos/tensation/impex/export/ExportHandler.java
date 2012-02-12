package com.mpdeimos.tensation.impex.export;

import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Exports the values of an object via
 * 
 * @author mpdeimos
 * 
 */
public class ExportHandler implements IExportable
{
	/** the underlying exportable. */
	private final Object exportable;

	/** Constructor. */
	public ExportHandler(Object exportable)
	{
		this.exportable = exportable;

	}

	@Override
	public void setValues(HashMap<String, Object> map)
	{
		for (Field field : getAllFields())
		{
			Export annotation = field.getAnnotation(Export.class);
			if (annotation != null)
			{
				String name = annotation.name();
				if (StringUtil.isNullOrEmpty(name))
					name = field.getName();
				try
				{
					Object value = map.get(name);
					if (value != null || annotation.nulls())
					{
						String setter = annotation.set();
						if (StringUtil.isNullOrEmpty(setter))
						{
							field.setAccessible(true);
							field.set(this.exportable, value);
							field.setAccessible(false);
						}
						else
						{
							for (Method method : this.exportable.getClass().getMethods())
							{
								if (method.getName().equals(setter))
								{
									try
									{
										method.invoke(this.exportable, value);
										break;
									}
									catch (IllegalArgumentException e)
									{
										// swallow
									}
									catch (InvocationTargetException e)
									{
										// swallow
									}
								}
							}
						}
					}
				}
				catch (IllegalAccessException e)
				{
					Log.e(this, e); // shouldn't happen anyways
				}
			}
		}
	}

	/** @return all fields of this object. */
	private ArrayList<Field> getAllFields()
	{
		ArrayList<Field> fields = new ArrayList<Field>();

		Class<?> clazz = this.exportable.getClass();
		while (clazz != null)
		{
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	@Override
	public HashMap<String, Object> getValues()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Field field : getAllFields())
		{
			Export annotation = field.getAnnotation(Export.class);
			if (annotation != null)
			{
				String name = annotation.name();
				if (StringUtil.isNullOrEmpty(name))
					name = field.getName();
				try
				{
					field.setAccessible(true);
					Object object = field.get(this.exportable);
					if (object != null || annotation.nulls())
						map.put(name, object);
				}
				catch (IllegalAccessException e)
				{
					Log.e(this, e); // shouldn't happen anyways
				}
				field.setAccessible(false);
			}
		}

		return map;
	}
}
