package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.ui.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
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
				new ImageIcon(R.drawable.EDIT_UNDO_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
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
