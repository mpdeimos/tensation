package com.mpdeimos.tensor.util;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;

/**
 * UndoableEdit implementation that can always be done and redone.
 * 
 * @author mpdeimos
 */
abstract public class InfiniteUndoableEdit extends AbstractUndoableEdit
{
	/** Constructor. */
	public InfiniteUndoableEdit()
	{
		this.init();
	}

	/** Performs initialization tasks upon construction. Meant for overriding. */
	protected void init()
	{
		// nothing
	}

	@Override
	public void redo()
	{
		// nothing
	}

	@Override
	public void undo()
	{
		// nothing
	}

	@Override
	public boolean canRedo()
	{
		return true;
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}

	/** performs redo and returns itself (useful for constructor chaining). */
	public UndoableEdit act()
	{
		this.redo();

		return this;
	}
}
