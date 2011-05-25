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
	public boolean doOnMouseMove(MouseEvent e);
	
	/** being called when the mouse clicked on the canvas */
	public boolean doOnMouseClicked(MouseEvent e);
	
	/** being called during the paint cycle of the canvas */
	public boolean drawOverlay(Graphics2D gfx);
}
