package com.mpdeimos.tensation.feature.contract;

import com.mpdeimos.tensation.feature.IFeature;
import com.mpdeimos.tensor.action.canvas.ICanvasAction;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Feature Contract for canvas actions.
 * 
 * @author mpdeimos
 */
public interface ICanvasContract extends IFeature
{
	/** being called when the mouse button is released */
	public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e);

	/** being called when a mouse button is pressed down */
	public boolean doOnMousePressed(ICanvasAction action, MouseEvent e);

	/** being called if the mouse is moved on the canvas with a button clicked */
	public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e);

	/** being called when the mouse clicked on the canvas */
	public boolean doOnMouseClicked(ICanvasAction action, MouseEvent e);

	/** being called if the mouse is moved on the canvas */
	public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e);

	/** being called during the paint cycle of the canvas */
	public boolean drawOverlay(ICanvasAction action, Graphics2D gfx);

	/** being called once a key has been pressed */
	public boolean doOnKeyPressed(ICanvasAction action, KeyEvent e);

	/** being called once a key has been released */
	public boolean doOnKeyReleased(ICanvasAction action, KeyEvent e);

	/** being called once a EditPart has been selected or deselected. */
	public void doOnEditPartSelected(boolean selected);
}
