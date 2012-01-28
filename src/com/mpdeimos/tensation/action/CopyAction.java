package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.impex.Clipboard;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import resources.R;

/**
 * Action for copying the active selection to the clipboard.
 * 
 * @author mpdeimos
 * 
 */
public class CopyAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public CopyAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_COPY.string(),
				null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Clipboard.get().clipSelection();
	}
}
