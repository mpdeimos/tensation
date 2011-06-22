/**
 * 
 */
package com.mpdeimos.tensor.editpart;


/**
 * Base interface for rotatable EditParts.
 * 
 * @author mpdeimos
 *
 */
public interface IRotatableEditPart {
	/** Returns the current rotation of the EditPart in degrees. */
	public double getRotation();
	
	/** Sets the rotation of the current EditPart in degrees. */
	public void setRotation(double degrees);
}
