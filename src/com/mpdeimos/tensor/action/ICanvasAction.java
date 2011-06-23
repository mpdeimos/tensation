package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Base interface for drawing canvas actions.
 * 
 * @author mpdeimos
 *
 */
public interface ICanvasAction {
	/** being called if the mouse is moved on the canvas */
	public boolean doOnMouseMoved(MouseEvent e);
	
	/** being called when the mouse clicked on the canvas */
	public boolean doOnMouseClicked(MouseEvent e);
	
	/** being called when a mouse button is pressed down  */
	public boolean doOnMousePressed(MouseEvent e);
	
	/** being called when the mouse button is released */
	public boolean doOnMouseReleased(MouseEvent e);
	
	/** being called if the mouse is moved on the canvas with a button clicked */
	public boolean doOnMouseDragged(MouseEvent e);
	
	/** being called during the paint cycle of the canvas */
	public boolean drawOverlay(Graphics2D gfx);
	
	/** stops the current canvas action */
	public void stopAction();


}
