package com.mpdeimos.tensor.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import com.mpdeimos.tensor.util.ImmutableList;

/**
 * Abstract base class for model data
 * 
 * @author mpdeimos
 * 
 */
public abstract class ModelDataBase implements IModelData
{
	/** initial list size for children */
	private static int INITIAL_CHILDLIST_SIZE = 3;

	/** listeners for modeldata changes */
	private HashSet<IModelChangedListener> listeners = new LinkedHashSet<IModelChangedListener>();

	/** the children of this model */
	private List<IModelData> children = null;

	/** Parent model */
	private final IModelData parent;

	/** Constructor. */
	public ModelDataBase(IModelData parent)
	{
		this.parent = parent;
	}

	@Override
	public ImmutableList<IModelData> getChildren()
	{
		if (children == null)
			return null;

		return new ImmutableList<IModelData>(children);
	}

	@Override
	public void addChild(IModelData child)
	{
		ensureChildListExists();

		children.add(child);

		fireOnChildAdded(child);
	}

	@Override
	public boolean removeChild(IModelData child)
	{
		ensureChildListExists();

		boolean response = children.remove(child);
		if (response == true)
			fireOnChildRemoved(child);

		return response;
	}

	/** ensures the list of children actually exists */
	private void ensureChildListExists()
	{
		if (children == null)
			children = new ArrayList<IModelData>(INITIAL_CHILDLIST_SIZE);
	}

	@Override
	public void addModelChangedListener(IModelChangedListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public boolean removeModelDataChangedListener(IModelChangedListener listener)
	{
		return listeners.remove(listener);
	}

	/** fires the onModelDataChanged callback for all listeners */
	protected void fireOnModelDataChanged(IModelData model)
	{
		for (IModelChangedListener listener : listeners)
		{
			listener.onModelChanged(model);
		}

		if (parent != null && parent instanceof ModelDataBase)
			((ModelDataBase) parent).fireOnModelDataChanged(model);
	}

	/** fires the onChildAdded callback for all listeners */
	protected void fireOnChildAdded(IModelData child)
	{
		for (IModelChangedListener listener : listeners)
		{
			listener.onChildAdded(child);
		}

		if (parent != null && parent instanceof ModelDataBase)
			((ModelDataBase) parent).fireOnModelDataChanged(child);
	}

	/** fires the onChildRemoved callback for all listeners */
	protected void fireOnChildRemoved(IModelData child)
	{
		for (IModelChangedListener listener : listeners)
		{
			listener.onChildRemoved(child);
		}

		if (parent != null && parent instanceof ModelDataBase)
			((ModelDataBase) parent).fireOnModelDataChanged(child);
	}

}
