package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.impex.INameEnum;

/**
 * Key Value Store XML definitions.
 * 
 * @author mpdeimos
 * 
 */
public enum ETdgKeyValueStore implements INameEnum
{
	/** element representing an keyvalue store node. */
	ELEMENT_ATTRIBUTES("attributes"), //$NON-NLS-1$

	/** element representing an attribute entry. */
	ELEMENT_ITEM("attrib"), //$NON-NLS-1$

	/** the group of an attribute. */
	ATTRIB_GROUP_NAME("name"), //$NON-NLS-1$

	/** the name of an attribute. */
	ATTRIB_ITEM_KEY("key"), //$NON-NLS-1$

	/** the value of an attribute. */
	ATTRIB_ITEM_VALUE("value"), //$NON-NLS-1$

	/** the type of an attribute. */
	ATTRIB_ITEM_TYPE("type"), //$NON-NLS-1$

	;

	/** the attribute name. */
	private String name;

	/** Constructor. */
	private ETdgKeyValueStore(String name)
	{
		this.name = name;
	}

	@Override
	public String $(Object... format)
	{
		return INameEnum.Impl.$(this.name, format);
	}
}
