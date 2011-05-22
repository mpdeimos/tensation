package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.util.ImmutalList;

/**
 * Base interface for data model objects.
 * 
 * @author mpdeimos
 *
 */
public interface IModelData {
	/**
	 * @return the children of this data model object
	 */
	public ImmutalList<IModelData> getChildren();
	
	/** adds a child to this object */
	public void addChild(IModelData child);
	
	/** removes a child from this object */
	public boolean removeChild(IModelData child);
	
	/** Adds a listener that fires upon model change events */
	public void addModelChangedListener(IModelChangedListener listener);
	
	/** removes each instance of this listener from listening to model changes of this object */
	public boolean removeModelDataChangedListener(IModelChangedListener listener);
}
