package com.mpdeimos.tensation.impex;

/**
 * Tensor XML definitions.
 * 
 * @author mpdeimos
 * 
 */
public enum ETdgTensor implements INameEnum
{
	/** element representing a tensor. */
	ELEMENT_TENSOR("tensor"), //$NON-NLS-1$

	/** one anchor of a tensor. */
	ELEMENT_TENSOR_ANCHOR("anchor"), //$NON-NLS-1$

	/** the id of a tensor. */
	ATTRIB_ID("tensorid"), //$NON-NLS-1$

	/** the x position of a tensor. */
	ATTRIB_POS_X("x"), //$NON-NLS-1$

	/** the y position of a tensor. */
	ATTRIB_POS_Y("y"), //$NON-NLS-1$

	/** the rotation of the tensor. */
	ATTRIB_ROTATION("rot"), //$NON-NLS-1$

	/** the id of an anchor. */
	ATTRIB_ANCHOR_ID("anchorid"), //$NON-NLS-1$

	/** the direction of an connection anchor. */
	ATTRIB_ANCHOR_DIRECTION("dir"), //$NON-NLS-1$

	/** anchor direction: sink. */
	VALUE_ANCHOR_SINK("sink"), //$NON-NLS-1$

	/** anchor direction: source. */
	VALUE_ANCHOR_SOURCE("source"), //$NON-NLS-1$
	;

	/** the attribute name. */
	private String name;

	/** Constructor. */
	private ETdgTensor(String name)
	{
		this.name = name;
	}

	@Override
	public String $(Object... format)
	{
		return INameEnum.Impl.$(this.name, format);
	}
}
