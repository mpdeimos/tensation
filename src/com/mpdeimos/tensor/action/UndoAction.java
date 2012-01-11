package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.undo.UndoManager;

import resources.R;

/**
 * Action for exiting the program.
 * 
 * @author mpdeimos
 * 
 */
public class UndoAction extends ActionBase
{

	/**
	 * Constructor.
	 */
	public UndoAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_UNDO.string(),
				new ImageIcon(R.drawable.EDIT_UNDO_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		UndoManager undoManager = Application.getApp().getActiveCanvas().getUndoManager();

		if (!undoManager.canUndo())
			return;

		undoManager.undo();
	}
}
