package com.mpdeimos.tensation.impex.export;

import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.StringUtil;

import java.lang.reflect.Field;
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
	private final IExportable exportable;

	/** Constructor. */
	public ExportHandler(IExportable exportable)
	{
		this.exportable = exportable;

	}

	@Override
	public void setValues(HashMap<String, Object> map)
	{
		for (Field field : this.exportable.getClass().getDeclaredFields())
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
					field.set(this.exportable, map.get(name));
				}
				catch (IllegalAccessException e)
				{
					Log.e(this, e); // shouldn't happen anyways
				}
				field.setAccessible(false);
			}
		}
	}

	@Override
	public HashMap<String, Object> getValues()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Field field : this.exportable.getClass().getDeclaredFields())
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
