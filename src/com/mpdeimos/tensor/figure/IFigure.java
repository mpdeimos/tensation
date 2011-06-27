package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Interface definition for canvas elements, called figures
 * 
 * @author mpdeimos
 * 
 */
public interface IFigure
{

	/** draws the object to the provided canvas */
	public void draw(Graphics2D gfx);

	/** performs a check whether a point hits this figure */
	public boolean containsPoints(Point point);

	/** performs a check whether a rectangle intersects this figure */
	public boolean intersects(Rectangle rect);

	/** @return the bounding rectangle */
	public Rectangle getBoundingRectangle();

	/** Schedules a figure redraw. */
	public void redraw();

}
