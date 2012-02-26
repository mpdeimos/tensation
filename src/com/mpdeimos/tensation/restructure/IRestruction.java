package com.mpdeimos.tensation.restructure;

import com.mpdeimos.tensation.editpart.IEditPart;

/** Restruction action interface. */
public interface IRestruction
{
	/** @return true when the given editpart is restructable by this restruction. */
	public boolean isRestructable(IEditPart restructable);

	/** performs the graph restruction. */
	public void restruct(IEditPart restructable);

	/** @return the human readable name of the restruction. */
	public String getDisplayName();
}
