package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
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
	protected final List<ShapePack> shapePacks;
	
	/** associated EditPart */
	protected final IEditPart editPart;
	
	/** Constructor. */
	FigureBase(IEditPart editPart) {
		this.editPart = editPart;
		shapePacks = new ArrayList<ShapePack>(0);
		updateShapes();
	}
	
	@Override
	public void redraw()
	{
		updateShapes();
	}
	
	/** Updates the shape Objects. */
	public void updateShapes()
	{
		shapePacks.clear();
	}

	@Override
	public void draw(Graphics2D gfx) {
		for (ShapePack pack : shapePacks)
		{
			pack.draw(gfx);
		}
	}
	
	@Override
	public boolean containsPoints(Point point) {
		
		for (ShapePack feature : shapePacks)
		{
			for (Shape shape : feature.getShapes())
			{
				if (shape.contains(point))
					return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean intersects(Rectangle rect) {
		
		for (ShapePack pack : shapePacks)
		{
			for (Shape shape : pack.getShapes())
			{
				if (shape.intersects(rect))
					return true;
			}
		}
		
		return false;
	}
}
