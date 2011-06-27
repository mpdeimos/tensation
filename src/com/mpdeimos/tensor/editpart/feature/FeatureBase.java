package com.mpdeimos.tensor.editpart.feature;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Base class for EditPart Features.
 * 
 * @author mpdeimos
 */
public abstract class FeatureBase<T extends IFeatureEditPart> implements
		IFeature
{
	/** The EditPart for this feature. */
	protected final T editPart;

	/** Constructor. */
	protected FeatureBase(T editPart)
	{
		this.editPart = editPart;
	}

	@Override
	public boolean doOnMouseReleased(DrawingCanvas canvas, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMousePressed(DrawingCanvas canvas, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseDragged(DrawingCanvas canvas, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseClicked(DrawingCanvas canvas, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseMoved(DrawingCanvas canvas, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean drawOverlay(DrawingCanvas canvas, Graphics2D gfx)
	{
		return false;
	}

	@Override
	public void doOnEditPartSelected(boolean selected)
	{
		// nothing
	}
}
