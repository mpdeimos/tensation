package com.mpdeimos.tensor.editpart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import com.mpdeimos.tensor.editpart.feature.IFeature;
import com.mpdeimos.tensor.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.model.IModelChangedListener;
import com.mpdeimos.tensor.model.IModelData;

/**
 * abstract base class for EditParts.
 * 
 * @author mpdeimos
 *
 */
public abstract class EditPartBase implements IFeatureEditPart {

	/** Flag whether the mouse is hovered over this EditPart. */
	private boolean highlighted;
	
	/** the data model object linked to this EditPart */
	private IModelData model;
	
	/** the figure for drawing this object */
	private IFigure figure;
	
	/** The list of Features linked to this EditPart. Default is null. */
	protected List<IFeature> features;

	/**
	 * Constructor.
	 */
	public EditPartBase(IModelData modelData) {
		this.model = modelData;
		
		model.addModelChangedListener(new ModelChangedListener());
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
		Color oldPaint = gfx.getColor();
		if (highlighted)
			gfx.setColor(Color.BLUE);

		getFigure().draw(gfx);
		
		if (highlighted)
			gfx.setColor(oldPaint);
	}
	
	/** @return the figure responsible for drawing this object */
	protected IFigure getFigure()
	{
		return this.figure;
	}

	@Override
	public boolean intersects(Rectangle rect) {
		return this.getFigure().intersects(rect);
	}
	
	@Override
	public Rectangle getBoundingRectangle() {
		return this.getFigure().getBoundingRectangle();
	}
	
	/** private model data change listener */
	private class ModelChangedListener implements IModelChangedListener
	{

		@Override
		public void onModelChanged(IModelData model) {
			figure.redraw();
		}

		@Override
		public void onChildAdded(IModelData child) {
			figure.redraw();
		}

		@Override
		public void onChildRemoved(IModelData child) {
			figure.redraw();
		}
		
	}
	
	@Override
	public List<IFeature> getFeatures() {
		return features;
	}
	
	@Override
	public void setSelected(boolean selected) {
		for (IFeature feature : getFeatures())
		{
			feature.doOnEditPartSelected(selected);
		}
	}
	
	@Override
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
}
