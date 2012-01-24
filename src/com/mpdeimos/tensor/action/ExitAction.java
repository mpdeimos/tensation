package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import resources.R;

/**
 * Action for exiting the program.
 * 
 * @author mpdeimos
 * 
 */
public class ExitAction extends ActionBase
{

	/**
	 * Constructor.
	 */
	public ExitAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_EXIT.string(),
				new ImageIcon(R.drawable.WINDOW_EXIT.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().exit();
	}
}
