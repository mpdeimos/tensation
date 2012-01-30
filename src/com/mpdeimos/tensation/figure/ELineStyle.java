package com.mpdeimos.tensation.figure;

import resources.R;
import resources.R.string;

/** Line style enumeration. */
public enum ELineStyle
{
	/** Solid stroke */
	SOLID(0, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_SOLID),
	/** Dotted stroke */
	DOTTED(1, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_DOTTED),
	/** Dashed stroke */
	DASHED(3, R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_LINE_STYLE_DASHED);

	/** The style display name. */
	private final string name;

	/** the pattern multiplier. */
	private final int patternMultiplier;

	/** Constructor. */
	private ELineStyle(int patternMultiplier, R.string name)
	{
		this.patternMultiplier = patternMultiplier;
		this.name = name;
	}

	@Override
	public String toString()
	{
		return this.name.toString();
	}

	/** @return the pattern multiplier value */
	public int getPatternMultiplier()
	{
		return this.patternMultiplier;
	}
}