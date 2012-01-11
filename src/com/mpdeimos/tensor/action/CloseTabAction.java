package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import resources.R;

/**
 * Action for exiting the program.
 * 
 * @author mpdeimos
 * 
 */
public class CloseTabAction extends ActionBase
{

	/**
	 * Constructor.
	 */
	public CloseTabAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_CLOSETAB.string(),
				new ImageIcon(R.drawable.TAB_CLOSE.url()));
		this.putValue(
				Action.SHORT_DESCRIPTION,
				R.string.WINDOW_MENU_FILE_CLOSETAB_DESCRIPTION.string());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		int answer = JOptionPane.showConfirmDialog(
				Application.getApp(),
				R.string.DLG_QUESTION_SAVE_BEFORE_NEW.string(),
				R.string.DLG_QUESTION_SAVE_BEFORE_NEW_TITLE.string(),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (answer == JOptionPane.CANCEL_OPTION)
			return;

		if (answer == JOptionPane.OK_OPTION)
		{
			new SaveAction().actionPerformed(e);
		}

		Application.getApp().closeActiveCanvas();
	}
}
