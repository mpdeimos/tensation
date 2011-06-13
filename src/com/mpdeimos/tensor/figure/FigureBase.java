package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

import com.mpdeimos.tensor.editpart.IEditPart;

/**
 * Abstract base class for figures. 
 * 
 * @author mpdeimos
 *
 */
public abstract class FigureBase implements IFigure {
	
	/** The shapes to be drawn on the canvas */
	protected List<Shape> shapes;
	
	/** associated EditPart */
	protected final IEditPart editPart;
	
	/** Constructor. */
	FigureBase(IEditPart editPart) {
		this.editPart = editPart;
	}

	@Override
	public void draw(Graphics2D gfx) {
		for (Shape shape : shapes)
		{
			gfx.draw(shape);
		}
	}

}
