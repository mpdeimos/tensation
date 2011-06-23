package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.List;

import com.mpdeimos.tensor.util.ImmutableList;

/**
 * Class that knows how to paint a parts of a figure.
 * 
 * @author mpdeimos
 *
 */
public class ShapePack
{
	/** List of Shapes this Feature constists of. */
	private final List<Shape> shapes;
	
	/** The drawing mode for the Shapes. */
	private final EDrawingMode mode;
	
	/** The stroke used for painting. Just useful if in Stroke mode. */
	private Stroke stroke;
	
	/** Drawing mode enumeration. */
	public static enum EDrawingMode
	{
		/** Draws the shapes by filling their interior. */
		FILL,
		/** Draws the outline of the shapes. */
		STROKE,
	}

	/** Constructor. */
	ShapePack(EDrawingMode mode, List<Shape> shapes)
	{
		this.mode = mode;
		this.shapes = shapes;
	}
	
	/** Draws this feature on a Graphics2D Canvas. */
	public void draw(Graphics2D gfx)
	{
		Stroke oldStroke = null;
		if (stroke != null)
		{
			oldStroke = gfx.getStroke();
			gfx.setStroke(stroke);
		}
		
		for (Shape shape : shapes)
		{
			if (EDrawingMode.FILL.equals(mode))
				gfx.fill(shape);
			else if (EDrawingMode.STROKE.equals(mode))
				gfx.draw(shape);
		}
		
		if (oldStroke != null)
			gfx.setStroke(oldStroke);
	}
	
	/** @return the Shapes of this Feature. */
	public ImmutableList<Shape> getShapes()
	{
		return new ImmutableList<Shape>(shapes);
	}

	/** Sets the stroke being used for this Feature. */
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
	
	/** The stroke of this feature. May be null. */
	public Stroke getStroke()
	{
		return this.stroke;
	}
}
