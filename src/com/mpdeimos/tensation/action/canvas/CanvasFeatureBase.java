package com.mpdeimos.tensation.action.canvas;

import com.mpdeimos.tensation.editpart.feature.EditPartFeatureBase;
import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.ICanvasFeatureContract;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Base class for EditPart Features.
 * 
 * @author mpdeimos
 */
public abstract class CanvasFeatureBase<T extends IFeatureEditPart, F extends ICanvasFeatureContract>
		extends EditPartFeatureBase<T, F> implements ICanvasFeatureContract
{
	/** Constructor. */
	public CanvasFeatureBase(T editPart)
	{
		super(editPart);
	}

	@Override
	public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseClicked(ICanvasAction action, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
	{
		return false;
	}

	@Override
	public boolean doOnKeyPressed(ICanvasAction action, KeyEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnKeyReleased(ICanvasAction action, KeyEvent e)
	{
		return false;
	}

	@Override
	public void doOnEditPartSelected(boolean selected)
	{
		// nothing
	}
}
