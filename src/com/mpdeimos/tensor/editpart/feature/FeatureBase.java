package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.ParameterizedType;

/**
 * Base class for EditPart Features.
 * 
 * @author mpdeimos
 */
public abstract class FeatureBase<T extends IFeatureEditPart, U extends ICanvasAction>
		implements IFeature
{
	/** The EditPart for this feature. */
	protected final T editPart;

	/** The class of the canvas action */
	private final Class<U> canvasActionClass;

	/** Constructor. */
	@SuppressWarnings("unchecked")
	public FeatureBase(T editPart)
	{
		this.editPart = editPart;

		ParameterizedType superclass = (ParameterizedType) getClass()
				.getGenericSuperclass();

		this.canvasActionClass = (Class<U>) (superclass)
				.getActualTypeArguments()[1];
	}

	@Override
	public Class<? extends ICanvasAction> getActionGroup()
	{
		return this.canvasActionClass;
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
	public void doOnEditPartSelected(boolean selected)
	{
		// nothing
	}
}
