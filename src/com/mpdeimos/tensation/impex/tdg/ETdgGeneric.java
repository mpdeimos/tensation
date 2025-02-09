package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.impex.INameEnum;

/**
 * Tags used for XML writing and loading.
 * 
 * @author mpdeimos
 * 
 */
public enum ETdgGeneric implements INameEnum
{
	/** The root element. */
	ELEMENT_ROOT("tensordiagram"), //$NON-NLS-1$

	/** root element for all tensors. */
	ELEMENT_TENSORS("tensors"), //$NON-NLS-1$

	/** operators element. */
	ELEMENT_OPERATORS("operators"), //$NON-NLS-1$

	/** operator element. */
	ELEMENT_OPERATOR("operator"), //$NON-NLS-1$

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
	ETdgGeneric(String name)
	{
		this.name = name;
	}

	/** @return the name of the xml tag. */
	@Override
	public String $(Object... format)
	{
		return INameEnum.Impl.$(this.name, format);
	}
}
