package com.mpdeimos.tensation.figure;

import com.mpdeimos.tensation.impex.export.Export;
import com.mpdeimos.tensation.impex.export.ExportHandler;
import com.mpdeimos.tensation.impex.export.IExportable;
import com.mpdeimos.tensation.impex.svg.ESvg;
import com.mpdeimos.tensation.util.Gfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import org.w3c.dom.Element;

/**
 * Container class for appearance settings.
 * 
 * @author mpdeimos
 */
public class AppearanceContainer implements IExportable
{
	/** The color of this object. */
	@Export
	private Color color;

	/** The line width of this object. */
	@Export(name = "line.width")
	private Integer lineWidth;

	/** The line style of this object. */
	@Export(name = "line.style")
	private ELineStyle lineStyle;

	/** export handler for this container. */
	private final ExportHandler exportHandler = new ExportHandler(this);

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

	/**
	 * Applies the appearance settings to the given SVG Node.
	 */
	public void applyAppearance(Element path)
	{
		StringBuilder style = new StringBuilder();

		if (this.color != null)
		{
			String c = ESvg.VALUE_HEX.$(this.color.getRGB());
			style.append(ESvg.VALUE_STYLE_FILL.$(c));
			style.append(ESvg.VALUE_STYLE_STROKE.$(c));
		}
		if (this.lineWidth != null)
		{
			style.append(ESvg.VALUE_STYLE_STROKE_WIDTH.$(this.lineWidth));
		}
		if (this.lineStyle != null)
		{
			int width = 1;
			if (this.lineWidth != null)
				width = this.lineWidth;

			float[] pattern = Gfx.createPatternArray(
					width,
					this.lineStyle.getPatternMultiplier());

			if (pattern != null)
			{
				StringBuilder dash = new StringBuilder();
				for (float f : pattern)
				{
					dash.append(f + " "); //$NON-NLS-1$
				}
				style.append(ESvg.VALUE_STYLE_STROKE_DASHARRAY.$(dash.toString()));
			}
		}
		if (style.length() > 0)
			path.setAttribute(
					ESvg.ATTRIB_STYLE.$(),
					style.toString());

	}

	/** Interface for objects holding an appearance container. */
	public interface IAppearanceHolder
	{
		/** @return the appearance container. */
		public AppearanceContainer getAppearanceContainer();
	}

	@Override
	public HashMap<String, Object> getValues()
	{
		return this.exportHandler.getValues();
	}

	@Override
	public void setValues(HashMap<String, Object> map)
	{
		this.exportHandler.setValues(map);
	}
}
