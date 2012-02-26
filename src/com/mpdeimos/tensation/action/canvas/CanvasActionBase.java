package com.mpdeimos.tensation.action.canvas;

import com.mpdeimos.tensation.action.ActionBase;
import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.ICanvasFeatureContract;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.ContextPanelContentBase;
import com.mpdeimos.tensation.ui.DrawingCanvas;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 * Base class for canvas actions.
 * 
 * @author mpdeimos
 * 
 */
public abstract class CanvasActionBase extends ActionBase implements
		ICanvasAction
{
	/** back reference to the application. */
	protected final Application applicationWindow;

	/** back reference to the drawing panel. */
	protected DrawingCanvas canvas;

	/** flag for bypassing the stop event. */
	private boolean myStop = false;

	/**
	 * Constructor.
	 */
	public CanvasActionBase(
			String name,
			ImageIcon icon)
	{
		this(name, icon, null);
	}

	/** Constructor. */
	public CanvasActionBase(
			String name,
			ImageIcon icon,
			Integer mnemonic)
	{
		super(name, icon);
		this.applicationWindow = Application.getApp();
		if (mnemonic != null)
			putValue(MNEMONIC_KEY, mnemonic);
	}

	@Override
	public void stopAction()
	{
		if (this.myStop)
			return;

		this.applicationWindow.getContextPanel().setContent(null);

		putValue(Action.SELECTED_KEY, false);
		this.canvas.stopCanvasAction();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.myStop = true;
		stopAction();
		this.myStop = false;

		this.canvas = this.applicationWindow.getActiveCanvas();

		this.canvas.startCanvasAction(this);

		this.applicationWindow.getContextPanel().setContent(getContextPanel());
		putValue(Action.SELECTED_KEY, true);
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
	public boolean doOnMouseEntered(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnMouseExited(MouseEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnKeyPressed(KeyEvent e)
	{
		return false;
	}

	@Override
	public boolean doOnKeyReleased(KeyEvent e)
	{
		return false;
	}

	@Override
	public void doOnSelectionChanged()
	{
		// nothing
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

		List<? extends ICanvasFeatureContract> features = ((IFeatureEditPart) editPart).getFeatures(getCanvasContract());
		if (features == null)
			return false;

		for (ICanvasFeatureContract feature : features)
		{
			if (feature.drawOverlay(this, gfx))
				return true;
		}

		return false;
	}

	/** handles the feature action for a specific key event. */
	protected final boolean handleKeyEventForFeatures(
			IEditPart editPart,
			KeyEvent e,
			boolean release)
	{
		if (!(editPart instanceof IFeatureEditPart))
			return false;

		List<? extends ICanvasFeatureContract> features = ((IFeatureEditPart) editPart).getFeatures(getCanvasContract());
		if (features == null)
			return false;

		for (ICanvasFeatureContract feature : features)
		{
			if (release && feature.doOnKeyReleased(this, e))
				return true;
			if (!release && feature.doOnKeyPressed(this, e))
				return true;
		}

		return false;
	}

	/** handles the feature action for a specific key event. */
	protected final boolean handleKeyEventForFeatures(
			List<IEditPart> editParts,
			KeyEvent e,
			boolean release)
	{
		for (IEditPart editPart : new ArrayList<IEditPart>(editParts))
		{
			if (handleKeyEventForFeatures(editPart, e, release))
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
		for (IEditPart editPart : new ArrayList<IEditPart>(editParts))
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

		List<? extends ICanvasFeatureContract> features = ((IFeatureEditPart) editPart).getFeatures(getCanvasContract());
		if (features == null)
			return false;

		for (ICanvasFeatureContract feature : features)
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
	public boolean drawOverlay(Graphics2D gfx)
	{
		return false;
	}

	@Override
	public DrawingCanvas getCanvas()
	{
		return this.canvas;
	}

	/** @return the context panel used by this action. may be null. */
	protected ContextPanelContentBase getContextPanel()
	{
		return null;
	}

	/** @return the canvas action contract. */
	protected abstract Class<? extends ICanvasFeatureContract> getCanvasContract();
}
