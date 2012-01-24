package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import resources.R;

/**
 * Action for copying the active selection to the clipboard.
 * 
 * @author mpdeimos
 * 
 */
public class DeleteSelectionAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public DeleteSelectionAction()
	{
		super(
				R.string.WINDOW_MENU_EDIT_DELETE.string(),
				null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_DELETE, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		DrawingCanvas activeCanvas = Application.getApp().getActiveCanvas();
		final IEditPart[] selectedEditParts = activeCanvas.getSelectedEditParts().toArray(
				new IEditPart[0]);
		if (selectedEditParts.length > 0)
		{
			final ModelRoot root = activeCanvas.getModel();
			activeCanvas.getUndoManager().addEdit(new InfiniteUndoableEdit()
			{
				@Override
				public void redo()
					{
						for (IEditPart part : selectedEditParts)
							root.removeChild(part.getModel());
					}

				@Override
				public void undo()
					{
						for (IEditPart part : selectedEditParts)
							root.addChild(part.getModel());
					}

				@Override
				public String getPresentationName()
					{
						return R.string.WINDOW_MENU_EDIT_DELETE.string();
					}
			}.act());
		}
	}
}
