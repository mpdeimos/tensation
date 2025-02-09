package com.mpdeimos.tensation.util;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Utility functions for swing layouting.
 * 
 * @author mpdeimos
 * 
 */
public class LayoutUtil
{

	/** Sets the minimum, maximum and preferred height value of this component. */
	public static Component setHeight(Component comp, int height)
	{
		Dimension d = comp.getMaximumSize();
		d.height = height;
		comp.setMaximumSize(d);

		d = comp.getMinimumSize();
		d.height = height;
		comp.setMinimumSize(d);

		d = comp.getPreferredSize();
		d.height = height;
		comp.setPreferredSize(d);

		return comp;
	}

	/** Sets the minimum, maximum and preferred width value of this component. */
	public static Component setWidth(Component comp, int width)
	{
		Dimension d = comp.getMaximumSize();
		d.width = width;
		comp.setMaximumSize(d);

		d = comp.getMinimumSize();
		d.width = width;
		comp.setMinimumSize(d);

		d = comp.getPreferredSize();
		d.width = width;
		comp.setPreferredSize(d);

		return comp;
	}

	/**
	 * Sets the minimum, maximum and preferred width/height value of this
	 * component.
	 */
	public static Component setSize(Component comp, int width, int height)
	{
		setWidth(comp, width);
		setHeight(comp, height);

		return comp;
	}

}
