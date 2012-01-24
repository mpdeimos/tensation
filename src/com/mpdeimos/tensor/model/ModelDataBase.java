package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.util.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

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
	private final HashSet<IModelChangedListener> listeners = new LinkedHashSet<IModelChangedListener>();

	/** the children of this model */
	private List<IModelData> children = null;

	/** Parent model */
	/* package */IModelData parent;

	/** Constructor. */
	public ModelDataBase(IModelData parent)
	{
		this.parent = parent;
	}

	@Override
	public ImmutableList<IModelData> getChildren()
	{
		if (this.children == null)
			return new ImmutableList<IModelData>();

		return new ImmutableList<IModelData>(this.children);
	}

	@Override
	public boolean remove()
	{
		if (this.parent != null)
			return this.parent.removeChild(this);

		return false;
	}

	@Override
	public void addChild(IModelData child)
	{
		ensureChildListExists();

		this.children.add(child);

		fireOnChildAdded(child);
	}

	@Override
	public boolean removeChild(IModelData child)
	{
		ensureChildListExists();

		boolean response = this.children.remove(child);
		if (response == true)
			fireOnChildRemoved(child);

		return response;
	}

	/** ensures the list of children actually exists */
	private void ensureChildListExists()
	{
		if (this.children == null)
			this.children = new ArrayList<IModelData>(INITIAL_CHILDLIST_SIZE);
	}

	@Override
	public void addModelChangedListener(IModelChangedListener listener)
	{
		this.listeners.add(listener);
	}

	@Override
	public boolean removeModelDataChangedListener(IModelChangedListener listener)
	{
		return this.listeners.remove(listener);
	}

	/** fires the onModelDataChanged callback for all listeners */
	protected void fireOnModelDataChanged(IModelData model)
	{
		for (IModelChangedListener listener : this.listeners)
		{
			if (listener == null)
				removeModelDataChangedListener(listener);
			else
				listener.onModelChanged(model);
		}

		if (this.parent != null && this.parent instanceof ModelDataBase)
			((ModelDataBase) this.parent).fireOnModelDataChanged(model);
	}

	/** fires the onChildAdded callback for all listeners */
	protected void fireOnChildAdded(IModelData child)
	{
		for (IModelChangedListener listener : this.listeners)
		{
			if (listener == null)
				removeModelDataChangedListener(listener);
			else
				listener.onChildAdded(child);
		}

		if (this.parent != null && this.parent instanceof ModelDataBase)
			((ModelDataBase) this.parent).fireOnModelDataChanged(child);
	}

	/** fires the onChildRemoved callback for all listeners */
	protected void fireOnChildRemoved(IModelData child)
	{
		for (IModelChangedListener listener : this.listeners)
		{
			if (listener == null)
				removeModelDataChangedListener(listener);
			else
				listener.onChildRemoved(child);
		}

		if (this.parent != null && this.parent instanceof ModelDataBase)
			((ModelDataBase) this.parent).fireOnModelDataChanged(child);
	}

	/** @return the parent model */
	public IModelData getParent()
	{
		return this.parent;
	}
}
