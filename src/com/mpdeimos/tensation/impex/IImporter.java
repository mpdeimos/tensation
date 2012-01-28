package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelRoot;

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
