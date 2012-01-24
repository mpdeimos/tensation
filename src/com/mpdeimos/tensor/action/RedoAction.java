package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

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
public class RedoAction extends ActionBase
{

	/**
	 * Constructor.
	 */
	public RedoAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_REDO.string(),
				new ImageIcon(R.drawable.EDIT_REDO_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		UndoManager undoManager = Application.getApp().getActiveCanvas().getUndoManager();

		if (!undoManager.canRedo())
			return;

		undoManager.redo();
	}
}
