package com.mpdeimos.tensor.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/** default action implementation. */
public abstract class ActionBase extends AbstractAction
{
	/** Name of the action. */
	private final String name;

	/** Constructor. */
	public ActionBase(String name, ImageIcon imageIcon)
	{
		super(name, imageIcon);
		this.putValue(Action.SHORT_DESCRIPTION, name);
		this.name = name;
	}

	/** @return the name of this action. */
	public String getName()
	{
		return this.name;
	}
}
