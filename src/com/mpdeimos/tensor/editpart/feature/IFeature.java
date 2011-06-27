package com.mpdeimos.tensor.editpart.feature;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Interface for canvas features.
 * 
 * @author mpdeimos
 * 
 */
public interface IFeature
{
	/** being called during the paint cycle of the canvas */
	public boolean drawOverlay(DrawingCanvas canvas, Graphics2D gfx);

	/** being called if the mouse is moved on the canvas */
	public boolean doOnMouseMoved(DrawingCanvas canvas, MouseEvent e);

	/** being called when the mouse clicked on the canvas */
	public boolean doOnMouseClicked(DrawingCanvas canvas, MouseEvent e);

	/** being called when a mouse button is pressed down */
	public boolean doOnMousePressed(DrawingCanvas canvas, MouseEvent e);

	/** being called when the mouse button is released */
	public boolean doOnMouseReleased(DrawingCanvas canvas, MouseEvent e);

	/** being called if the mouse is moved on the canvas with a button clicked */
	public boolean doOnMouseDragged(DrawingCanvas canvas, MouseEvent e);

	/** being called once a EditPart has been selected or deselected. */
	public void doOnEditPartSelected(boolean selected);
}
