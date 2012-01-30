package com.mpdeimos.tensation.impex.svg;

import com.mpdeimos.tensation.impex.INameEnum;

/**
 * Definitions used for SVG writing.
 * 
 * @author mpdeimos
 * 
 */
public enum ESvgDefinitions implements INameEnum
{
	/** Triangle start marker. */
	MARKER_TRIANGLE_START("marker_triangle_start"), //$NON-NLS-1$

	/** Triangle end marker. */
	MARKER_TRIANGLE_END("marker_triangle_end"), //$NON-NLS-1$

	/** Circle marker. */
	MARKER_CRICLE("marker_circle"), //$NON-NLS-1$

	/** prefix for tensor definitions. */
	TENSOR_DEF_PREFIX("tensor_"), //$NON-NLS-1$

	/** suffix for sink edges */
	TENSOR_DEF_SINK("i"), //$NON-NLS-1$

	/** suffix for sink edges */
	TENSOR_DEF_SOURCE("o"), //$NON-NLS-1$

	/** tensor css class name. */
	CLASS_TENSOR("tensor"), //$NON-NLS-1$

	;

	/** the name of the xml tag. */
	private final String name;

	/** Constructor. */
	ESvgDefinitions(String name)
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
