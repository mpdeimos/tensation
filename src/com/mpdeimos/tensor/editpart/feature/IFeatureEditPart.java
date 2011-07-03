package com.mpdeimos.tensor.editpart.feature;

import java.util.List;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.editpart.IEditPart;

/**
 * Interface for EditParts supporting canvas features.
 * 
 * @author mpdeimos
 * 
 */
public interface IFeatureEditPart extends IEditPart
{
	/** @return List of all interaction features. May be null. */
	public List<IFeature> getFeatures(Class<? extends ICanvasAction> group);
}
