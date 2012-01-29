package com.mpdeimos.tensation.model;

import com.mpdeimos.tensation.impex.serialize.Export;
import com.mpdeimos.tensation.util.Gfx;
import com.mpdeimos.tensation.util.StringUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Container class for appearance settings.
 * 
 * @author mpdeimos
 */
public class AppearanceContainer
{
	/** The color of this object. */
	@Export
	private Color color;

	/** The line width of this object. */
	@Export
	private Integer lineWidth;

	/** The line style of this object. */
	@Export
	private ELineStyle lineStyle;

	/**
	 * @return the color of the container.
	 */
	public Color getColor()
	{
		return this.color;
	}

	/**
	 * Sets the color of this container.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/** @return the line width of this container. */
	public Integer getLineWidth()
	{
		return this.lineWidth;
	}

	/** Sets the line width of this container. */
	public void setLineWidth(Integer lineWidth)
	{
		this.lineWidth = lineWidth;
	}

	/** @return the line style of this container. */
	public ELineStyle getLineStyle()
	{
		return this.lineStyle;
	}

	/** Sets the line style of this container. */
	public void setLineStyle(ELineStyle lineStyle)
	{
		this.lineStyle = lineStyle;
	}

	/**
	 * Applies the appearance settings to the given Graphics object.
	 */
	public void applyAppearance(Graphics2D gfx)
	{
		if (this.color != null)
			gfx.setColor(this.color);

		int width = 1;
		int multiplier = 0;
		if (this.lineWidth != null)
		{
			width = this.lineWidth;
		}
		if (this.lineStyle != null)
		{
			multiplier = this.lineStyle.getPatternMultiplier();
		}
		gfx.setStroke(Gfx.createStroke(width, multiplier));
	}

	/** Interface for objects holding an appearance container. */
	public interface IAppearanceHolder
	{
		/** @return the appearance container. */
		public AppearanceContainer getAppearanceContainer();
	}

	public HashMap<String, Object> getValues()
	{
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (Field field : this.getClass().getDeclaredFields())
		{
			Export annotation = field.getAnnotation(Export.class);
			if (annotation != null)
			{
				String name = annotation.name();
				if (StringUtil.isNullOrEmpty(name))
					name = field.getName();
				try
				{
					Object object = field.get(this);
					if (object != null || annotation.nulls())
						map.put(name, object);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		return map;
	}

	public void setValues(HashMap<String, Object> map)
	{
		for (Field field : this.getClass().getDeclaredFields())
		{
			Export annotation = field.getAnnotation(Export.class);
			if (annotation != null)
			{
				String name = annotation.name();
				if (StringUtil.isNullOrEmpty(name))
					name = field.getName();
				try
				{
					field.set(this, map.get(name));
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
