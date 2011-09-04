package com.mpdeimos.tensor.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/** default action implementation. */
public abstract class ActionBase extends AbstractAction
{
	/** Constructor. */
	public ActionBase(String name, ImageIcon imageIcon)
	{
		super(name, imageIcon);
		this.putValue(Action.SHORT_DESCRIPTION, name);
	}
}
