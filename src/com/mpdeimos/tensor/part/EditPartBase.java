package com.mpdeimos.tensor.part;

import java.awt.Graphics2D;

import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * abstract base class for EditParts.
 * 
 * @author mpdeimos
 *
 */
public abstract class EditPartBase implements IEditPart {

	/** the data model object linked to this EditPart */
	private IModelData model;
	
	/** the figure for drawing this object */
	private IFigure figure;

	/**
	 * Constructor.
	 */
	public EditPartBase(IModelData modelData) {
		this.model = modelData;
	}
	
	/**
	 * Sets the figure of this EditPart
	 */
	protected void setFigure(IFigure figure)
	{
		this.figure = figure;
		
	}
	
	@Override
	public IModelData getModelData() {
		return model;
	}
	
	@Override
	public void setModelData(IModelData model) {
		this.model = model;
	}
	
	@Override
	public void draw(Graphics2D gfx) {
		getFigure().draw(gfx);
	}
	
	/** @return the figure responsible for drawing this object */
	protected IFigure getFigure()
	{
		return this.figure;
	}
	
}
