package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;

import org.w3c.dom.Element;

/**
 * Base interface for import of an xml document into model data.
 * 
 * @author mpdeimos
 * 
 */
public interface IImporter
{
	/**
	 * imports an xml node to a model data object.
	 */
	public IModelData importNode(Element node, ModelRoot mr, Object... helpers);
}
