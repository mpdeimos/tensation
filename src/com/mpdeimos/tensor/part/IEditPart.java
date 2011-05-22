package com.mpdeimos.tensor.part;

import java.awt.Graphics2D;

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

}
