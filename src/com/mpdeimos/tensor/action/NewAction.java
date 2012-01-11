package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

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
				new ImageIcon(R.drawable.DOCUMENT_NEW_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().createNewCanvas();
	}
}
