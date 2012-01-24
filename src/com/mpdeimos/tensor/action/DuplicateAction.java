package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.impex.Clipboard;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import resources.R;

/**
 * Action for duplicating the active selection.
 * 
 * @author mpdeimos
 * 
 */
public class DuplicateAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public DuplicateAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_DUPLICATE.string(),
				null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Clipboard.get().clipSelection();
		Clipboard.get().copyToCanvas();
	}
}
