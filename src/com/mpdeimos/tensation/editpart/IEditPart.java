package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.model.IModelData;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Controller objects that link views with model data.
 * 
 * @author mpdeimos
 * 
 */
public interface IEditPart
{

	/** draws the object to the provided canvas */
	public void draw(Graphics2D gfx);

	/** @return the linked data model object */
	public IModelData getModel();

	/** @return the linked drawing object. */
	public IFigure getFigure();

	/** performs a check whether the Rectangle intersects the EditPart. */
	public boolean intersects(Rectangle rect);

	/** @return the bounding rectangle */
	public Rectangle getBoundingRectangle();

	/** sets the selection status of the EditPart. */
	public void setSelected(boolean selected);

	/** sets the highlight status of the EditPart. */
	public void setHighlighted(boolean highlighted);

	/** @return the highlight status of this EditPart. */
	public boolean isHighlighted();

	/** @return the selected status of this EditPart. */
	public boolean isSelected();
}
