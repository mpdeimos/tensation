package com.mpdeimos.tensor.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/** default action implementation. */
public abstract class ActionBase extends AbstractAction
{
	/** Name of the action. */
	private final String name;

	/** Constructor. */
	public ActionBase(String name, ImageIcon imageIcon, KeyStroke accelerator)
	{
		super(name, imageIcon);
		this.putValue(Action.SHORT_DESCRIPTION, name);
		if (accelerator != null)
		{
			this.putValue(Action.ACCELERATOR_KEY, accelerator);
		}
		this.name = name;
	}

	/** Constructor. */
	public ActionBase(String name, ImageIcon imageIcon)
	{
		this(name, imageIcon, null);
	}

	/** @return the name of this action. */
	public String getName()
	{
		return this.name;
	}
}
