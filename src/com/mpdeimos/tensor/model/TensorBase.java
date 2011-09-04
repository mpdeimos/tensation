package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.util.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * abstract base class for tensors.
 * 
 * @author mpdeimos
 * 
 */
public abstract class TensorBase extends ModelDataBase
{
	/** Position of the object in screen coordinates. */
	protected final Point position;

	/** Rotation of the current figure */
	private double rotation = 0;

	/** Anchors for this tensor. */
	protected final List<TensorConnectionAnchor> anchors = new ArrayList<TensorConnectionAnchor>();

	/** Constructor. */
	public TensorBase(IModelData parent)
	{
		super(parent);

		this.position = new Point(0, 0);
	}

	/**
	 * @return the position of this object.
	 */
	public Point getPosition()
	{
		return this.position;
	}

	/**
	 * @return the rotation of this figure in degrees
	 */
	public double getRotation()
	{
		return this.rotation;
	}

	/** Sets the position of the current point. */
	public void setPosition(Point p)
	{
		this.position.setLocation(p);
		fireOnModelDataChanged(this);
	}

	/**
	 * Sets the rotation of the current point in degrees.
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
		fireOnModelDataChanged(this);
	}

	/** @return the anchors of this tensor. */
	public ImmutableList<TensorConnectionAnchor> getAnchors()
	{
		return new ImmutableList<TensorConnectionAnchor>(this.anchors);
	}

	@Override
	public boolean remove()
	{
		for (TensorConnectionAnchor anchor : this.anchors)
		{
			anchor.doOnTensorRemoved();
		}
		return super.remove();
	}

	/** Duplicates this tensor with the current model as parent. */
	public TensorBase duplicate()
	{
		return duplicate(this.parent);
	}

	/** Duplicates this tensor with the given model as parent. */
	abstract public TensorBase duplicate(IModelData root);
}