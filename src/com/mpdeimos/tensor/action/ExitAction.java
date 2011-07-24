package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.ApplicationWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import resources.R;

/**
 * Action for exiting the program.
 * 
 * @author mpdeimos
 * 
 */
public class ExitAction extends AbstractAction
{

	/**
	 * Constructor.
	 */
	public ExitAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_EXIT.string(),
				new ImageIcon(R.drawable.WINDOW_EXIT.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ApplicationWindow.getApplicationWindow().exit();
	}
}
