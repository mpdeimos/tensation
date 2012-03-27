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
	/** Triangle start marker w/ color. */
	MARKER_TRIANGLE_START_COLOR("marker_triangle_start_%x"), //$NON-NLS-1$

	/** Triangle end marker. */
	MARKER_TRIANGLE_END("marker_triangle_end"), //$NON-NLS-1$
	/** Triangle end marker w/ color. */
	MARKER_TRIANGLE_END_COLOR("marker_triangle_end_%x"), //$NON-NLS-1$

	/** Circle marker. */
	MARKER_CRICLE("marker_circle"), //$NON-NLS-1$

	/** prefix for tensor definitions. */
	TENSOR_DEF_PREFIX("tensor_"), //$NON-NLS-1$

	/** prefix for operator definitions. */
	OPERATOR_DEF_PREFIX("operator_"), //$NON-NLS-1$

	/** prefix for tensor definitions. */
	TENSOR_DEF_COLOR_POSTFIX("_c%x"), //$NON-NLS-1$

	/** prefix for tensor definitions. */
	TENSOR_DEF_LABEL_POSTFIX("_l"), //$NON-NLS-1$

	/** suffix for sink edges */
	TENSOR_DEF_SINK("i"), //$NON-NLS-1$

	/** suffix for source edges */
	TENSOR_DEF_SOURCE("o"), //$NON-NLS-1$

	/** suffix for connected source edges */
	TENSOR_DEF_SOURCE_CONNECTED("c"), //$NON-NLS-1$

	/** tensor css class name. */
	CLASS_TENSOR("tensor"), //$NON-NLS-1$

	/** connection css class name. */
	CLASS_CONNECTION("connection"), //$NON-NLS-1$

	/** operator css class name. */
	CLASS_OPERATOR("operator"), //$NON-NLS-1$

	/** operator circle css class name. */
	CLASS_OPERATOR_CIRCLE("g_operator_circle"), //$NON-NLS-1$

	/** connection path css class name. */
	CLASS_CONNECTION_PATH("connectionPath"), //$NON-NLS-1$

	/** tensor circle class. */
	CLASS_TENSOR_CIRCLE("g_tensor_circle"), //$NON-NLS-1$

	/** tensor circle unfilled class. */
	CLASS_TENSOR_CIRCLE_UNFILLED("g_tensor_circle_unfilled"), //$NON-NLS-1$

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
