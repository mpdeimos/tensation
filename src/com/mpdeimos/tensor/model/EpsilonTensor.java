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
	private double rotation = 45;

	/**
	 * Constructor.
	 */
	public EpsilonTensor(ModelDataBase parent)
	{
		this(parent, new Point(0,0));
	}
	
	/**
	 * Constructor w/ initial coordinates.
	 */
	public EpsilonTensor(ModelDataBase parent, Point position)
	{
		super(parent);
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
	}
	
	/** Sets the rotation of the current point in degrees. TODO refactor to base interface */
	public void setRotation(double rotation) {
		this.rotation = rotation;
		fireOnModelDataChanged(this);
	}

}
