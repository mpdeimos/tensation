package com.mpdeimos.tensor.model;

import java.awt.Point;

/**
 * Represents an Epsilon Tensor (ok, will do so in the future, now we're just testing stuff)
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensor extends ModelDataBase {
	
	/** Position of the object in screen coordinates. */
	private final Point position;
	
	/** Rotation of the current figure */
	private short rotation = 0;

	private final ModelDataBase parent;

	/**
	 * Constructor.
	 */
	public EpsilonTensor(ModelDataBase /** TODO */ parent)
	{
		this(parent, new Point(0,0));
	}
	
	/**
	 * Constructor w/ initial coordinates.
	 */
	public EpsilonTensor(ModelDataBase parent, Point position)
	{
		this.parent = parent;
		this.position = position;
	}

	/**
	 * @return the position of this object.
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @return the rotation of this figure in degrees
	 */
	public double getRotation() {
		return rotation;
	}

	/** Sets the position of the current point. TODO refactor to base interface */
	public void setPosition(Point p) {
		this.position.setLocation(p);
		fireOnModelDataChanged(this);
		if (parent != null)
			parent.fireOnModelDataChanged(this);
	}

}
