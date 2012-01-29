package com.mpdeimos.tensation.impex;

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
	 * imports an xml node to an object.
	 */
	public Object importNode(Element node, Object... helpers);
}
