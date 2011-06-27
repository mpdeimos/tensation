package com.mpdeimos.tensor.model;

/**
 * Interface for receiving messages for changed model data
 * 
 * @author mpdeimos
 * 
 */
public interface IModelChangedListener
{
	/**
	 * fires upon changes within the data of this model, but not when children
	 * are added or removed.
	 */
	public void onModelChanged(IModelData model);

	/** fires upon a child being added to this model */
	public void onChildAdded(IModelData child);

	/** fires upon a child being removed from this model */
	public void onChildRemoved(IModelData child);
}
