package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.feature.IFeature;
import com.mpdeimos.tensation.util.Reflections;

/**
 * Base class for EditPart Features.
 * 
 * @author mpdeimos
 */
public abstract class EditPartFeatureBase<T extends IFeatureEditPart, F extends IFeature>
		implements IFeature
{
	/** The EditPart for this feature. */
	protected final T editPart;

	/** The class of the feature contract */
	private final Class<F> featureContract;

	/** Constructor. */
	@SuppressWarnings("unchecked")
	public EditPartFeatureBase(T editPart)
	{
		this.editPart = editPart;

		this.featureContract = (Class<F>) Reflections.getGenericType(
				this,
				EditPartFeatureBase.class,
				1);
	}

	@Override
	public Class<F> getFeatureContract()
	{
		return this.featureContract;
	}
}
