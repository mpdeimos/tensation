package com.mpdeimos.tensation.model;

import com.mpdeimos.tensation.model.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.util.ImmutableList;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * abstract base class for tensors.
 * 
 * @author mpdeimos
 * 
 */
public abstract class TensorBase extends ModelDataBase implements
		IAppearanceHolder
{
	/** Position of the object in screen coordinates. */
	protected final Point position;

	/** Rotation of the current figure */
	private double rotation = 0;

	/** Anchors for this tensor. */
	protected final List<TensorConnectionAnchor> anchors = new ArrayList<TensorConnectionAnchor>();

	/** The appearance container of this object. */
	private final AppearanceContainer appearanceContainer = new AppearanceContainer();

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
		rotation %= 360; // ensure 0..2PI values

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

	/** @return the anchors being connected. */
	public List<TensorConnectionAnchor> getOccupiedAnchors()
	{
		List<TensorConnectionAnchor> l = new LinkedList<TensorConnectionAnchor>();
		for (TensorConnectionAnchor anchor : this.anchors)
		{
			if (anchor.isOccopied())
			{
				l.add(anchor);
			}
		}
		return l;
	}

	@Override
	public AppearanceContainer getAppearanceContainer()
	{
		return this.appearanceContainer;
	}
}