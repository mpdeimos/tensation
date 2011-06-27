package com.mpdeimos.tensor.action;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Base class for canvas actions.
 * 
 * @author mpdeimos
 * 
 */
public abstract class CanvasActionBase extends AbstractAction implements ICanvasAction
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
}
