/**
 * 
 */
package com.mpdeimos.tensor.editpart;

import java.awt.Point;

import com.mpdeimos.tensor.editpart.feature.IFeatureEditPart;


/**
 * Base interface for movable EditParts.
 * 
 * @author mpdeimos
 *
 */
public interface IMovableEditPart extends IFeatureEditPart {
	/** Returns the current position of the EditPart. */
	public Point getPosition();
	
	/** Sets the position of the current EditPart. */
	public void setPosition(Point p);
}
