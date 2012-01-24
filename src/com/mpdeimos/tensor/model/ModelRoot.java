package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * The topmost model object
 * 
 * @author mpdeimos
 */
public class ModelRoot extends ModelDataBase
{
	/** Drawing Canvas. */
	private DrawingCanvas drawingCanvas;

	/** Constructor. */
	public ModelRoot(DrawingCanvas drawingCanvas)
	{
		super(null);
		this.drawingCanvas = drawingCanvas;
	}

	/** Constructor. */
	public ModelRoot()
	{
		this(null);
	}

	/** @return the drawing canvas. */
	public DrawingCanvas getDrawingCanvas()
	{
		return this.drawingCanvas;
	}

	/** @return the drawing canvas. */
	public void setDrawingCanvas(DrawingCanvas drawingCanvas)
	{
		this.drawingCanvas = drawingCanvas;
	}
}
