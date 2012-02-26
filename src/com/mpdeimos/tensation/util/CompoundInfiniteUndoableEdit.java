package com.mpdeimos.tensation.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Compound version of an infinite undoable edit.
 * 
 * @author mpdeimos
 * 
 */
public class CompoundInfiniteUndoableEdit extends InfiniteUndoableEdit
{
	/** The edits. */
	List<InfiniteUndoableEdit> edits = new ArrayList<InfiniteUndoableEdit>();

	@Override
	public String getPresentationName()
	{
		if (this.edits.isEmpty())
			return null;

		return this.edits.get(0).getPresentationName();
	}

	@Override
	public void undo()
	{
		for (InfiniteUndoableEdit edit : this.edits)
			edit.undo();
	}

	@Override
	public void redo()
	{
		for (InfiniteUndoableEdit edit : this.edits)
			edit.redo();
	}

	/** performs redo and returns itself (useful for constructor chaining). */
	@Override
	public CompoundInfiniteUndoableEdit act()
	{
		this.redo();

		return this;
	}

	/** Adds a new edit. */
	public void add(InfiniteUndoableEdit edit)
	{
		this.edits.add(edit);
	}
}
