package com.mpdeimos.tensor.impex;

/**
 * Tags used for XML writing and loading.
 * 
 * @author mpdeimos
 * 
 */
public enum EXmlGeneric implements IXmlNameEnum
{
	/** The root element. */
	ELEMENT_ROOT("tensordiagram"), //$NON-NLS-1$

	/** root element for all tensors. */
	ELEMENT_TENSORS("tensors"), //$NON-NLS-1$

	/** the id of an XML tag. */
	ATTRIB_ID("id"), //$NON-NLS-1$

	/** root element for all connections between tensors. */
	ELEMENT_CONNECTIONS("connections"), //$NON-NLS-1$

	/** element representing a tensor connection. */
	CONNECTION("connection"), //$NON-NLS-1$
	;

	/** the name of the xml tag. */
	private final String name;

	/** Constructor. */
	EXmlGeneric(String name)
	{
		this.name = name;
	}

	/** @return the name of the xml tag. */
	@Override
	public String getName()
	{
		return this.name;
	}
}
