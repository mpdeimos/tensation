package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

	/**
	 * The definition map contains all svg definitions so far. It may be
	 * extended and acts as well as out param.
	 * 
	 * @return the svg node of this element. may be null.
	 */
	public Element getSvgNode(
			Document parent,
			HashMap<String, Element> definitions);
}
