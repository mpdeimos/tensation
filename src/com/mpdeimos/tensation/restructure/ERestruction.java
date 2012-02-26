package com.mpdeimos.tensation.restructure;

/** enum for restruction actions. just used for epsilon-delta for now. */
public enum ERestruction
{
	/** epsilon delta rule. */
	EPSILON_DELTA(new EpsilonDeltaRestruction());

	/** The bound restruction action. */
	private final IRestruction restruction;

	/** private constructor. */
	private ERestruction(IRestruction restruction)
	{
		this.restruction = restruction;
	}

	/** @return the bound restruction. */
	public IRestruction getRestruction()
	{
		return this.restruction;
	}
}
