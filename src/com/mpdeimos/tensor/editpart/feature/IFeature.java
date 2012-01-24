package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Interface for canvas features.
 * 
 * @author mpdeimos
 * 
 */
public interface IFeature
{
	/** being called during the paint cycle of the canvas */
	public boolean drawOverlay(ICanvasAction action, Graphics2D gfx);

	/** being called if the mouse is moved on the canvas */
	public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e);

	/** being called when the mouse clicked on the canvas */
	public boolean doOnMouseClicked(ICanvasAction action, MouseEvent e);

	/** being called when a mouse button is pressed down */
	public boolean doOnMousePressed(ICanvasAction action, MouseEvent e);

	/** being called when the mouse button is released */
	public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e);

	/** being called if the mouse is moved on the canvas with a button clicked */
	public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e);

	/** being called once a key has been pressed */
	public boolean doOnKeyPressed(ICanvasAction action, KeyEvent e);

	/** being called once a key has been released */
	boolean doOnKeyReleased(ICanvasAction action, KeyEvent e);

	/** being called once a EditPart has been selected or deselected. */
	public void doOnEditPartSelected(boolean selected);

	/** returns the canvas action class this feature responds to. */
	public Class<? extends ICanvasAction> getActionGroup();

}
