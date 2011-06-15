package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Collection;

/**
 * Class that knows how to paint a parts of a figure.
 * 
 * @author mpdeimos
 *
 */
public class Feature
{
	private final Collection<Shape> shapes;
	private final EDrawingMode mode;
	
	public static enum EDrawingMode
	{
		FILL,
		STROKE,
	}

	/** Constructor. */
	Feature(EDrawingMode mode, Collection<Shape> shapes)
	{
		this.mode = mode;
		this.shapes = shapes;
	}
	
	public void draw(Graphics2D  gfx)
	{
		for (Shape shape : shapes)
		{
			if (EDrawingMode.FILL.equals(mode))
				gfx.fill(shape);
			else if (EDrawingMode.STROKE.equals(mode))
				gfx.draw(shape);
		}
	}
}
