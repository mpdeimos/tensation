package com.mpdeimos.tensor.editpart;

import java.awt.Graphics2D;
import java.awt.Point;

import com.mpdeimos.tensor.model.IModelData;

/**
 * Controller objects that link views with model data.
 * 
 * @author mpdeimos
 *
 */
public interface IEditPart {

	/** draws the object to the provided canvas */
	public void draw(Graphics2D gfx);
	
	/** sets the data model object */
	public void setModelData(IModelData model);
	
	/** @return the linked data model object */
	public IModelData getModelData();

	/** performs a check whether the mouse hovers this EditPart */
	public void isMouseOver(Point point);

}
