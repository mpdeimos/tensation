package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
				R.string.WINDOW_MENU_NEW.string(),
				new ImageIcon(R.drawable.DOCUMENT_NEW_16.url()));
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

		Application.getApp().setModel(new ModelRoot());
	}
}
