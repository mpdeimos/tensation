package com.mpdeimos.tensor.impex;

/**
 * Connection XML definitions.
 * 
 * @author mpdeimos
 * 
 */
public enum EXmlConnection implements IXmlNameEnum
{
	/** element representing a connection. */
	ELEMENT_CONECTION("connection"), //$NON-NLS-1$

	/** the id of a connection. */
	ATTRIB_ID("connid"), //$NON-NLS-1$

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
	private EXmlConnection(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
}
