package com.mpdeimos.tensation.model;

/** Operator model type. */
public enum EOperation
{
	/** plus operation. */
	PLUS("+"), //$NON-NLS-1$
	/** minus operation. */
	MINUS("-"); //$NON-NLS-1$

	/** The label of the operator. */
	private String label;

	/** private constructor. */
	private EOperation(String label)
	{
		this.label = label;
	}

	/** @return the label of the operator. */
	public String getLabel()
	{
		return this.label;
	}

	/** String representation of the enum constant. */
	@Override
	public String toString()
	{
		String first = this.name().substring(0, 1);
		String second = this.name().substring(1);

		return first.toUpperCase() + second.toLowerCase();
	}

}