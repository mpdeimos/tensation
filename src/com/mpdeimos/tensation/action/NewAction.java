package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.ui.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import resources.R;

/**
 * Creates a new tensor model.
 * 
 * @author mpdeimos
 * 
 */
public class NewAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public NewAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_NEW.string(),
				new ImageIcon(R.drawable.DOCUMENT_NEW_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().createNewCanvas();
	}
}
