package com.mpdeimos.tensor.impex;

/**
 * Connection XML definitions.
 * 
 * @author mpdeimos
 * 
 */
public enum ETdgConnection implements INameEnum
{
	/** element representing a connection. */
	ELEMENT_CONECTION("connection"), //$NON-NLS-1$

	/** the tensor this connection is drawn from. */
	ATTRIB_SOURCE("source"), //$NON-NLS-1$

	/** the from tensor anchor id */
	ATTRIB_SOURCE_ANCHOR("sourceanchor"), //$NON-NLS-1$

	/** the from tensor anchor control point distance */
	ATTRIB_SOURCE_DISTANCE("sourcedistance"), //$NON-NLS-1$

	/** the tensor this connection is drawn to */
	ATTRIB_SINK("sink"), //$NON-NLS-1$

	/** the to anchor id */
	ATTRIB_SINK_ANCHOR("sinkanchor"), //$NON-NLS-1$

	/** the from tensor anchor control point distance */
	ATTRIB_SINK_DISTANCE("sinkdistance"), //$NON-NLS-1$
	;

	/** the attribute name. */
	private String name;

	/** Constructor. */
	private ETdgConnection(String name)
	{
		this.name = name;
	}

	@Override
	public String $(Object... format)
	{
		return INameEnum.Impl.$(this.name, format);
	}
}
