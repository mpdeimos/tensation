package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensation.feature.IFeature;
import com.mpdeimos.tensor.editpart.IEditPart;

import java.util.List;

/**
 * Interface for EditParts supporting canvas features.
 * 
 * @author mpdeimos
 * 
 */
public interface IFeatureEditPart extends IEditPart
{
	/** @return List of all interaction features. May be null. */
	public <T extends IFeature> List<T> getFeatures(
			Class<T> contract);
}
