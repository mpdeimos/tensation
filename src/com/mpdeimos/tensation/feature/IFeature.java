package com.mpdeimos.tensation.feature;

/**
 * Interface for canvas features.
 * 
 * @author mpdeimos
 * 
 */
public interface IFeature
{
	/** returns the feature contract this feature responds to. */
	public Class<? extends IFeature> getFeatureContract();

}
