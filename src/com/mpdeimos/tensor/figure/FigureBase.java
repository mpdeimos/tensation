package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
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
	
	/** The features to be drawn on the canvas */
	protected final List<Feature> features;
	
	/** associated EditPart */
	protected final IEditPart editPart;
	
	/** Constructor. */
	FigureBase(IEditPart editPart) {
		this.editPart = editPart;
		features = new ArrayList<Feature>(0);
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
		features.clear();
	}

	@Override
	public void draw(Graphics2D gfx) {
		for (Feature feature : features)
		{
			feature.draw(gfx);
		}
	}

}
