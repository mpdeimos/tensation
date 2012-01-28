package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.TensorConnectAction;

import java.awt.Color;

/**
 * Interface for EditParts that have customizable appearance attributes.
 * 
 * @author mpdeimos
 * 
 */
public interface ICustomizable extends IFeatureEditPart
{
	/** @return the color of the editpart */
	public Color getColor();

	/** sets the color of the editpart. */
	public void setColor(Color color);

	/** feature class for EditParts with connection sources. */
	public class Feature extends TensorConnectAction.IFeature<ICustomizable>
	{
		/** Constructor. */
		public Feature(ICustomizable editPart)
		{
			super(editPart);
		}

	}
}
