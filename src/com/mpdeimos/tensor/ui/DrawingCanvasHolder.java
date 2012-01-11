package com.mpdeimos.tensor.ui;

import java.awt.Adjustable;
import java.awt.BorderLayout;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * Windowholder for the drawing canvas with scrollbars.
 * 
 * @author mpdeimos
 * 
 */
public class DrawingCanvasHolder extends JPanel
{
	/** The enclosed drawing canvas */
	private final DrawingCanvas canvas;

	/** The horizontal scrollbar for the canvas. */
	private final JScrollBar hScrollBar;

	/** the vertical scrollbar for the canvas. */
	private final JScrollBar vScrollBar;

	/** Constructor. */
	public DrawingCanvasHolder()
	{
		this.setLayout(new BorderLayout(0, 0));

		this.hScrollBar = new JScrollBar(Adjustable.HORIZONTAL);
		this.add(this.hScrollBar, BorderLayout.SOUTH);

		this.vScrollBar = new JScrollBar(Adjustable.VERTICAL);
		this.add(this.vScrollBar, BorderLayout.EAST);

		this.canvas = new DrawingCanvas(this);
		this.add(this.canvas, BorderLayout.CENTER);
	}

	/** @return the horizontal scroll model */
	public BoundedRangeModel getHorizontalScrollModel()
	{
		return this.hScrollBar.getModel();
	}

	/** @return the vertical scroll model */
	public BoundedRangeModel getVerticalScrollModel()
	{
		return this.vScrollBar.getModel();
	}

	/** @return the enclosed drawing canvas. */
	public DrawingCanvas getDrawingCanvas()
	{
		return this.canvas;
	}
}
