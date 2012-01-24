package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.impex.Clipboard;
import com.mpdeimos.tensor.ui.Application;

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
public class DuplicateNewCanvasAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public DuplicateNewCanvasAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_DUPLICATE_NEWCANVAS.string(),
				null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_D, ActionEvent.CTRL_MASK
								+ ActionEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Clipboard.get().clipSelection();
		Application.getApp().createNewCanvas();
		Clipboard.get().copyToCanvas();
	}
}
