package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;

/**
 * Interface definition for canvas elements, called figures
 * 
 * @author mpdeimos
 *
 */
public interface IFigure {
	
	/** draws the object to the provided canvas */
	public void draw(Graphics2D gfx);

}
