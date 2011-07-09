package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IFeature;
import com.mpdeimos.tensor.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Base class for canvas actions.
 * 
 * @author mpdeimos
 * 
 */
public abstract class CanvasActionBase extends AbstractAction implements
		ICanvasAction
{

	/** back reference to the drawing panel. */
	protected final DrawingCanvas canvas;

	/**
	 * Constructor.
	 */
	public CanvasActionBase(DrawingCanvas canvas, String name, ImageIcon icon)
	{
		super(name, icon);
		this.canvas = canvas;
		this.putValue(Action.SHORT_DESCRIPTION, name);
	}

	@Override
	public void stopAction()
	{
		putValue(Action.SELECTED_KEY, false);
		this.canvas.stopCanvasAction();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		putValue(Action.SELECTED_KEY, true);
		this.canvas.startCanvasAction(this);
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnKeyPressed(KeyEvent e)
	{
		return false;
	}

	/** Draws an overlay for all features of the given EditPart. */
	protected final boolean drawOverlayForFeatures(
			List<IEditPart> editParts,
			Graphics2D gfx)
	{
		for (IEditPart editPart : editParts)
		{
			if (drawOverlayForFeatures(editPart, gfx))
				return true;
		}

		return false;
	}

	/** Draws an overlay for all features of the given EditPart. */
	protected final boolean drawOverlayForFeatures(
			IEditPart editPart,
			Graphics2D gfx)
	{
		if (!(editPart instanceof IFeatureEditPart))
			return false;

		List<IFeature> features = ((IFeatureEditPart) editPart).getFeatures(this.getClass());
		if (features == null)
			return false;

		for (IFeature feature : features)
		{
			if (feature.drawOverlay(this, gfx))
				return true;
		}

		return false;
	}

	/** Handles the feature actions for a specific mouse event. */
	protected final boolean handleMouseEventForFeatures(
			List<IEditPart> editParts,
			MouseEvent e,
			int which)
	{
		for (IEditPart editPart : editParts)
		{
			if (handleMouseEventForFeatures(editPart, e, which))
				return true;
		}

		return false;
	}

	/** Handles the feature actions for a specific mouse event. */
	protected final boolean handleMouseEventForFeatures(
			IEditPart editPart,
			MouseEvent e,
			int which)
	{
		if (!(editPart instanceof IFeatureEditPart))
			return false;

		List<IFeature> features = ((IFeatureEditPart) editPart).getFeatures(this.getClass());
		if (features == null)
			return false;

		for (IFeature feature : features)
		{
			boolean handled = false;

			switch (which)
			{
			case MouseEvent.MOUSE_CLICKED:
				handled = feature.doOnMouseClicked(this, e);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				handled = feature.doOnMouseDragged(this, e);
				break;
			case MouseEvent.MOUSE_MOVED:
				handled = feature.doOnMouseMoved(this, e);
				break;
			case MouseEvent.MOUSE_PRESSED:
				handled = feature.doOnMousePressed(this, e);
				break;
			case MouseEvent.MOUSE_RELEASED:
				handled = feature.doOnMouseReleased(this, e);
				break;
			default:
				handled = false;
				break;
			}

			if (handled)
				return true;
		}

		return false;
	}

	@Override
	public DrawingCanvas getCanvas()
	{
		return this.canvas;
	}
}
