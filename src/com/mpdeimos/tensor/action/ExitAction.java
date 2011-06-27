package com.mpdeimos.tensor.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.ui.ApplicationWindow;

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
		super(R.strings.getString("window_menu_file_exit"), new ImageIcon(R.drawable.getURL("window_exit"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ApplicationWindow.getApplicationWindow().exit();
	}
}
