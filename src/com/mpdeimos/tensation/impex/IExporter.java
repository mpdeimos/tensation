package com.mpdeimos.tensation.impex;

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
	 * exports the specific data to an xml element.
	 */
	public Element export(Document xmlDoc, Object... helpers);
}
