package com.mpdeimos.tensation.model;

import com.mpdeimos.tensation.util.Gfx;

import java.awt.Color;
import java.awt.Graphics2D;

import resources.R;
import resources.R.string;

/**
 * Container class for appearance settings.
 * 
 * @author mpdeimos
 */
public class AppearanceContainer
{
	/** The color of this object. */
	private Color color;

	/** The line width of this object. */
	private Integer lineWidth;

	/** The line style of this object. */
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
			multiplier = this.lineStyle.patternMultiplier;
		}
		gfx.setStroke(Gfx.createStroke(width, multiplier));
	}

	/** Interface for objects holding an appearance container. */
	public interface IAppearanceHolder
	{
		/** @return the appearance container. */
		public AppearanceContainer getAppearanceContainer();
	}

	/** Line style enumeration. */
	public enum ELineStyle
	{
		/** Solid stroke */
		SOLID(0, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_SOLID),
		/** Dotted stroke */
		DOTTED(1, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_DOTTED),
		/** Dashed stroke */
		DASHED(3, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_DASHED);

		/** The style display name. */
		private final string name;

		/** the pattern multiplier. */
		private final int patternMultiplier;

		/** Constructor. */
		private ELineStyle(int patternMultiplier, R.string name)
		{
			this.patternMultiplier = patternMultiplier;
			this.name = name;
		}

		@Override
		public String toString()
		{
			return this.name.toString();
		}
	}
}
