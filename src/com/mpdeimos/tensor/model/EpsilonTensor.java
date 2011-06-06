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

	/**
	 * Constructor.
	 */
	public EpsilonTensor()
	{
		this(new Point(0,0));
	}
	
	/**
	 * Constructor w/ initial coordinates.
	 */
	public EpsilonTensor(Point position)
	{
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
		return 0;
	}

}
