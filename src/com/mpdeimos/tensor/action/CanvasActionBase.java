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
public abstract class CanvasActionBase extends AbstractAction implements ICanvasAction {

	/** back reference to the drawing panel. */
	protected final DrawingCanvas drawingPanel;
	
	/**
	 * Constructor.
	 */
	public CanvasActionBase(DrawingCanvas drawingPanel, String name, ImageIcon icon)
	{
		super(name, icon);
		this.drawingPanel = drawingPanel;
	}

	@Override
	public void stopAction() {
		putValue(Action.SELECTED_KEY, false);
		drawingPanel.stopCanvasAction();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		putValue(Action.SELECTED_KEY, true);
		drawingPanel.startCanvasAction(this);
	}
	
	@Override
	public boolean doOnMouseReleased(MouseEvent e) {
		return false;
	}
	
	@Override
	public boolean doOnMousePressed(MouseEvent e) {
		return false;
	}
	
	@Override
	public boolean doOnMouseDragged(MouseEvent e) {
		return false;
	}
	
	@Override
	public boolean doOnMouseClicked(MouseEvent e) {
		return false;
	}
	
	@Override
	public boolean doOnMouseMove(MouseEvent e) {
		return false;
	}
}
