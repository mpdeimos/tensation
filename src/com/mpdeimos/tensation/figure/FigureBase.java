package com.mpdeimos.tensation.figure;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.util.ImmutableList;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract base class for figures.
 * 
 * @author mpdeimos
 * 
 */
public abstract class FigureBase implements IFigure
{

	/** The shapes to be drawn on the canvas */
	protected final List<ShapePack> shapePacks;

	/** associated EditPart */
	protected final IEditPart editPart;

	/** Flag for determining needed shape updates. */
	private boolean scheduleShapeUpdate;

	/** Constructor. */
	FigureBase(IEditPart editPart)
	{
		this.editPart = editPart;
		this.shapePacks = new ArrayList<ShapePack>(0);
		initBeforeFirstUpdateShapes();
		redraw();
	}

	/** Called before the first shape update cycle. */
	protected void initBeforeFirstUpdateShapes()
	{
		// nothing
	}

	@Override
	public void redraw()
	{
		this.scheduleShapeUpdate = true;
	}

	/** Updates the shape Objects. */
	protected void updateShapes()
	{
		this.shapePacks.clear();
		this.scheduleShapeUpdate = false;
	}

	@Override
	public void draw(Graphics2D gfx)
	{
		if (this.scheduleShapeUpdate)
			updateShapes();

		for (ShapePack pack : this.shapePacks)
		{
			pack.draw(gfx);
		}
	}

	@Override
	public boolean containsPoints(Point point)
	{

		for (ShapePack feature : this.shapePacks)
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
	public Rectangle getBoundingRectangle()
	{
		Rectangle r = null;
		for (ShapePack feature : this.shapePacks)
		{
			for (Shape shape : feature.getShapes())
			{
				if (r == null)
					r = shape.getBounds();
				else
					r.createUnion(shape.getBounds());
			}
		}
		return r;
	}

	@Override
	public boolean intersects(Rectangle rect)
	{

		for (ShapePack pack : this.shapePacks)
		{
			for (Shape shape : pack.getShapes())
			{
				if (shape.intersects(rect))
					return true;
			}
		}

		return false;
	}

	@Override
	public Element getSvgNode(
			Document parent,
			HashMap<String, Element> definitions)
	{
		return null;
	}

	/** @return the shape pack of a figure. */
	public ImmutableList<ShapePack> getShapePacks()
	{
		return new ImmutableList<ShapePack>(this.shapePacks);
	}
}
