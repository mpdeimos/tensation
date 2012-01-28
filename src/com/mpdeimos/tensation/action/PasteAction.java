package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.impex.Clipboard;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import resources.R;

/**
 * Action for pasting the copied selection from the clipboard.
 * 
 * @author mpdeimos
 * 
 */
public class PasteAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public PasteAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_PASTE.string(),
				null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Clipboard.get().copyToCanvas();
	}
}
