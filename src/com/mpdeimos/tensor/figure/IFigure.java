package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Interface definition for canvas elements, called figures
 * 
 * @author mpdeimos
 *
 */
public interface IFigure {
	
	/** draws the object to the provided canvas */
	public void draw(Graphics2D gfx);

	/** performs a check whether the mouse is positioned over this figure */
	public boolean isMouseOver(Point point);

}
