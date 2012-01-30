package com.mpdeimos.tensation.impex.export;

import java.util.HashMap;

/**
 * Interface for importing/exporting values via an keyvalue store mechanism.
 * 
 * @author mpdeimos
 */
public interface IExportable
{
	/** sets the values of this container from a hashmap. */
	public void setValues(HashMap<String, Object> map);

	/** @return the values of this container as hashmap. */
	public HashMap<String, Object> getValues();
}
