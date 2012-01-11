package com.mpdeimos.tensor.ui;

import javax.swing.Action;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/** Extended UndoManager */
public class CanvasUndoManager extends UndoManager
{
	/** Constructor. */
	public CanvasUndoManager()
	{
		updateActions();
	}

	@Override
	public synchronized void undo() throws CannotUndoException
	{
		super.undo();

		updateActions();
	}

	@Override
	public synchronized void redo() throws CannotRedoException
	{
		super.redo();

		updateActions();
	}

	@Override
	public synchronized void discardAllEdits()
	{
		super.discardAllEdits();

		updateActions();
	}

	@Override
	public synchronized boolean addEdit(UndoableEdit anEdit)
	{
		boolean addEdit = super.addEdit(anEdit);

		updateActions();

		return addEdit;
	}

	/** updates the action states. */
	public void updateActions()
	{
		Application.getApp().ACTION_UNDO.setEnabled(canUndo());
		Application.getApp().ACTION_UNDO.putValue(
				Action.NAME,
				getUndoPresentationName());
		Application.getApp().ACTION_UNDO.putValue(
				Action.SHORT_DESCRIPTION,
				getUndoPresentationName());
		Application.getApp().ACTION_REDO.setEnabled(canRedo());
		Application.getApp().ACTION_REDO.putValue(
				Action.NAME,
				getRedoPresentationName());
		Application.getApp().ACTION_REDO.putValue(
				Action.SHORT_DESCRIPTION,
				getRedoPresentationName());
	}
}