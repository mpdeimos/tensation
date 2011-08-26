package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.IModelData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base interface for export of model data into an xml format.
 * 
 * @author mpdeimos
 * 
 */
public interface IExporter
{
	/**
	 * exports the specific model to an xml element.
	 */
	public Element export(Document xmlDoc, IModelData data, Object... helpers);
}
