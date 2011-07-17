package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.util.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Epsilon Tensor (ok, will do so in the future, now we're just
 * testing stuff)
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonTensor extends ModelDataBase
{

	/** Position of the object in screen coordinates. */
	private final Point position;

	/** Rotation of the current figure */
	private double rotation = 0;

	/** Anchors for this tensor. */
	private final List<TensorConnectionAnchor> anchors;

	/**
	 * Constructor.
	 */
	public EpsilonTensor(ModelDataBase parent, EDirection direction)
	{
		this(parent, new Point(0, 0), direction);
	}

	/**
	 * Constructor w/ initial coordinates.
	 */
	public EpsilonTensor(
			ModelDataBase parent,
			Point position,
			EDirection direction)
	{
		super(parent);
		this.position = position;

		this.anchors = new ArrayList<TensorConnectionAnchor>();
		this.anchors.add(new TensorConnectionAnchor(this, direction));
		this.anchors.add(new TensorConnectionAnchor(this, direction));
		this.anchors.add(new TensorConnectionAnchor(this, direction));
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

	/** Sets the position of the current point. TODO refactor to base interface */
	public void setPosition(Point p)
	{
		this.position.setLocation(p);
		fireOnModelDataChanged(this);
	}

	/**
	 * Sets the rotation of the current point in degrees. TODO refactor to base
	 * interface
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
}
