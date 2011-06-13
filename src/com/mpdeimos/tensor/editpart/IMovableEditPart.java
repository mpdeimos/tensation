/**
 * 
 */
package com.mpdeimos.tensor.editpart;

import java.awt.Point;

/**
 * Base interface for movable EditParts.
 * 
 * @author mpdeimos
 *
 */
public interface IMovableEditPart {
	/** Returns the current position of the EditPart. */
	public Point getPosition();
	
	/** Sets the position of the current EditPart. */
	public void setPosition(Point p);
}
